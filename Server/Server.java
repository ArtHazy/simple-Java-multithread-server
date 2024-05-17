package Server;
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
            if (socket.isConnected()) {
                MySocketThread thread = new MySocketThread(socket); 
                thread.start();
            }
        }
        serverSocket.close();
    }


    public static class MySocketThread extends Thread {
        Socket socket; 
        MySocketThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            super.run();
            try {
                //receiveFile(socket);
                sendFile(socket);
                
                socket.close();

            } catch (IOException e) {throw new Error(e);}
        }

        void sendFile(Socket socket) throws IOException{
            System.out.print("enter file name: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String fileName = reader.readLine();
            
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(fileName);
            File sendFile = new File(fileName);
            dos.writeLong(sendFile.length());
            FileInputStream fis = new FileInputStream(sendFile);

            //!
            byte[] buffer = new byte[9];
            while (fis.read(buffer)!=-1){
                os.write(buffer);
            }

            fis.close();
            System.out.println("File was sent");
        }

        void receiveFile(Socket socket) throws IOException{
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String inFileName = "s-received-"+dis.readUTF();
            
            File receivedFile = new File(inFileName);
            Long inFileLength = dis.readLong();
            FileOutputStream fos = new FileOutputStream(receivedFile);

            byte[] buffer = new byte[131072]; 
            System.out.println("file length: "+inFileLength);

            int bytesRead;
            while (( bytesRead = is.read(buffer) )!=-1 ){
                fos.write(buffer, 0, bytesRead);
                System.out.println(receivedFile.length());
            }

            fos.close();
            System.out.println("file received");
        }
    }
}