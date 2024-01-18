import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket;
        serverSocket = new ServerSocket(3000);
        boolean running = true;

        while (running){
            Socket socket = serverSocket.accept();
            if (socket.isConnected()){System.out.println("connected " + socket.toString());}

            myUserThread userThread = new myUserThread(socket);
            userThread.start();
        }
        serverSocket.close();
    }

    public static void handleRequest(Socket socket) throws IOException{
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        String request = in.readLine();            
        
        if (request.startsWith("GET /hi")){
            // headers
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            // empty line separator (http protocol)
            out.println();
            // content
            out.println("<h1>hello world<h1>");
        }
    }
}