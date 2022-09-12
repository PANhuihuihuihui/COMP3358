import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.rmi.*;
import java.rmi.registry.*;




public class MessageBox implements Runnable, DocumentListener {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MessageBox(args[0]));
	}
	
	private int wordCount;
	private JLabel wordCountLabel;
	private JTextArea msgBox;
	private WordCount wordCounter;
	public MessageBox(String host) {
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			wordCounter = (WordCount)registry.lookup("WordCounter");
		} catch(Exception e) {
			System.err.println("Failed accessing RMI: "+e);
		}
	}

	public void updateCount() {
		// TODO: update variable wordCount according to the content in msgBox
		if(wordCounter != null) {
			try {
				wordCount = wordCounter.count(msgBox.getText());
			} catch (RemoteException e) {
				System.err.println("Failed invoking RMI: ");
			}
		}
	
	}
	
	public void run() {
		JFrame frame = new JFrame("Message Box");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		msgBox = new JTextArea();
		msgBox.getDocument().addDocumentListener(this);
		msgBox.setPreferredSize(new Dimension(400,300));
		frame.add(msgBox, BorderLayout.CENTER);
		
		JPanel wordCountPane = new JPanel();
		wordCountPane.add(new JLabel("Word count:"));
		
		wordCount = 0;
		wordCountLabel = new JLabel(""+wordCount);
		wordCountPane.add(wordCountLabel);
		
		frame.add(wordCountPane, BorderLayout.PAGE_END);
		
		frame.pack();
		frame.setVisible(true);
	}
	

	/* Document Listener */
	public void insertUpdate(DocumentEvent e) {
		new WordCountUpdater().execute();
	}
	public void removeUpdate(DocumentEvent e) {
		new WordCountUpdater().execute();
	}
	public void changedUpdate(DocumentEvent e) {
		new WordCountUpdater().execute();
	}

	/* Word count updater */
	private class WordCountUpdater extends SwingWorker<Void, Void> {

		protected Void doInBackground() {
			updateCount();
			return null;
		}
		protected void done() {
			wordCountLabel.setText(""+wordCount);
			wordCountLabel.invalidate();
		}
	}

	
}
