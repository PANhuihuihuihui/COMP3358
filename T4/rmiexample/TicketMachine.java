import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicketMachine extends Remote {
	public int getTicket() throws RemoteException;
}
