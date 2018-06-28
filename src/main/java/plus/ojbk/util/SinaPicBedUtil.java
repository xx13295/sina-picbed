package plus.ojbk.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import plus.ojbk.exception.ServiceException;

/**
 * 
 * @author 王小明
 *
 */
public class SinaPicBedUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SinaPicBedUtil.class);

	private static final String UPLOAD_URL = "http://picupload.service.weibo.com/interface/pic_upload.php?mime=image%sFjpeg&data=base64&url=0&markpos=1&logo=&nick=0&marks=1&app=miniblog&cb=http://weibo.com/aj/static/upimgback.html?_wv=5&callback=STK_ijax_%s";

	private static final String LOGIN_URL = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.19)&_=1530066315000";

	private static String responseEntity = "{\"code\":\"https://ojbk.plus\",\"data\":{\"count\":-1}}";

	private static final String PID_PATTERN = "^[a-zA-Z0-9]{32}$";

	/**
	 * sina图片上传 
	 * @param multipartFiles
	 * @param cookies
	 * @param size
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ServiceException 
	 */
	public static List<String> uploadFile(MultipartFile[] multipartFiles, String cookies, Integer size) throws ClientProtocolException, IOException, ServiceException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String dateline = String.valueOf(System.currentTimeMillis());
		dateline = dateline.substring(0, 10);
		List<String> urls = new ArrayList<>();
		try {
			HttpPost httpPost = new HttpPost(String.format(UPLOAD_URL, "%2", dateline));
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(100000).setSocketTimeout(100000).build();
			httpPost.setConfig(requestConfig);
			String fileName = null;
			String prefix = null;
			File excelFile = null;
			for(MultipartFile multipartFile:multipartFiles) {
				if(!GeneralUtils.isImage(multipartFile.getInputStream())) {
					throw new ServiceException("兄dei,只能上传图片文件~");
				}
				fileName = multipartFile.getOriginalFilename();
		        fileName.substring(fileName.lastIndexOf("."));
		        excelFile = File.createTempFile(GeneralUtils.getLowerUUID(), prefix);
		        multipartFile.transferTo(excelFile);
		        FileBody bin = new FileBody(excelFile);
				HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("pic1", bin).build();
				httpPost.addHeader("Cookie", cookies);
				httpPost.setHeader(new BasicHeader("Charset", "UTF-8"));
				httpPost.setEntity(reqEntity);
				CloseableHttpResponse response = httpclient.execute(httpPost);
				LOGGER.info("Connection state {} ", response.getStatusLine());
				try {
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						responseEntity = EntityUtils.toString(response.getEntity());
						LOGGER.info("Success");
						responseEntity = responseEntity.substring(responseEntity.indexOf("code") - 2);
						JSONObject json = JSONObject.parseObject(responseEntity);
						JSONObject data = json.getJSONObject("data");
						int count = data.getInteger("count");
						if (count > 0) {
							JSONObject pic1 = data.getJSONObject("pics").getJSONObject("pic_1");
							Integer num = pic1.getInteger("ret");
							if (num == 1) {
								String pid = pic1.getString("pid");
								String imgpath = getImageUrl(pid, size);
								if(imgpath != null) {
									urls.add(imgpath);
								}
							}else {
								LOGGER.info("The expected value is 1, and the actual value is ", num);
							}
						}
					}
					EntityUtils.consume(resEntity);
				} finally {
					deleteFile(excelFile);
					response.close();
				}
			}
		} finally {
			httpclient.close();
		}
		return urls;
	}
	
	/**
	 * 删除缓存文件
	 * @param files
	 */
	private static void deleteFile(File... files) {  
        for (File file : files) {  
            if (file.exists()) {  
                file.delete();
            }  
        }  
    }
	
	/**
	 * 获取图片的url
	 * 
	 * @param pid 图片ID
	 * @param index 图片尺寸下标
	 * @return
	 */
	public static String getImageUrl(String pid, int index) {
		String[] sizeArr = new String[] { "large", "mw1024", "mw690", "bmiddle", "small", "thumb180", "thumbnail", "square" };
		pid = pid.trim();
		String size = sizeArr[index];
		Pattern p = Pattern.compile(PID_PATTERN);
		Matcher m = p.matcher(pid);
		if (m.matches()) {
			CRC32 crc32 = new CRC32();
			crc32.update(pid.getBytes());
			StringBuilder img = new StringBuilder("https://ws");
			img.append((crc32.getValue() & 3) + 1).append(".sinaimg.cn/").append(size)
			.append("/").append(pid).append(".").append(pid.charAt(21) == 'g' ? "gif" : "jpg");
			LOGGER.info("Image URL {} ", img);
			return img.toString();
		}
		return null;
	}

	/**
	 * 获取Sina Cookies
	 * 
	 * @param username 用户名经过base64加密
	 * @param password
	 * @return Cookies
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ServiceException 
	 */
	public static String getSinaCookie(String username, String password) throws ClientProtocolException, IOException, ServiceException {
		StringBuilder sinaCookie = new StringBuilder("SUB=");
		CookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		HttpPost httpPost = new HttpPost(LOGIN_URL);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("entry", "sso"));
		list.add(new BasicNameValuePair("gateway", "1"));
		list.add(new BasicNameValuePair("from", "null"));
		list.add(new BasicNameValuePair("savestate", "30"));
		list.add(new BasicNameValuePair("useticket", "0"));
		list.add(new BasicNameValuePair("pagerefer", ""));
		list.add(new BasicNameValuePair("vsnf", "1"));
		list.add(new BasicNameValuePair("su", username));
		list.add(new BasicNameValuePair("service", "sso"));
		list.add(new BasicNameValuePair("sp", password));
		list.add(new BasicNameValuePair("sr", "1920*1080"));
		list.add(new BasicNameValuePair("encoding", "UTF-8"));
		list.add(new BasicNameValuePair("cdult", "3"));
		list.add(new BasicNameValuePair("domain", "sina.com.cn"));
		list.add(new BasicNameValuePair("prelt", "0"));
		list.add(new BasicNameValuePair("returntype", "TEXT"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
		httpPost.setEntity(entity);
		CloseableHttpResponse chr = httpClient.execute(httpPost);
		String respones = EntityUtils.toString(chr.getEntity());
		JSONObject reason = JSONObject.parseObject(respones);
		if(reason.containsKey("reason")) {
			throw new ServiceException(reason.getString("reason"));
		}
		List<Cookie> cookies = cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			if (cookies.get(i).getName().equals("SUB")) {
				sinaCookie.append(cookies.get(i).getValue()).append(";");
			}
		}
		return sinaCookie.toString();
	}
	
}
