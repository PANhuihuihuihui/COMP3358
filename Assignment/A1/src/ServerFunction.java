import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerFunction extends Remote {
    
    ArrayList<String> userList = new ArrayList<String>();
	ArrayList<String> password = new ArrayList<String>();
	ArrayList<String> onlineUserList = new ArrayList<String>();
    
	/*
	 * Validate user from UserInfo.txt
	 * Avoid Repeated login using OnlineUser.txt
	 * Update OnlineUser.txt
	 */
	int count (String message) throws RemoteException;

	int Login(String username, String password) throws RemoteException;
	
	/*
	 * Avoid duplicating user name with UserInfo.txt
	 * login user and update OnlineUser.txt
	 */
	int Register(String username, String password) throws RemoteException;
	
	/*
	 * Update OnlineUser.txt
	 */
	void Logout(String username) throws RemoteException;
	

	
	// write the username and password to the local file
	void writeToFile(String username, String password, String fileName) throws RemoteException;
}