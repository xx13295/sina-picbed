package plus.ojbk.exception;

import plus.ojbk.common.Message;

public class ServiceException extends Exception{

	private static final long serialVersionUID = -2804390381900229831L;
	
	private Message<Void> message;
	
	public ServiceException(String message) {
		super(message);
		this.message = new Message<>();
		this.message.setSuccess(Boolean.FALSE);
		this.message.setMessage(message);
	}
	public Message<Void> getErrorMessage(){
		return this.message;
	}
}
