package tcpclient;
import java.io.*;
import java.net.*;

public class TCPClient {

    private static int BUFFER_SIZE = 1024;
    private boolean shutdown;
    private Integer timeout;
    private Integer limit;
    
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown=shutdown;
        this.timeout=timeout;
        this.limit=limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        
        // Create buffers for receiving data through the socket
        byte[] tmpBuffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream receiveBuffer = new ByteArrayOutputStream();

        try {
            // Create socket for the client to connect to server
            Socket clientSocket = new Socket(hostname, port);

            // Send data in the sockets outputstream to server
            clientSocket.getOutputStream().write(toServerBytes, 0, toServerBytes.length);
            
            // Shutdown
            if(shutdown ==  true) clientSocket.shutdownOutput();
            
            // Read server response
            int tmpBufferLength;

            // Configure timeout for socket
            if (timeout != null) clientSocket.setSoTimeout(timeout);
            
            // Receive larger response in buffer iteratively
            while ((tmpBufferLength = clientSocket.getInputStream().read(tmpBuffer)) != -1) {

                // Data limit exceeded
                if (limit != null && receiveBuffer.size() + tmpBufferLength > limit) {
                    receiveBuffer.write(tmpBuffer, 0, limit - receiveBuffer.size());
                    break;
                }
                
                // Continue receiving data
                receiveBuffer.write(tmpBuffer, 0, tmpBufferLength);
            }
            
            // Close connection
            clientSocket.close();
            return receiveBuffer.toByteArray();

        } catch(SocketTimeoutException e){
            System.out.println("Timeout occured");
            return receiveBuffer.toByteArray();
            
         } catch (IOException e) {
            System.out.println("Connection failure occured");
            throw new IOException();
        }
    }
}
