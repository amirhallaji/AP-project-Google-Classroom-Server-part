import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread{
    private Socket clientSocket;
    private String username;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ClientHandler(Socket clientSocket, String username, DataInputStream inputStream, DataOutputStream outputStream) {
        this.clientSocket = clientSocket;
        this.username = username;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        String receivedText;
        while (true) {
            try {
                receivedText = inputStream.readUTF();
                String recipientUsername , messageText;
                String[] arr = receivedText.split(":");
                recipientUsername = arr[0];
                messageText = arr[1];
                for (ClientHandler clientHandlers : Server.activeClient) {
                    if (clientHandlers.equals(recipientUsername)
                            && clientHandlers.username.equals(this.username)) {
                        clientHandlers.outputStream.writeUTF(this.username + " : " + messageText);
                        break;
                    }
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}