package plus.ojbk.common;

/**
 * 
 * @author HTTP请求返回JSON消息
 * 
 * @param <T>
 */
public class Message<T> {
	
    private boolean isSuccess;

    private String message;
 
    private T data;

    public Message() {}
    
    public Message(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Message(boolean isSuccess, T data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public Message(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
   
}
