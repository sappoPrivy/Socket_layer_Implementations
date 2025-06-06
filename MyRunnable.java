// Author @Tenzin Sangpo Choedon

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import tcpclient.TCPClient;

public class MyRunnable implements Runnable{
    Socket connectionSocket;
    
    public MyRunnable(Socket connectionSocket){
        this.connectionSocket = connectionSocket;
    }

    public void run(){
        int BUFFER_SIZE = 1024;
        String hostname = null;
        Integer port = null;
        boolean shutdown = false;
        Integer limit = null;
        Integer timeout = null;
        String msgServer = "";

        try {
            // Pre-allocate buffer for receiving bytes from client
            byte[] fromClientBytes = new byte[BUFFER_SIZE];
            ByteArrayOutputStream fromClientBuffer = new ByteArrayOutputStream();
            int fromClientBytesLength;
            
            // Read client request
            while((fromClientBytesLength = connectionSocket.getInputStream().read(fromClientBytes)) != -1){
                fromClientBuffer.write(fromClientBytes, 0, fromClientBytesLength);
                String tmpClientBuffer = new String(fromClientBytes);
                if(tmpClientBuffer.contains("HTTP/1.1")){
                    break;
                }
            }
            
            String output = new String(fromClientBuffer.toByteArray(), StandardCharsets.UTF_8);
            System.out.println("-----Client request-----\n" + output);

            // Split up client request
            String[] request = output.split("[=&+? \\r\\n]");
            boolean foundHttp = false;

            // Extract parameters for TCPClient
            for (int i = 0; i < request.length; i++) {                    
                switch(request[i]){
                    case "hostname" -> hostname = request[++i];
                    case "port" -> port = Integer.valueOf(request[++i]);
                    case "string" -> msgServer = request[++i];
                    case "shutdown" -> shutdown = Boolean.parseBoolean(request[++i]);
                    case "limit" -> limit = Integer.valueOf(request[++i]);
                    case "timeout" -> timeout = Integer.valueOf(request[++i]);
                    case "HTTP/1.1" -> foundHttp = true;
                }       
            }

            String status = "";

            if(!request[0].equals("GET") || !foundHttp || port == null || hostname == null){
                status = "HTTP/1.1 400 Bad Request\r\n";
                connectionSocket.getOutputStream().write(status.getBytes());
            }
            else if (!request[1].equals("/ask")) {
                status = "HTTP/1.1 404 Not Found\r\n";
                connectionSocket.getOutputStream().write(status.getBytes());
            }
            else{
                status = "HTTP/1.1 200 OK\r\n\r\n";
            
                try{
                    // TCPclient instance
                    TCPClient client = new tcpclient.TCPClient(shutdown, timeout, limit);
                    
                    if(msgServer.length()>0 && msgServer.charAt(msgServer.length()-1) != '\n') msgServer+="\n";
                    
                    // Buffer for message to server
                    byte[] toServerBytes = (msgServer).getBytes(StandardCharsets.UTF_8);

                    // Response from the server
                    byte[] fromServerBytes = client.askServer(hostname, port, toServerBytes);
                    String fromServer = new String(fromServerBytes);

                    // Computing HTTP response
                    String response = status + fromServer;

                    // Buffer for HTTP response
                    byte[] toClientBytes = response.getBytes(StandardCharsets.UTF_8);

                    // Send HTTP response
                    connectionSocket.getOutputStream().write(toClientBytes);

                }catch (IOException e) {
                    status = "HTTP/1.1 400 Bad Request\r\n";
                    connectionSocket.getOutputStream().write(status.getBytes());
                }
                  
            }
            connectionSocket.close();             
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
