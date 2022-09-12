import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ChatClient implements MessageListener {
	
	public static void main(String [] args) {
		try {
			if(args.length > 0) {
				new ChatClient(args[0]).start();
			} else {
				new ChatClient().start();
			}
		} catch (NamingException | JMSException e) {
			System.err.println("Program aborted.");
		}
	}
	
	private JMSHelper jmsHelper;
	private MessageProducer queueSender;
	private MessageConsumer topicReceiver;
	
	private JFrame frame;
	private JTextArea messageArea;
	private JTextField messageBox;
	private String name;
	
	public ChatClient() throws NamingException, JMSException {
		jmsHelper = new JMSHelper();
		init();
	}
	public ChatClient(String host) throws NamingException, JMSException {
		jmsHelper = new JMSHelper(host);
		init();
	}
	private void init() throws JMSException {
		name = JOptionPane.showInputDialog(null, "Please enter a name", "Pick a name", JOptionPane.QUESTION_MESSAGE);
		queueSender = jmsHelper.createQueueSender();
		topicReceiver = jmsHelper.createTopicReader(name);
		topicReceiver.setMessageListener(this);
	}

	public void start() {
		frame = new JFrame();
		if(name != null && !(name = name.trim()).isEmpty()) {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("JMS chat box ["+name+"]");
			messageArea = new JTextArea();
			messageArea.setEditable(true);
			messageArea.setBackground(Color.WHITE);
			((DefaultCaret)messageArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
			JScrollPane scrollPane = new JScrollPane(messageArea);
			scrollPane.add(messageArea);
			scrollPane.setViewportView(messageArea);
			scrollPane.setPreferredSize(new Dimension(600,300));
			 
			messageBox = new JTextField();
			messageBox.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						sendMessage();
					}
				}
			});
			
			frame.add(scrollPane, BorderLayout.CENTER);
			frame.add(messageBox, BorderLayout.PAGE_END);
			frame.pack();
			frame.setVisible(true);
			
			messageBox.requestFocus();
		}
	}
	
	private ChatMessage getChatMessage() {
		String message = messageBox.getText().trim();
		int colonPos = message.indexOf(":");
		if(colonPos >= 0){
			String target = message.substring(0, colonPos).trim();
			String newMessage = message.substring(colonPos+1).trim();
			if (!target.isEmpty() && !newMessage.isEmpty()){
				return new ChatMessage (name,target,newMessage);
			}
		}
		if(!message.isEmpty()) {
			return new ChatMessage(name, message);
		}
		return null;
	}
	
	public void sendMessage() {
		ChatMessage chatMessage = getChatMessage();
		messageBox.setText("");
		if(chatMessage != null) {
			System.out.println("Trying to send message: "+chatMessage);
			Message message = null;
			try {
				message = jmsHelper.createMessage(chatMessage);
			} catch (JMSException e) {
			}
			if(message != null) {
				try {
					queueSender.send(message);
				} catch (JMSException e) {
					System.err.println("Failed to send message");
				}
			}
		}
	}
	public void onMessage(Message jMessage){
		try {
			ChatMessage chatMessage = (ChatMessage)((ObjectMessage)jMessage).getObject();
			// messageArea.append(chatMessage.toString());
			String s = chatMessage.toString()+System.lineSeparator();
			// System.out.println("receive message: "+ s);
			messageArea.append(s);
			
			messageArea.invalidate();
			// System.out.println("succefull repaint1: "+ chatMessage.toString());
			// messageArea.append(chatMessage.toString()+System.lineSeparator());
			// System.out.println("succefull repaint1: "+ chatMessage.toString());
			// System.out.println("succefull repaint2: "+ chatMessage.toString());
			// frame.repaint();
			// System.out.println("succefull repaint3: "+ chatMessage.toString());
		} catch (Exception e) {
			System.err.println("Failed to receive message");
		}
	}

}
