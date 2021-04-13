import java.io.Serializable;


public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -1675867563027817666L;
	
	public String from;
	public String to;
	public String message;
	public ChatMessage(String from, String message) {
		this(from, null, message);
	}
	public ChatMessage(String from, String to, String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}
	public String toString() {
		if(to == null) {
			return from+": "+message;
		} else {
			return from+": (private message to "+to+") "+message;
		}
	}
}
