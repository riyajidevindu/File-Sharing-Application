import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
  
	
	
    private String fileName;
    private byte[] fileContentBytes;
    private int fileContentLength;
    private int fileNameLength;
    
    private Socket socket;
    private DataInputStream dataInputStream;
    private int currentClient;

    public ClientHandler(Socket socket, DataInputStream dataInputStream, int currentClient) throws IOException{
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.currentClient = currentClient;
    }
    
    @Override
    public void run() {       
            try {
            	
            	while(true) {
            		System.out.println("New client thread was created and running for user "+ currentClient); 
                	
                    fileNameLength = dataInputStream.readInt();
                    
                    if(fileNameLength > 0) {
                        byte[] fileNameBytes = new byte[fileNameLength];
                        dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                        fileName = new String(fileNameBytes);
                        
                        fileContentLength = dataInputStream.readInt();
                        
                        System.out.println("Client handler " + fileContentLength); 
                        
                        if(fileContentLength > 0) {
                            fileContentBytes = new byte[fileContentLength];
                            dataInputStream.readFully(fileContentBytes, 0, fileContentLength);
                            
                            System.out.println("Data arrived succesfully."); 
                            
                            // If fileId is sent by the client, you can read it here.
                            // fileId = dataInputStream.readInt();
                            
                        }
                    }
            	}
            	
            } catch(IOException error) {
                error.printStackTrace();
            }
            finally {
    			try {
    				socket.close();
    				dataInputStream.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
        }
    
    
    // Getter methods to access the fileId, fileName, and fileContentBytes fields
    
 
    public int getFileContentLength() {
    	return fileContentLength;
    }
    
    public int fileNameLength() {
    	System.out.println( "function" + fileContentLength); 
    	return fileNameLength;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileContentBytes() {
        return fileContentBytes;
    }
}
