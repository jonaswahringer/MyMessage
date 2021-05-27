import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionGUI extends JFrame {
    JButton client, server;
    JLabel label;

    public ConnectionGUI() {
        this.setBounds(800, 500, 200, 130);
        this.setLayout(null);
        
//        label = new JLabel("Server or Client?");
//        label.setBounds(150, 0, 40, 20);
        
        client = new JButton("Start as Client");
        client.setBounds(0, 0, 100, 100);
        client.addActionListener(new ClientListener());

        server = new JButton("Start as Server");
        server.setBounds(100, 0, 100, 100);
        server.addActionListener(new ServerListener());

//        this.add(label);
        this.add(client);
        this.add(server);

        this.setVisible(true);
    }
    
    public void dispose() {
    	this.setVisible(false);
    }

    class ClientListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Client();
            dispose();
        }
    }

    class ServerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           new Server();
           dispose();
        }
    }

}
