import java.io.Serializable;


public class Message implements Serializable{
	/**
	 * S
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	
	public Message(){
		
	}
	
	public Message(String text){
		this.text = text;
	}
	
	public String get(){
		return this.text;
	}
	
}
