import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

import javax.naming.*;


public class TicketServer extends UnicastRemoteObject implements TicketMachine {
	
	public static void main(String [] args) throws RemoteException, MalformedURLException, NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>() ;
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system");
		env.put(Context.SECURITY_CREDENTIALS, "secret");
		String rmiurl = "rmi://localhost/TicketMachine";
		Context ctx = new InitialContext(env);
		TicketMachine app = new TicketServer();

		Reference ref = new Reference("TicketMachine", new StringRefAddr("URL", rmiurl));
		
		ctx.rebind("cn=TicketMachine", ref);
		ctx.rebind(rmiurl, app);
		
		System.out.println("Service registered");
	}

	
	private int counter;
	public TicketServer() throws RemoteException {
		counter = 1000;
	}
	public int getTicket() throws RemoteException {
		return counter++;
	}
}
