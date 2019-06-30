import java.io.*;
import java.net.*;
import java.util.*;

class Server {
    static ArrayList<ClientHandler> activeClient = new ArrayList<>();
    static ArrayList<Person> people = new ArrayList<>();
    static HashMap<String,Integer> position = new HashMap<>();
    //Related to Classes
    static ArrayList<Class>classes = new ArrayList<>();
    static ArrayList<String> testNames = new ArrayList<>();
    static HashMap<String,Integer> classCodes = new HashMap();
    static ArrayList<Homework> homework = new ArrayList<>();
    static HashMap<String,String> homeworkTopic = new HashMap<>();    // code,topic
    static String message;

    public static void main(String[] args) throws IOException {

        testNames.add("math");
        testNames.add("123");
        testNames.add("corner");
        testNames.add("108");
        testNames.add("ap");
        testNames.add("400");

        ServerSocket serverSocket  = new ServerSocket(8867);
        Socket clientSocket;

        while (true){
            clientSocket = serverSocket.accept();
            System.out.println("User connected");
            DataInputStream serverDataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream serverDataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            // ***  message = serverDataInputStream.readUTF();
            // ***   ClientHandler.msg = message;
            // ***   System.out.println("Server >>>>>>>>>"+message);

            Thread thread = new ClientHandler(clientSocket,serverDataInputStream,serverDataOutputStream);
            thread.start();

        }
    }
    public static String codeGenerator(){
        StringBuilder code = new StringBuilder() ;
        for (int i = 0; i < 5 ; i++) {
            char ch = (char) (((int)(Math.random())*26) + 97);
            code.append(ch) ;
        }
        return code.toString();
    }
}
