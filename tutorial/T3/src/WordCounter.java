import java.rmi.*;
import java.rmi.server.*;

public class WordCounter extends UnicastRemoteObject implements WordCount {

	public static void main(String[] args) {
		try {
			WordCounter app = new WordCounter();
			//int count = app.count("The quick brown fox jumps over a lazy dog");
			//System.out.println("There are "+count+" words");
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("WordCounter", app);
			System.out.println("Service registered");
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}
	}
	
	public WordCounter() throws RemoteException {} 
	public int count(String message) throws RemoteException {
		return message.split(" +").length;
	}

}
