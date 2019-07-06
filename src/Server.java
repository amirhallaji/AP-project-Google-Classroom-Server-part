import java.io.*;
import java.net.*;
import java.util.*;

class Server {
    static String userPath = "Users\\" ;
    static String classPath = "Class\\";

    static ArrayList<ClientHandler> activeClient = new ArrayList<>();
    static ArrayList<Person> people = new ArrayList<>();
    static HashMap<String,Integer> position = new HashMap<>(); //<Username,its position>

    //Related to Classes
    static ArrayList<Class>classes = new ArrayList<>(); //all of classes used for generating unique code
    static HashMap<String,Integer>classPositions = new HashMap<>() ; //<The Class Code,its position>
    //homework
    static ArrayList<Homework> homework = new ArrayList<>();
    static HashMap<String,Integer> homeworkPositions = new HashMap<>();// code,topic
    static ArrayList<String> homeworkCode = new ArrayList<>();
    static int numberOfFiles  = 0;
    //static HashMap<String,Class> code
    static String message;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket  = new ServerSocket(8850);
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
