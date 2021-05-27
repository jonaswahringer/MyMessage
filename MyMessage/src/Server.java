import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Server extends JFrame implements Runnable {
    ServerSocket myServerSocket;
    Socket mySocket;
    BufferedReader in;
    PrintWriter out;
    InputStream inputStream;
    OutputStream outputStream;
    Thread serverThread;

    JPanel topPanel, mainPanel, bottomPanel;
    JTextField portField, messageField;
    JTextArea conversationArea;
    JButton sendButton, connectButton;
    JLabel title, portLabel;
    
    Boolean connectionFlag;

    public Server() {
        super("MyMessage - Server");
        this.setBounds(400, 400, 420, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        //topPanel // connectionArea
        title = new JLabel("MyMessage");
        title.setBounds(170, 5, 100, 50);
        title.setFont(new Font("Calibri", Font.BOLD, 16));

        // topPanel = new JPanel();
        // topPanel.setLayout(null);

        portField = new JTextField();
        portField.setBounds(150, 50, 50, 40);
        portLabel = new JLabel("Port");
        portLabel.setLabelFor(portField);

        connectButton = new JButton("Connect");
        connectButton.setBounds(200, 50, 80, 40);
        connectButton.setOpaque(true);
        connectButton.setForeground(Color.RED);
        connectButton.addActionListener(new connectListener());

        this.add(title);
        this.add(portLabel);
        this.add(portField);
        this.add(connectButton);

        conversationArea = new JTextArea();
        conversationArea.setBounds(40, 100, 340, 360);
        conversationArea.setEditable(false);
        this.add(conversationArea);

        //bottomPanel // messageArea
        messageField = new JTextField();
        messageField.setBounds(40, 480, 250, 50);
        this.add(messageField);
        
        sendButton = new JButton("Send");
        sendButton.setBounds(290, 485, 100, 40);
        sendButton.addActionListener(new sendListener());
        this.add(sendButton);
        
        this.setFocusable(false);
        
        connectionFlag=false;
        serverThread = new Thread(this);
        
        this.setVisible(true);
    }

    public void createServer() {
        try {
            myServerSocket = new ServerSocket(Integer.parseInt(portField.getText()));
            mySocket = myServerSocket.accept();
            inputStream = mySocket.getInputStream();
            outputStream = mySocket.getOutputStream();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionFlag = true;
        connectButton.setForeground(Color.GREEN);
        serverThread.start();
    }

    public void sendMessage() {
    	conversationArea.setEditable(true);
    	out = new PrintWriter(outputStream, true);
        out.println(messageField.getText());
        conversationArea.append("Ich: "+messageField.getText() +"\n");
        messageField.setText("");
        conversationArea.setEditable(false);
    }

    class connectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	if(connectionFlag == false) {
        		createServer();
        	}
        	else {
        		System.out.println("Already connected: disconnect first");
        	}
        }
    }

    class sendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendMessage();
            messageField.setText("");
        }
    }

    public void refreshArea() {
    	System.out.println("Refresh-Server");
    	String message;
    	try {
			if((message = in.readLine()) != null) {
				System.out.println("Client: " + message);
				conversationArea.append("Client: "+ message +"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	System.out.println("IN GEWRITED");
    }

	@Override
	public void run() {
		in = new BufferedReader(new InputStreamReader(inputStream));
		
		while(connectionFlag) {
			System.out.println(serverThread.getState());
			System.out.println("Test - Run - Server");
			refreshArea();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}


}
