import java.io.*;
import java.net.*;
import java.util.*;

class Server {
    static ArrayList<ClientHandler> activeClient = new ArrayList<>();
    static ArrayList<Person> people = new ArrayList<>();
    static HashMap<String,Integer> position = new HashMap<>(); //<Username,its position>

    //Related to Classes
    static ArrayList<Class>classes = new ArrayList<>(); //all of classes used for generating unique code
    static HashMap<String,Integer>classPositions = new HashMap<>() ; //<The Class Code,its position>
    //
    static ArrayList<Homework> homework = new ArrayList<>();
    static HashMap<String,String> homeworkTopic = new HashMap<>();// code,topic
    //static HashMap<String,Class> code
    static String message;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket  = new ServerSocket(8867);
        Socket clientSocket;

        while (true){
            clientSocket = serverSocket.accept();
            //System.out.println("User connected");
            DataInputStream serverDataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream serverDataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            //Devoting new thread for each Client
            Thread thread = new ClientHandler(clientSocket,serverDataInputStream,serverDataOutputStream);
            thread.start();
        }
    }
    //****************************************
    public static String codeGenerator(){ //method for devoting a unique code for each class
        StringBuilder code = new StringBuilder() ;
        for (int i = 0; i < 5 ; i++) {
            int number = (int)((Math.random()) * 26) + 97;
            char ch = (char) number;
            code.append(ch) ;
        }
        return code.toString();
    }
}
