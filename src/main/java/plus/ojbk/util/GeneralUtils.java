package plus.ojbk.util;


import java.awt.Image;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

public class GeneralUtils {
	
    private static final int[] NUMBERS = {0,1,2,3,4,5,6,7,8,9};
    
    private static final String[] LETTERS = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t", "u","v","w","x","y","z"};
    
    /**
     * 数组是否为空
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T[] t){
        return t == null || t.length == 0;
    }

    /**
     * 字符串是否为空
     * @param string
     * @return
     */
    public static boolean isEmpty(String string){
        return string == null || string.trim().isEmpty();
    }

    /**
     * Set集合是否为空
     * @param set
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Set<T> set){
        return set == null || set.isEmpty();
    }

    /**
     * Collection是否为空
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * Map是否为空
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> boolean isEmpty(Map<K,V> map){
        return map == null || map.isEmpty();
    }
    
    /**
     * 获取32位无符号大写UUID
     * @return
     */
    public static String getUpperUUID(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
    
    /**
     * 获取32位无符号小写UUID
     * @return
     */
    public static String getLowerUUID(){
        return UUID.randomUUID().toString().replace("-","").toLowerCase();
    }
    
    /**
     * 获取0-9随机字符字符串
     * @param length
     * @return
     */
    public static String getRandomNum(int length){
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for(int x = 0;x < length ; x++){
            sb.append(NUMBERS[random.nextInt(NUMBERS.length)]);
        }
        return sb.toString();
    }
    
    /**
     * 获取a-z随机字符串
     * @param length
     * @return
     */
    public static String getRandomLetter(int length){
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for(int x = 0;x < length ; x++){
            sb.append(LETTERS[random.nextInt(LETTERS.length)]);
        }
        return sb.toString();
    }
    
    /**
     * 获取数组中的随机默认值
     * @param arr
     * @return
     */
    public static <T> T choose(T[] arr) {
    	return arr[new Random().nextInt(arr.length)];
    }
    
    /**
     * 获取集合中的随机值
     * @param list
     * @return
     */
    public static <T> T choose(List<T> list) {
    	return list.get(new Random().nextInt(list.size()));
    }
    
    
    /**
     * 对字符串进行Base64编码
     * @param data
     * @return
     */
    public static String base64Encode(String data){
        return new String(Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
    }

    /**
     * 对指定Base64编码字符串进行解码
     * @param data
     * @return
     */
    public static String base64Decode(String data){
        return new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
    }
    
    /**
     * 当前操作系统是否是Windows操作系统
     * @return
     */
    public static Boolean isWindows() {
    	return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

	 /**
     * 判断文件是否是图片文件
     * @param inputStream
     * @return
     */
    public static boolean isImage(InputStream inputStream) {
		if (inputStream == null) {
            return false;
        }
        Image image;
        try {
        	image = ImageIO.read(inputStream);
            return !(image == null || image.getWidth(null) <= 0 || image.getHeight(null) <= 0);
        } catch (Exception e) {
            return false;
        }
	}
}
