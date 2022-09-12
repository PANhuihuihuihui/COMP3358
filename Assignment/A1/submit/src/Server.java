import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.*;
import java.rmi.server.*;

public class Server extends UnicastRemoteObject implements ServerFunction {

	public static void main(String[] args) {
		try {
			Server app = new Server();
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("Server", app);
            // try to clean the file at each time the server restart
			try {
				FileWriter f = new FileWriter(new File("UserInfo.txt"));
				f.write("");
				f.close();
				f = new FileWriter(new File("OnlineUser.txt"));
				f.write("");
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}
	}
	
	public Server() throws RemoteException {}

	public int count(String message) throws RemoteException {
		if (message.isEmpty())
			return 0;
		else
			return message.split(" +").length;
	}


    @Override
    public int Login(String username,String pwd) throws RemoteException{
        int state;

        if (!userList.contains(username)){
            state = 1; // no this user
        }
        else if (!pwd.equals(this.password.get(userList.indexOf(username)))){
            state = 2; // has this user but wrong password 
        }
        else if (onlineUserList.contains(username)){
            state = 3;// already login user
        }
        else{
            // sucessfull login! and update the Online user list
            onlineUserList.add(username);
            writeToFile(username, "", "OnlineUser.txt");
            state = 4;

        }
        return state;
    }
    @Override
	public int Register(String username, String password) throws RemoteException {
		int state = 0;
		if (userList.contains(username))
			state = 5;//user name already exist (enter user name again)
		else {
            //valid register
			userList.add(username);
			this.password.add(password);
			writeToFile(username, password, "UserInfo.txt");
			// try to login 
			Login(username, password);
			state = 4; // succeed
		}
		return state;
	}

	@Override
	public void Logout(String username) throws RemoteException {
		// update online user list
        onlineUserList.remove(username);
		
		// update the local file OnlineUser.txt
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("OnlineUser.txt"));
			for (int i = 0; i < onlineUserList.size(); i++) {
				out.write("" + onlineUserList.get(i));
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			System.out.println("Fail to write to file!");
			e.printStackTrace();
		}
		
	}

	@Override
	public void writeToFile(String username, String password, String fileName) throws RemoteException {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true)); // append
		    out.write(username + "#" + password);
		    out.newLine();
		    out.close();
		} catch (IOException e){
			System.out.println("Fail to write to file!");
			e.printStackTrace();
		}
		
	}

}
