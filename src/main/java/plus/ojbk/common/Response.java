package plus.ojbk.common;


public class Response {
	
	private static final String SUCCESS ="OJBK";
	public Message<Void> SUCCESS () {
		return this.SUCCESS(null);
	}
	
	public <T> Message<T> SUCCESS (T data) {
		Message<T> message = new Message<T>();
		message.setData(data);
		message.setSuccess(Boolean.TRUE);
		message.setMessage(SUCCESS);
		return message;
	}

	public <T> Message<T> ERROR(String data){
		Message<T> message = new Message<>();
		message.setSuccess(Boolean.FALSE);
		message.setData(null);
		message.setMessage(data);
		return message;
	}
}
