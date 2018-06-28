package plus.ojbk;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SinaPicbedApplicationTests {

	@Test
	public void contextLoads() {
	}
	public static void main(String[] args) {
		String reason = "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u767b\u5f55\u540d";
		
		try {
		    if (reason.equals(new String(reason.getBytes("ISO8859-1"), "ISO8859-1"))) {
		       reason = new String(reason.getBytes("ISO8859-1"), "UTF-8");
		       System.err.println(reason);
		    }
		} catch (UnsupportedEncodingException e) {
		}

	}
}
