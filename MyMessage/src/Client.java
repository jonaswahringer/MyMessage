import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements Runnable, KeyListener {
    Socket myClientSocket;
    BufferedReader in;
    PrintWriter out;
    InputStream inputStream;
    OutputStream outputStream;
    Thread clientThread;

    JPanel topPanel, mainPanel, bottomPanel;
    JTextField ipField, portField, messageField;
    JTextArea conversationArea;
    JButton sendButton, connectButton;
    JLabel title, ipLabel, portLabel;
    
    Boolean connectionFlag;

    public Client() {
        super("MyMessage - Client");
        this.setBounds(400, 400, 420, 580);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        //topPanel // connectionArea
        title = new JLabel("MyMessage");
        title.setBounds(170, 5, 100, 50);
        title.setFont(new Font("Calibri", Font.BOLD, 16));

        // topPanel = new JPanel();
        // topPanel.setLayout(null);

        ipField = new JTextField();
        ipField.setBounds(120, 50, 70, 40);
        ipLabel = new JLabel("IP");
        ipLabel.setLabelFor(ipField);

        portField = new JTextField();
        portField.setBounds(190, 50, 50, 40);
        portLabel = new JLabel("Port");
        portLabel.setLabelFor(portField);

        connectButton = new JButton("Connect");
        connectButton.setBounds(240, 50, 80, 40);
        connectButton.setOpaque(true);
        connectButton.setForeground(Color.RED);
        connectButton.addActionListener(new connectListener());

        this.add(title);
        this.add(ipLabel);
        this.add(ipField);
        this.add(portLabel);
        this.add(portField);
        this.add(connectButton);


        //mainPanel // Conversation Area
        // mainPanel = new JPanel();
        // mainPanel.setLayout(null);

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
        this.addKeyListener(this);

        // this.add(topPanel);
        // this.add(mainPanel);
        // this.add(bottomPanel);
        
        connectionFlag=false;
        clientThread = new Thread(this);
        
        this.setVisible(true);
    }

    public void createClient() {
        try {
            myClientSocket = new Socket(InetAddress.getByName(ipField.getText()), Integer.parseInt(portField.getText()));
            inputStream = myClientSocket.getInputStream();
            outputStream = myClientSocket.getOutputStream();   
        }catch (UnknownHostException UHE) {
            System.out.println("Host not found");
        } catch (IOException IO) {
            System.exit(1);
        }
        connectionFlag=true;
        connectButton.setForeground(Color.GREEN);
        clientThread.start();
    }
    
//    not used yet
    public void disconnectClient() { 
    	connectionFlag=false; 
    }
//

    public String getIP() {
        return ipField.getText();
    }

    public int getPort() {
        return Integer.parseInt(portField.getText());
    }

    class connectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	if(connectionFlag == false) {
        		createClient();
        	}
        	else {
        		System.out.println("Already connected: disconnect first");
        	}
        	
        }
    }

    class sendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.out.println(clientThread.getState());
        	sendMessage();
        	
        }
    }
    
    public void sendMessage() {
    	conversationArea.setEditable(true);
    	out = new PrintWriter(outputStream, true);
    	out.println(messageField.getText());
        conversationArea.append("Ich : "+messageField.getText() +"\n");
        messageField.setText("");
        conversationArea.setEditable(false);
    }
    
    public void refreshArea() {
    	System.out.println("Refresh-Client");
    	String message;
    	try {
			if((message = in.readLine()) != null) {
				conversationArea.append("Server: " + message + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void run() {
		in = new BufferedReader(new InputStreamReader(inputStream));
		
		while(connectionFlag) {
			System.out.println(clientThread.getState());
			refreshArea();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==31)
			sendMessage();
		else
			System.out.println(e.getKeyCode());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
