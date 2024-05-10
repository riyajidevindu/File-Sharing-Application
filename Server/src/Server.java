import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Server {

	static ArrayList<MyFiles> myfiles = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		int fileId = 0;
	    String fileName = "";
	    byte[] fileContentBytes = null ;
	    int fileContentLength = 1;
	    int currentClient = 1;
		
		
		JFrame jFrame = new JFrame("Flie Sending Server");
		jFrame.setSize(400,400);
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
		
		JScrollPane jScrolPane = new JScrollPane(jPanel);
		jScrolPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel jTitle = new JLabel("File Sharing Application Server Side");
		jTitle.setFont(new Font("Arial",Font.BOLD,25));
		jTitle.setBorder(new EmptyBorder(20,0,10,0));
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		jFrame.add(jTitle);
		jFrame.add(jScrolPane);
		jFrame.setVisible(true);
		
		
		System.out.println("File Server Started."); 
		
		ServerSocket serverSocket = new ServerSocket(1234);
		
		while(true) {
			try {
				
				Socket socket = serverSocket.accept();
				
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());				
				
				System.out.println("Assigning new thread for this client"); 
				
				ClientHandler t = new ClientHandler(socket,dataInputStream,currentClient);
				
				t.start();		
				
				currentClient++;
	
				fileName = t.getFileName();
				fileContentBytes = t.getFileContentBytes();
				fileContentLength = t.getFileContentLength();
				
				System.out.println(fileContentLength); 			
					
					if(fileContentLength>0) {
						
						System.out.println(fileContentLength); 
						JPanel jpFileRow = new  JPanel();
						jpFileRow.setLayout(new BoxLayout(jpFileRow,BoxLayout.Y_AXIS));
						
						JLabel jlFileName = new JLabel(fileName);
						jlFileName.setFont(new Font("Arial",Font.BOLD,25));
						jlFileName.setBorder(new EmptyBorder(10,0,10,0));
						
						
						if (getFileNameExtension(fileName).equalsIgnoreCase("txt")) {
							jpFileRow.setName(String.valueOf(fileId));
							jpFileRow.addMouseListener(getMyMouseListener());
							
							jpFileRow.add(jlFileName);
							jPanel.add(jpFileRow);
							jFrame.validate();
						}else {
							jpFileRow.setName(String.valueOf(fileId));
							jpFileRow.addMouseListener(getMyMouseListener());
							
							jpFileRow.add(jlFileName);
							jPanel.add(jpFileRow);
							jFrame.validate();
						}
						
						myfiles.add(new MyFiles(fileId,fileName,fileContentBytes,getFileNameExtension(fileName)));
						
						fileId++;
					}
					
				
				//serverSocket.close();
				
			}catch(IOException error) {
				error.printStackTrace();
			}
		}

	}
	
	public static MouseListener getMyMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JPanel jPanel = (JPanel) e.getSource();
				
				int fileId = Integer.parseInt(jPanel.getName());
				
				for(MyFiles myFile:myfiles) {
					if(myFile.getId() == fileId) {
					
						JFrame jfPreview = createFrame(myFile.getName(),myFile.getData(),myFile.getFileExtension());
						jfPreview.setVisible(true);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
				
			}
			
			
			
		};
		
	}
	
	public static JFrame createFrame(String fileName,byte[] fileData, String fileExtension) {
		
		JFrame jFrame = new JFrame("File Downloader");
		jFrame.setSize(400,400);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
		
		JLabel jlTitle = new JLabel("File Downloader");
		jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		jlTitle.setFont(new Font("Arial",Font.BOLD,25));
		jlTitle.setBorder(new EmptyBorder(20,0,10,0));
		
		JLabel jlPrompt = new JLabel("Are you sure to downlaod "+fileName);
		jlPrompt.setFont(new Font("Arial",Font.BOLD,20));
		jlPrompt.setBorder(new EmptyBorder(20,0,10,0));
		jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton jbYes = new JButton("Yes");
		jbYes.setPreferredSize(new Dimension(150,75));
		jbYes.setFont(new Font("Arial",Font.BOLD,20));
		
		JButton jbNo = new JButton("No");
		jbNo.setPreferredSize(new Dimension(150,75));
		jbNo.setFont(new Font("Arial",Font.BOLD,20));
		
		
		JLabel jlFileContent = new JLabel();
		jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jpButton = new JPanel();
		jpButton.setBorder(new EmptyBorder(20,0,10,0));
		jpButton.add(jbYes);
		jpButton.add(jbNo);
		
		if(fileExtension.equalsIgnoreCase("txt")) {
			jlFileContent.setText("<html>"+ new String(fileData)+ "</html>");
		}else {
			jlFileContent.setIcon(new ImageIcon(fileData));
		}
		
		jbYes.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        JFileChooser fileChooser = new JFileChooser();
		        int result = fileChooser.showSaveDialog(null); // Show the save dialog
		        
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = fileChooser.getSelectedFile();
		            try {
		                FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
		                fileOutputStream.write(fileData);
		                fileOutputStream.close();
		                
		                jFrame.dispose();
		            } catch (IOException error) {
		                error.printStackTrace();
		            }
		        }
		    }
		});

		jbNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jFrame.dispose();				
			}
			
		});
		
		jPanel.add(jlTitle);
		jPanel.add(jlPrompt);
		jPanel.add(jlFileContent);
		jPanel.add(jpButton);
		
		jFrame.add(jPanel);
		
		return jFrame;
		
	}
	
	public static String getFileNameExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		if(i>0) {
			return fileName.substring(i+1);
		}else {
			return "No extension found.";
		}
	}

}
