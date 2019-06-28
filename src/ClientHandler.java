import java.io.*;
import java.net.*;

class ClientHandler extends Thread {

    private Socket clientSocket;
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
            if(params[0].equals("createClass")){
                createClass(params);
            }
            else if(params[0].equals("joinClass")){
                joinClass(params);
            }



        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //************************************************************
    public static void createClass(String []s) {

        Person p = Server.people.get(Server.position.get(s[1]));
        Class c = new Class(p, s[2], s[3], s[4]); //teacher(user):name:description:number
        p.getPersonClasses().add(c);
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
        try {
            outputStream.writeUTF("makeClass:success:" + c.getCode());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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