package model;

public class Message {

	private String type;
	
	private String message;
	
	
	public Message(String message) {
		
		this.message = message;
		
		type = "Message";
		
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMessageText() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
