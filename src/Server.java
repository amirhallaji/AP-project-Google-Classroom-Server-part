import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

class Server {
    static String userPath = "Users\\" ;
    static String classPath = "Classes\\";
    static String homeworkPath = "Homework\\";
    static ArrayList<Person> people = new ArrayList<>();
    static HashMap<String,Integer> position = new HashMap<>(); //<Username,its position>
    static ArrayList<Class>classes = new ArrayList<>(); //all of classes used for generating unique code
    static HashMap<String,Integer>classPositions = new HashMap<>() ; //<The Class Code,its position>
    static ArrayList<Homework> homeworks = new ArrayList<>();
    static HashMap<String,Integer> homeworkPositions = new HashMap<>();// code,topic
    static ArrayList<String> homeworkCode = new ArrayList<>();
    static int numberOfFiles  = 0 , numberOfHomework = 0 , numberOfClasses = 0;
    static String message;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ServerSocket serverSocket  = new ServerSocket(8850);
        Socket clientSocket;

        //loadData();

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

    //**************************************************

    private static void loadData() throws IOException, ClassNotFoundException {
        int numberOfPeople = new File(userPath).listFiles().length;
        int numberOfClasses = new File(classPath).listFiles().length;
        int numberOfHomework = new File(homeworkPath).listFiles().length;

        for (int i = 0; i < numberOfPeople; i++) {
            File file = new File(userPath + i + ".txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Person p = (Person) objectInputStream.readObject();
            people.add(p);
            position.put(p.getUsername(),people.size()-1);
            System.out.println("People: " + p.getUsername());
        }
        for (int i = 0; i < numberOfClasses; i++) {
            File file = new File(classPath + i + ".txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Class c = (Class) objectInputStream.readObject();
            classes.add(c);
           classPositions.put(c.getClassCode(),classes.size()-1);
            System.out.println("Classes: " + c.getClassCode());
        }
        for (int i = 0; i < numberOfHomework; i++) {
            File file = new File(homeworkPath + i + ".txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Homework homework = (Homework) objectInputStream.readObject();
            homeworks.add(homework);
            homeworkPositions.put(homework.getHomeworkCode(),homeworks.size()-1);
            System.out.println("Homeworks: " + homework.getHomeworkCode());
        }
    }

    //****************************************************

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
