import java.io.IOException;
import java.net.Socket;

public class myUserThread extends Thread {
    Socket userSocket; 
    myUserThread(Socket userSocket){
        this.userSocket = userSocket;
    }

    @Override
    public void run() {
        super.run();
        try {
            Server.handleRequest(userSocket);
            userSocket.close();
            if (userSocket.isClosed()) {
                System.out.println("disconnected "+ userSocket.toString());
            }
        } catch (IOException e) {e.printStackTrace();}
    }
}
