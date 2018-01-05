import java.io.Serializable;


class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private String text;

	/**	Message
	 * @param text the message encapsulated as a Message object
	 */
	Message(String text){
		this.text = text;
	}
	
	String get() { return this.text; }

	boolean equals(Message m1) {
		return this.get().equals(m1.get());
	}
}
