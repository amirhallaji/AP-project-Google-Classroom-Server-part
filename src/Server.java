import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

class Server {
    static ArrayList<ClientHandler> activeClient = new ArrayList<>();
    static ArrayList<Person> people = new ArrayList<>();
    static ArrayList<Class> classes = new ArrayList<>();
    static HashMap<String,Integer> position = new HashMap<>();
    public static void main(String[] args) throws IOException {
        int numberOfActiveClients = 0;
        ServerSocket serverSocket  = new ServerSocket(8865);
        Socket clientSocket;
        while (true){
            clientSocket = serverSocket.accept();
            DataInputStream serverDataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream serverDataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            ClientHandler clientHandlers = new ClientHandler(clientSocket, "client" +
                    numberOfActiveClients, serverDataInputStream, serverDataOutputStream);
            Thread thread = new Thread(clientHandlers);
            activeClient.add(clientHandlers);
            thread.start();
        }
    }
    public static String codeGenerator(){
        return "";
    }
}
