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
            DataInputStream serverDataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream serverDataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            String message = serverDataInputStream.readUTF();
            System.out.println(message);

            Thread thread ;
            String[] parrams = message.split(":");
            Person person ;

            if (parrams[0].equals("userChecker")){
                String result;
                if (position.containsKey(parrams[1])){
                    result = "repeated";
                    serverDataOutputStream.writeUTF("checkResult:repeated");
                }
                else {
                    result = "unique";
                    serverDataOutputStream.writeUTF("checkResult:unique");
                }
                serverDataOutputStream.writeUTF(result);
                System.out.println(result);
            }
            if (parrams[0].equals("signIn")){
                if (position.containsKey(parrams[1])) {
                    person = people.get(position.get(parrams[1]));
                    if (person.getPassword().equals(parrams[2])){
                        person.setLoggedIn(true);
                        thread = activeClient.get(position.get(parrams[1]));
                        thread.start();
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
                }
                else {

                    try { //Creating new file for each person when registering
                        File information = new File(parrams[1] + ".txt");
                        information.createNewFile();
                        FileWriter fileWriter = new FileWriter(information);
                        fileWriter.write(parrams[1] + ":" + parrams[2]);
                        fileWriter.flush();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    thread = new ClientHandler(clientSocket, parrams[1], serverDataInputStream, serverDataOutputStream);
                    Person person1 = new Person(parrams[1],parrams[2]);
                    people.add(person1);
                    position.put(parrams[1],people.size()-1);
                    String s1 = "";
                    for (int i = 0; i < testNames.size(); i++) {
                        s1 = s1.concat(testNames.get(i) + ":");
                    }
                    serverDataOutputStream.writeUTF("listOfClasses:"+s1);
                    serverDataOutputStream.flush();
                    System.out.println("Before starting thread" + person1);
                    thread.start();

                }

            }
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
