import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;



public class Client implements Runnable {
	// server that handel the action
	private ServerFunction server;
	// for login and register page we store the length information
	private int userLength = 0;
	private int pwdLength = 0;
	private int r_userLength = 0;
	private int r_pwdLength = 0;
	private int r_confirmLength = 0;
	// JFrame
	private JFrame frame_Login; // login page
	private JFrame frame_Register; // register page
	private JFrame frame_Game; // game page
	// panel for game window (change in action_listener)
	private MyPanel panel;
	// the table of leader board
	private MyTable table; 
	// JTextField
	private JTextField name;
	private JPasswordField password;
	private JTextField rname;
	private JPasswordField rpassword;
	private JPasswordField cpassword;
	private String _username = "Default"; 
	private String _password = "********";

	private int index = -1;
	private int preindex = -1;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Client(args[0]));
	}

	public Client(String host) {
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			server = (ServerFunction)registry.lookup("Server");
		} catch(Exception e) {
			System.err.println("Failed accessing RMI: "+e);
		}
	}
	public Client() {
		try {

			server = (ServerFunction) Naming.lookup("Server");
		} catch(Exception e) {
			System.err.println("Failed accessing RMI: "+e);
		}
	}



	
	public void run() {
		frame_Login = new JFrame("Login");
		frame_Login.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame_Register = new JFrame("Register");
		frame_Register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame_Game = new JFrame("JPoker 24-Game");
		frame_Game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame_Game.addWindowListener(new WindowAdapter() {  
			public void windowClosing(WindowEvent e) {  
				super.windowClosing(e);  
				if (server != null) {
					try {
						server.Logout(_username);
						frame_Game.setVisible(false);
						frame_Register.setVisible(false);
						frame_Login.setVisible(true);
					} catch (RemoteException err) {
						System.err.println("Failed invoking RMI: ");
					}
				}
			}  
			  
		}); 
		
		// GUI Design for Login window
		JPanel title = new JPanel();
		title.setPreferredSize(new Dimension(260,150));
		title.setBorder(BorderFactory.createTitledBorder("LOGIN"));
		
		JPanel login = new JPanel();
		login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));

		JPanel field1 = new JPanel();
		field1.setLayout(new BorderLayout());

		JLabel title1 = new JLabel("Login Name");
		name = new JTextField(); // login name
		name.getDocument().addDocumentListener(new MyDocumentListener("name"));
		name.setPreferredSize(new Dimension(250, 25));

		field1.add(title1, BorderLayout.CENTER);
		field1.add(name, BorderLayout.PAGE_END);
		login.add(field1);
		
		JPanel field2 = new JPanel();
		field2.setLayout(new BorderLayout());
		JLabel title2 = new JLabel("Password");
		password = new JPasswordField(); // login password
		password.getDocument().addDocumentListener(new MyDocumentListener("password"));
		password.setPreferredSize(new Dimension(250, 25));
		field2.add(title2, BorderLayout.CENTER);
		field2.add(password, BorderLayout.PAGE_END);
		login.add(field2);
		
		JPanel buttons = new JPanel();
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new MyActionListener("login"));
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new MyActionListener("register"));
		buttons.add(loginButton);
		buttons.add(registerButton);
		login.add(buttons);
		title.add(login, new Integer(0));
		frame_Login.add(title, BorderLayout.CENTER);
		
		frame_Login.pack();
		frame_Login.setVisible(true);
		
		// GUI Design for Register window
		JPanel register = new JPanel();
		register.setBorder(BorderFactory.createTitledBorder("Register"));
		register.setPreferredSize(new Dimension(260, 170));
		
		JPanel rfield1 = new JPanel();
		rfield1.setLayout(new BorderLayout());
		JLabel rtitle1 = new JLabel("Login Name");
		rname = new JTextField(); // login name
		rname.getDocument().addDocumentListener(new MyDocumentListener("rname"));
		rname.setPreferredSize(new Dimension(250, 25));
		rfield1.add(rtitle1, BorderLayout.CENTER);
		rfield1.add(rname, BorderLayout.PAGE_END);
		register.add(rfield1);
		
		JPanel rfield2 = new JPanel();
		rfield2.setLayout(new BorderLayout());
		JLabel rtitle2 = new JLabel("Password");
		rpassword = new JPasswordField(); // password
		rpassword.getDocument().addDocumentListener(new MyDocumentListener("rpassword"));
		rpassword.setPreferredSize(new Dimension(250, 25));
		rfield2.add(rtitle2, BorderLayout.CENTER);
		rfield2.add(rpassword, BorderLayout.PAGE_END);
		register.add(rfield2);
		
		JPanel rfield3 = new JPanel();
		rfield3.setLayout(new BorderLayout());
		JLabel rtitle3 = new JLabel("Confirm Password");
		cpassword = new JPasswordField(); // confirm password
		cpassword.getDocument().addDocumentListener(new MyDocumentListener("cpassword"));
		cpassword.setPreferredSize(new Dimension(250, 25));
		rfield3.add(rtitle3, BorderLayout.CENTER);
		rfield3.add(cpassword, BorderLayout.PAGE_END);
		register.add(rfield3);
		
		JPanel buttonPanel = new JPanel();
		JButton registerB = new JButton("Register");
		registerB.addActionListener(new MyActionListener("rregister"));
		JButton cancelB = new JButton("Cancel");
		cancelB.addActionListener(new MyActionListener("cancel"));
		buttonPanel.add(registerB);
		buttonPanel.add(cancelB);
		
		frame_Register.add(register, BorderLayout.CENTER);
		frame_Register.add(buttonPanel, BorderLayout.SOUTH);
		frame_Register.pack();
		
		// GUI Design for the Game Window
		JPanel menu =new JPanel(new FlowLayout());
		JButton profile = new JButton("User Profile");
		profile.setPreferredSize(new Dimension(149, 30));
		profile.addActionListener(new MyActionListener("userProfile"));
		menu.add(profile);
		JButton game = new JButton("Play Game");
		game.setPreferredSize(new Dimension(149, 30));
		game.addActionListener(new MyActionListener("game"));
		menu.add(game);
		JButton board = new JButton("Leader Board");
		board.setPreferredSize(new Dimension(149, 30));
		board.addActionListener(new MyActionListener("leaderBoard"));
		menu.add(board);
		JButton logout = new JButton("Logout");
		logout.setPreferredSize(new Dimension(149, 30));
		logout.addActionListener(new MyActionListener("logout"));
		menu.add(logout);
		frame_Game.add(menu, BorderLayout.NORTH);
		
		panel = new MyPanel();
		frame_Game.add(panel, BorderLayout.CENTER);
		
		frame_Game.pack();
	}
	

	/* Document Listener */
    private class MyDocumentListener implements DocumentListener {
		private String name;
		public MyDocumentListener(String name) {
			this.name = name;
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			WordCountUpdater updater = new WordCountUpdater();
			updater.setCommand(name);
			updater.execute();
		}
		@Override
		public void insertUpdate(DocumentEvent e) {
			WordCountUpdater updater = new WordCountUpdater();
			updater.setCommand(name);
			updater.execute();
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			WordCountUpdater updater = new WordCountUpdater();
			updater.setCommand(name);
			updater.execute();
		}
	}
	//update the length infomation use the word counting service
	public void updateCount(String source) {
		// 
		if (server != null) {
			try {
				if (source == "name")
					userLength = server.count(name.getText());
				else if (source == "password")
					pwdLength = server.count(new String(password.getPassword()));
				else if (source == "rname")
					r_userLength = server.count(rname.getText());
				else if (source == "rpassword")
					r_pwdLength = server.count(new String(rpassword.getPassword()));
				else if (source == "cpassword")
					r_confirmLength = server.count(new String(cpassword.getPassword()));
			} catch (RemoteException e) {
				System.err.println("Failed invoking RMI: ");
			}
		}
	}


	// self define function 

	/* Word count updater */
	private class WordCountUpdater extends SwingWorker<Void,Void>{
		private String command;
		protected void setCommand(String cmd) {
			command = cmd;
		}
		protected Void doInBackground() {
			String _command = command;
			updateCount(_command);
			return null;
		}
	}

	/* Action listener 
		this is used to check which stage are the user in and take the corresponding action
		sourece >>>> difference whice stage
	*/
	private class MyActionListener implements ActionListener {
		private String source;
		public void setSource(String name) {
			source = name;
		}
		
		public MyActionListener (String name) {
			source = name;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (source.equals("login")) {
				if (userLength == 0)
					JOptionPane.showMessageDialog(null, "Login name cannot be empty!", "ERROR", JOptionPane.ERROR_MESSAGE);
				else if (pwdLength == 0)
					JOptionPane.showMessageDialog(null, "Password cannot be empty!", "ERROR", JOptionPane.ERROR_MESSAGE);
				else {
					if (server != null) {
						try {
							int state = server.Login(name.getText(), new String(password.getPassword()));

							if (state == 1)
								JOptionPane.showMessageDialog(null, "No such user! Please Register first!", "ERROR", JOptionPane.ERROR_MESSAGE);
							else if (state == 2)
								JOptionPane.showMessageDialog(null, "Wrong Password!", "ERROR", JOptionPane.ERROR_MESSAGE);
							else if (state == 3)
								JOptionPane.showMessageDialog(null, "User Already Login!", "ERROR", JOptionPane.ERROR_MESSAGE);
							else if (state == 4) {
								// login successfully
								if (index == -1) {
									// first time to open the application
									preindex = -1;
									index = 0;
								} else {
									preindex = index;
									index = 0;
								}
								_username = name.getText();
								_password = new String(password.getPassword());
								panel = new MyPanel();
								panel.repaint();
								if (preindex == 2) {
									frame_Game.remove(table);
									frame_Game.add(panel);
								}
								frame_Game.pack();
								frame_Game.repaint();
								frame_Login.setVisible(false);
								frame_Register.setVisible(false);
								frame_Game.setVisible(true); // jump to game page
							}
						} catch (RemoteException e) {
							System.err.println("Failed invoking RMI: ");
						}
					}
				}
			} else if (source.equals("register")) {
				// jump to the register window
				frame_Login.setVisible(false);
				frame_Game.setVisible(false);
				frame_Register.setVisible(true);
				
			} else if (source.equals("rregister")) {
				// judge whether the register is successful
				if (r_userLength == 0)
					JOptionPane.showMessageDialog(null, "User name cannot be empty!", "ERROR", JOptionPane.ERROR_MESSAGE);
				else if (r_pwdLength == 0)
					JOptionPane.showMessageDialog(null, "Password cannot be empty!", "ERROR", JOptionPane.ERROR_MESSAGE);
				else if (r_confirmLength == 0)
					JOptionPane.showMessageDialog(null, "Confirm-password cannot be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
				else if (!Arrays.equals(rpassword.getPassword(), cpassword.getPassword()))
					JOptionPane.showMessageDialog(null, "Password not consistent!", "ERROR", JOptionPane.ERROR_MESSAGE);
				else if (server != null) {
					try {
						int state = server.Register(rname.getText(), new String(rpassword.getPassword()));
						if (state == 4) {
							// login successfully
							_username = rname.getText();
							_password = new String(rpassword.getPassword());
							if (index == -1) {
								// first time to open the application
								preindex = -1;
								index = 0;
								panel = new MyPanel();
								
							} else {
								preindex = index;
								index = 0;
								if (preindex == 2) {
									frame_Game.remove(table);
								}
							}
							frame_Game.add(panel);
							frame_Game.pack();
							panel.repaint();
							frame_Game.repaint();
							frame_Login.setVisible(false);
							frame_Register.setVisible(false);
							frame_Game.setVisible(true); // jump to game page
						} else if (state == 5)
							JOptionPane.showMessageDialog(null, "Username already exists, choose another username!", "ERROR", JOptionPane.ERROR_MESSAGE);
					} catch (RemoteException e) {
						System.err.println("Failed invoking RMI: ");
					}
				}
				
			} else if (source.equals( "cancel")) {
				// cancel register
				frame_Register.setVisible(false);
				frame_Game.setVisible(false);
				frame_Login.setVisible(true);
			} else if (source.equals("userProfile")) {
				// show the user profile, i.e. draw UserProfilePanel
				preindex = index;
				index = 0;
				panel.repaint();
				if (table != null) {
					frame_Game.remove(table);
					frame_Game.add(panel);
				}
				frame_Game.pack();
				frame_Game.setVisible(true);
				frame_Game.repaint();
			} else if (source.equals("game")) {
				// jump to the game page
				preindex = index;
				index = 1;
				panel.repaint();
				if (table != null) {
					frame_Game.remove(table);
					frame_Game.add(panel);
				}
				frame_Game.pack();
				frame_Game.setVisible(true);
				frame_Game.repaint();
			} else if (source.equals("leaderBoard")) {
				// show the leader board
				preindex = index;
				index = 2;
				if (table == null && preindex != 2) {
					table = new MyTable();
				}
				frame_Game.remove(panel);
				frame_Game.add(table);
				frame_Game.pack();
				frame_Game.setVisible(true);
				frame_Game.repaint();
			} else if (source.equals("logout")) {
				// logout
				if (server != null) {
					try {
						server.Logout(_username);
						frame_Game.setVisible(false);
						frame_Register.setVisible(false);
						frame_Login.setVisible(true);
					} catch (RemoteException e) {
						System.err.println("Failed invoking RMI: ");
					}
				}
			}
		}
	}

	class MyTable extends JPanel {
		final JTable table;

		public MyTable() {
			super(new GridLayout(1,0));
			// Random data is being used for now.
			String[] columnNames = {"Rank", "Player", "Games Won", "Games Played", "Average Time"};
			Object[][] data = {
					{new Integer(1), "Jackson", new Integer(10), new Integer(20), "12.5s"},
					{new Integer(2), "phj", new Integer(9), new Integer(20), "12.5s"},
					{new Integer(3), "Leo1bee", new Integer(8), new Integer(20), "12.5s"},
					{new Integer(4), "Jack", new Integer(7), new Integer(20), "12.5s"},
					{new Integer(5), "Chio", new Integer(6), new Integer(20), "12.5s"},
					{new Integer(6), "Zhoue", new Integer(5), new Integer(20), "12.5s"},
					{new Integer(7), "James", new Integer(4), new Integer(20), "12.5s"},
					{new Integer(8), "Black", new Integer(3), new Integer(20), "12.5s"},
					{new Integer(9), "Withe", new Integer(2), new Integer(20), "12.5s"},
					{new Integer(10), "Bee", new Integer(1), new Integer(20), "12.5s"}
			};
			
			table = new JTable(data, columnNames);
	        table.setPreferredScrollableViewportSize(new Dimension(150, 300));
	        table.setFillsViewportHeight(true);
	        table.setRowHeight(27);
	        //Create the scroll pane and add the table to it.
	        JScrollPane scrollPane = new JScrollPane(table);

	        //Add the scroll pane to this panel.
	        add(scrollPane);
		}
	}

	class MyPanel extends JPanel {
		/*
		* 	0 - user profile
	 	* 	1 - game
	 	* 	2 - leader board
		*/
		public MyPanel() {
			this.setPreferredSize(new Dimension(150, 340));
		}
		public void paintComponent(Graphics g) {
			if (index == 0) {

				//Toimprove: in A2
				Graphics2D g2 = (Graphics2D) g;

		        Font f = new Font("Arial", Font.BOLD, 34);

		        g2.setFont(f);
		        g2.setStroke(new BasicStroke(2));
		        g2.drawString(_username, 30, 50);
		        

				f = new Font("Arial", Font.PLAIN, 20);
				g2.setFont(f);
				g2.drawString("Number of wins: " + "10", 30, 90);
				g2.drawString("Number of games: " + "20", 30, 118);
				g2.drawString("Average time to win: " + "12.5s", 30, 146);
				

				f = new Font("Arial", Font.BOLD, 29);
				g2.setFont(f);
				g2.drawString("Rank: #" + "10", 30, 184);
				
				frame_Game.pack();
				frame_Game.setVisible(true);
			} else if (index == 1) {
				//ToDo: in A3
				frame_Game.pack();
				frame_Game.setVisible(true);

			} 
		}
	}


}
