package Client;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("127.0.0.1", 3000);
        System.out.println("Connecting to server");

        //sendFile(socket);
        receiveFile(socket);
        
        socket.close();
        System.out.println("Socket closed");
    }

    static void sendFile(Socket socket) throws IOException {

        System.out.print("Enter file name: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();
        
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF(fileName);
        File sendFile = new File(fileName);
        dos.writeLong(sendFile.length());
        FileInputStream fis = new FileInputStream(sendFile);

        byte[] buffer = new byte[131072];
        while (fis.read(buffer)!=-1){
            os.write(buffer);
        }

        fis.close();
        
        System.out.println("File was sent");
        
    }
    static void receiveFile(Socket socket) throws IOException{

        System.out.println("Awaits reply file...");
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        String inFileName = "c-received-"+dis.readUTF();
        Long inFileLength = dis.readLong();
        File receivedFile = new File(inFileName);

        FileOutputStream fos = new FileOutputStream(receivedFile);

        byte[] buffer = new byte[131072];
        int bytesRead;
        while ( (bytesRead = is.read(buffer))  !=-1 && receivedFile.length()<inFileLength){
            fos.write(buffer,0,bytesRead);
            System.out.println(receivedFile.length());
        }

        fos.close();
        System.out.println("file received");

    }
}