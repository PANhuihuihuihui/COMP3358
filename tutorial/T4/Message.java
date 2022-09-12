import java.io.Serializable;

public class Message implements Serializable{
	String user;
	String subject;
	String content;

	public Message(String user, String subject, String content) {
		this.user = user;
		this.subject = subject;
		this.content = content;
	}
	public String toString() {
		return "["+this.user+"] "+this.subject+" <sep> "+this.content;
	}
}