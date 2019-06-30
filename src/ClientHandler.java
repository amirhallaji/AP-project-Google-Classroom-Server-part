


import java.io.*;
import java.net.*;

class ClientHandler extends Thread {
    private static Socket clientSocket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    static String message;

    public ClientHandler(Socket clientSocket, DataInputStream inputStream, DataOutputStream outputStream) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                message = inputStream.readUTF();
                System.out.println("message >>>" + message);
                String[] parrams = message.split(":");
                System.out.println(parrams[0]);

                switch (parrams[0]) {
                    case "userChecker": {
                        userChecker(parrams);
                        break;
                    }
                    case "signIn": {
                        signIn(parrams);
                        break;
                    }
                    case "signUp": {
                        signUp(parrams);
                        break;
                    }
                    case "createClass": {
                        createClass(parrams);
                        break;
                    }
                    case "joinClass": {
                        joinClass(parrams);
                        break;
                    }
                    case "classProfile": {
                        showClassProfile(parrams[1]);
                        break;
                    }
                    case "createHomework": {
                        createHomework(parrams);
                        break;
                    }
                    case "classList": {
                        System.out.println("into classList");
                        classList(parrams[1]);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//****************************************************************
    public void userChecker(String[] parrams) throws IOException {
        String result;
        if (Server.position.containsKey(parrams[1])) {
            result = "repeated";
            outputStream.writeUTF("checkResult:repeated");
        } else {
            result = "unique";
            outputStream.writeUTF("checkResult:unique");
        }
        outputStream.writeUTF(result);
        System.out.println(result);
    }

    //*************************************************************
    public void signUp(String[] parrams) throws IOException {
        if (Server.position.containsKey(parrams[1])) {
            outputStream.writeUTF("error:repeatedUsername");
            outputStream.flush();
        } else {
            try { //Creating new file for each person when registering
                File information = new File(parrams[1] + ".txt");
                information.createNewFile();
                FileWriter fileWriter = new FileWriter(information);
                fileWriter.write(parrams[1] + ":" + parrams[2]);
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Person person1 = new Person(parrams[1], parrams[2]);
            Server.people.add(person1);
            Server.position.put(parrams[1], Server.people.size() - 1);
            outputStream.writeUTF("signUp:success");
            outputStream.flush();
        }
    }

    //**********************************************************************
    public void signIn(String[] parrams) throws IOException {
        Person person;
        if (Server.position.containsKey(parrams[1])) {
            person = Server.people.get(Server.position.get(parrams[1]));
            if (person.getPassword().equals(parrams[2])) {
                person.setLoggedIn(true);
                outputStream.writeUTF("signIn:success");
            } else {
                outputStream.writeUTF("error:wrongPassword");
                outputStream.flush();
            }
        } else {
            outputStream.writeUTF("error:wrongUsername");
            outputStream.flush();
        }
    }

    //************************************************************
    public static void createClass(String[] s) throws IOException {

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

        sendClassList(person);

        try {
            outputStream.writeUTF("makeClass:success:" + c.getCode());
//            outputStream.flush();
            System.out.println("Make Class Successful");
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
    public static void joinClass(String[] s) {

        if (!Server.classCodes.containsKey(s[1])) {
            try {
                outputStream.writeUTF("joinClass:invalidCode");
                outputStream.flush();
                String message = inputStream.readUTF();
                String[] params = message.split(":");
                if (params[0].equals("retry")) {
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
    public static void showClassProfile(String code) throws IOException {
        Class c = Server.classes.get(Server.classCodes.get(code));

        String message = inputStream.readUTF();
        if (message.equals("streams")) {
            streams();
        } else if (message.equals("classWork")) {
            classWork(c);
        } else if (message.equals("people")) {
            people(c);
        }


    }

    //******************************************************************
    public static void createHomework(String[] s) throws IOException {
        String name = s[1];
        String description = s[2];
        String point = s[3];
        String date = s[4];
        String time = s[5];
        String topic = s[6];
        String code = s[7];

        Homework homework = new Homework(name, description, point, date, time, topic);

        Class c = Server.classes.get(Server.classCodes.get(code));
        c.getHomework().add(homework);

    }

    //***************************************************************
    public static void streams() {

    }

    //***************************************************************
    public static void classWork(Class c) throws IOException {

        String message = "classWork:";
        for (int i = 0; i < c.getTopic().size(); i++) {
            message = message.concat(c.getTopic().get(i) + "@");
            for (int j = 0; j < c.getHomework().size(); j++) {
                if (c.getHomework().get(i).getTopic().equals(c.getTopic().get(i))) {
                    message = message.concat(c.getName() + "@");
                }
            }
            message = message.concat(":");
        }
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    //**********************************************************
    public static void people(Class c) throws IOException {

        String message = "classPeople:";
        message = message.concat(c.getTeacher().getUsername() + "@");

        for (int i = 0; i < c.getTAs().size(); i++) {
            message = message.concat(c.getTAs().get(i).getUsername() + "@");
        }
        message = message.concat(":");
        for (int i = 0; i < c.getStudents().size(); i++) {
            message = message.concat(c.getStudents().get(i).getUsername() + "@");
        }
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    //********************************************************
    public static void classList(String s) throws IOException {
        Person p = Server.people.get(Server.position.get(s));
        String result = "classList:";
        for (int i = 0; i < p.getPersonClasses().size(); i++) {
            result = result.concat(p.getPersonClasses().get(i).getName() + ":");
        }
        System.out.println("classList >>> " + result);
        outputStream.writeUTF(result);
    }
    //**********************************************************

}
