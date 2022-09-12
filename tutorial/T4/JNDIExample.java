import javax.naming.* ;
import java.util.Hashtable ; 

public class JNDIExample {

	public static void main(String[] args) {
		
		 Hashtable<String, String> env = new Hashtable<String, String>() ;
		 env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory") ;
		 env.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com") ;
		 env.put(Context.SECURITY_AUTHENTICATION, "simple") ;
		 env.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system") ;
		 env.put(Context.SECURITY_CREDENTIALS, "secret") ;
		 
		 try {
			 Context ctx = new InitialContext(env) ;
			 Message message = new Message("phj", "T4", "Testing!");
			 ctx.rebind( "cn=test", message) ;
			 
			 Message msg = (Message) ctx.lookup( "cn=test") ;

			 System.out.println(msg) ;
		 } catch (NamingException e) {
			 e.printStackTrace() ; 
		 }	
	}

}
