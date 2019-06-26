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
        ServerSocket serverSocket  = new ServerSocket(8867);
        Socket clientSocket;
        while (true){
            clientSocket = serverSocket.accept();
            System.out.println("client connected");
            DataInputStream serverDataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream serverDataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            String message = serverDataInputStream.readUTF();
            System.out.println(message);
            Thread thread = new Thread();
            String[] parrams = message.split(":");
            Person person = null;
            if (parrams[0].equals("userChecker")){
                String result;
                if (position.containsKey(parrams[1])){
                    result = "repeated";
                }
                else {
                    result = "unique";
                }
                serverDataOutputStream.writeUTF(result);
                System.out.println(result);
            }
            if (parrams[0].equals("signIn")){
                if (position.containsKey(parrams[1])) {
                    person = people.get(position.get(parrams[1]));
                    if (person.getPassword().equals(parrams[2])){
                        thread = activeClient.get(position.get(parrams[1]));
                        thread.start();
                        person.setLoggedIn(true);
                    }
                    else {
                        //wrong password
                    }
                }
                else {
                    //error
                }
            }
            else if(parrams[0].equals("signUp")){
                if (position.containsKey(parrams[1])){
                    while (true){
                        //send error:repeated username
                    }
                }
                else {
                    thread = new ClientHandler(clientSocket, parrams[1], serverDataInputStream, serverDataOutputStream);
                    Person person1 = new Person(parrams[1],parrams[2]);
                    people.add(person1);
                    thread.start();
                }

            }
        }
    }
    public static String codeGenerator(){
        return "";
    }
}
