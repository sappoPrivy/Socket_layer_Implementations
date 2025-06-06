import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcHTTPAsk {
    public static void main( String[] args) {
        // Port to connect to
        int serverPort = Integer.parseInt(args[0]);

        try {
            // Listen to incoming TCP connections
            ServerSocket welcomeSocket = new ServerSocket(serverPort);
            
            while (true) {
                // Creating socket for TCP connection
                Socket connectionSocket = welcomeSocket.accept();

                // Runnable object
                MyRunnable r = new MyRunnable(connectionSocket);
                
                // Thread object
                Thread t = new Thread(r);

                // Calls run method
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

