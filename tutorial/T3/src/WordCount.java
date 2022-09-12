
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WordCount extends Remote {
	int count(String message) throws RemoteException;
}