import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Client {

	public static void main(String[] args) {

		final File[] fileToSend = new File[1];
		
		JFrame jFrame  = new JFrame("Flie Sending Client");
		jFrame.setSize(450, 450);
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel jTitle = new JLabel("File Sharing Application Client Side");
		jTitle.setFont(new Font("Arial",Font.BOLD,25));
		jTitle.setBorder(new EmptyBorder(20,0,10,0));
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel jFileName = new JLabel("Choose a file to send");
		jFileName.setFont(new Font("Arial",Font.BOLD,20));
		jFileName.setBorder(new EmptyBorder(50,0,0,0));
		jFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jpButton = new JPanel();
		jpButton.setBorder(new EmptyBorder(75,0,10,0));
		
		JButton jbSendButton = new JButton("Send File");
		jbSendButton.setPreferredSize(new Dimension(150,75));
		jbSendButton.setFont(new Font("Arial",Font.BOLD,20));
		
		JButton jbChooseButton = new JButton("Choose File");
		jbChooseButton.setPreferredSize(new Dimension(150,75));
		jbChooseButton.setFont(new Font("Arial",Font.BOLD,20));
		
		jpButton.add(jbSendButton); 
		jpButton.add(jbChooseButton);
		
		jbChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setDialogTitle("Choose a file to send");
				
				if(jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
					fileToSend[0] = jFileChooser.getSelectedFile();
					jFileName.setText("The file you want to send is: "+fileToSend[0].getName());
				}
			}
		});
		
		
		jbSendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fileToSend[0]==null) {
					jFileName.setText("Please Choose a File First.");
				}else {
					FileInputStream fileInputStream;
					try {
						fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
						Socket socket = new Socket("localhost",1234);
						
						DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
						
						String fileName = fileToSend[0].getName();
						byte[] fileNameBytes = fileName.getBytes();
						
						byte[] fileContentBytes = new byte[(int)fileToSend[0].length()];
						fileInputStream.read(fileContentBytes);
						
						dataOutputStream.writeInt(fileNameBytes.length);
						dataOutputStream.write(fileNameBytes);
						
						dataOutputStream.writeInt(fileContentBytes.length);
						dataOutputStream.write(fileContentBytes);
						
					} catch (IOException error) {
						error.printStackTrace();
					}
					
				}
			}
		});
		
		jFrame.add(jTitle);
		jFrame.add(jFileName);
		jFrame.add(jpButton);
		jFrame.setVisible(true);
		
 
	}

}
