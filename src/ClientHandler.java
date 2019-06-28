import java.io.*;
import java.net.*;
import java.sql.SQLOutput;

class ClientHandler extends Thread {

    private static Socket clientSocket;
    private String username;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public ClientHandler(Socket clientSocket, String username, DataInputStream inputStream, DataOutputStream outputStream) {
        this.clientSocket = clientSocket;
        this.username = username;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            String message = inputStream.readUTF();
            String [] params = message.split(":") ;
            System.out.println("params 0 >>>>"+params[0]);
            if(params[0].equals("createClass")){
                System.out.println("IN if");
                createClass(params);
            }
            else if(params[0].equals("joinClass")){
                System.out.println("in Else if");
                joinClass(params);
            }



        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //************************************************************
    public static void createClass(String []s) throws IOException {

        System.out.println("IN createClass()");
        Person person = Server.people.get(Server.position.get(s[1]));
        Class c = new Class(person, s[2], s[3], s[4]); //teacher(user):name:description:number
        person.getPersonClasses().add(c);
        String code;
        while (true) {
            code = Server.codeGenerator();
            if (!Server.classCodes.containsKey(code)) {
                c.setCode(code);
                break;
            }
        }
        Server.classCodes.put(code, Server.classes.size());
        Server.classes.add(c);

        Class c1 = new Class(person,"Test","AP","123");
        Class c2 = new Class(person,"Test 2","Fizik","456");

        person.getPersonClasses().add(c1);
        person.getPersonClasses().add(c2);

        sendClassList(person);

        try {
            outputStream.writeUTF("makeClass:success:" + c.getCode());
            outputStream.flush();
            System.out.println("MAke Class Successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //*************************************************************
    public static void sendClassList(Person person) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectOutputStream.writeObject(person.getPersonClasses());
    }
    //****************************************************************
    public static void joinClass(String [] s) {

        if (!Server.classCodes.containsKey(s[1])) {
            try {
                outputStream.writeUTF("joinClass:invalidCode");
                outputStream.flush();
                String message = inputStream.readUTF();
                String [] params = message.split(":");
                if(params[0].equals("retry")){
                    joinClass(params);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Person p = Server.people.get(Server.position.get(s[1]));
            Class c = Server.classes.get(Server.classCodes.get(s[2]));
            c.getStudents().add(p);
            try {
                outputStream.writeUTF("joinClass:success");
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    //*********************************************************************


}