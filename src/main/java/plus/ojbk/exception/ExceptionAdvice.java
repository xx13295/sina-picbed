package plus.ojbk.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import plus.ojbk.common.Message;
import plus.ojbk.common.Response;


@ControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class ExceptionAdvice extends Response{
	public static final Message<Void> SERVER_ERROR = new Message<>(Boolean.FALSE,null);
	private static final String AJAX_HEADER_NAME = "x-requested-with";
	private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";
	private static final String ERROR_VIEW = "/error/error";
	
	public ModelAndView errorHandle(HttpServletRequest request,HttpServletResponse response,Message<Void> message) throws IOException {
		String header = request.getHeader(AJAX_HEADER_NAME);
		if(header != null) {
			if(header.equalsIgnoreCase(AJAX_HEADER_VALUE)) {
				response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			  	response.getWriter().write(JSON.toJSONString(message));
			  	response.setStatus(HttpServletResponse.SC_OK);
			  	response.flushBuffer();
				return null;
			}
		}
		ModelAndView modelAndView = new ModelAndView(ERROR_VIEW);
		modelAndView.addObject("message", message);
		return modelAndView;
	}
	@ExceptionHandler(Exception.class)  
	public ModelAndView  handleException(HttpServletRequest request,HttpServletResponse response,Exception e) throws IOException {
		e.printStackTrace();
		if(e instanceof ServiceException) {
			return errorHandle(request,response,((ServiceException)e).getErrorMessage());
		}
		return errorHandle(request,response,SERVER_ERROR);
	}  
}
