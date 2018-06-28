package plus.ojbk.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import plus.ojbk.common.Message;
import plus.ojbk.common.Response;
import plus.ojbk.exception.ServiceException;
import plus.ojbk.util.GeneralUtils;
import plus.ojbk.util.SinaPicBedUtil;

@RestController
public class PicBedController extends Response{
	
	//新浪账户名
	@Value("${sina.username}")
	private String userName;
	
	//密码
	@Value("${sina.password}")
	private String passWord;
	private ModelAndView forwardView = null;
	
	@PostMapping("/sina/upload.php")
	@ResponseBody
	public Message<List<String>> upload(@RequestParam("file") MultipartFile[] multipartFiles, 
			@RequestParam(value="size", required=false, defaultValue="0") Integer size) throws IOException, ServiceException {
		if(GeneralUtils.isEmpty(multipartFiles)) {
			throw new ServiceException("图片不能为空");
		}else if(multipartFiles.length > 10) {
			throw new ServiceException("一次最多只能上传10张图片");
		} 
		String base64name = GeneralUtils.base64Encode(userName);
		String cookies = SinaPicBedUtil.getSinaCookie(base64name, passWord);// 持久化起来 不用每次都登录 一般失效7天左右
		List<String> url = SinaPicBedUtil.uploadFile(multipartFiles, cookies, size);
		return super.SUCCESS(url);
	}
	
	
	@GetMapping("/index.php")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("/sina/index");
		return modelAndView;
		
	}
	@GetMapping("/")
	public ModelAndView defaultPage(HttpServletRequest request) {
		if(forwardView == null) {
			forwardView = new ModelAndView("forward:" + request.getContextPath() + "/index.php");
		}
		return forwardView;
	}
}
