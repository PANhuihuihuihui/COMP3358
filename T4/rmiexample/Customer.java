import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Customer {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, NamingException {
		
		Hashtable<String, String> env = new Hashtable<String, String>() ;

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system");
		env.put(Context.SECURITY_CREDENTIALS, "secret");
		
		Context ctx = new InitialContext(env);
		TicketMachine app = (TicketMachine)ctx.lookup("cn=TicketMachine");
		int ticket = app.getTicket();
		System.out.println("Got ticket number: "+ ticket);
	}

}
