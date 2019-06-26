import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandler implements Runnable{
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
                if (recipientUsername.equals("signin")){
                    String username = arr[1];
                    String password = arr[2];
                    if (Server.people.contains(Server.position.get(username))) {
                        Person p = Server.people.get(Server.position.get(username));
                        if (p.getPassword().equals(password)){
                            p.setLoggedIn(true);
                        }
                    }
                    else {
                        //send error
                    }

                }
                else if (recipientUsername.equals("signup")){
                    String username = arr[1];
                    String password = arr[2];
                    if (!Server.people.contains(Server.position.get(username))){
                        Person person = new Person(username,password);
                        person.setLoggedIn(true);
                    }
                    else {
                        //error username
                    }
                }
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