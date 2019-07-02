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
                System.out.println("Message((Android)) >>>>>   " + message);
                String[] parrams = message.split(":");
                switch (parrams[0]) {
                    case "userChecker": {
                        userChecker(parrams);
                        break;
                    }
                    case "signIn": {
                        //System.out.println("Server >>>>>>>>>>>" + "singIn");
                        signIn(parrams);
                        break;
                    }
                    case "signUp": {
                        signUp(parrams);
                        break;
                    }
                    case "createClass": {
                        //System.out.println("##create class from android");
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
                    case "homeworkList": {
                        System.out.println("android :::" + message);
                        homeworkList(parrams[1], parrams[2]);
                        break;
                    }
                    case "people": {
                        people(parrams[1]);
                    }

                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
            //System.out.println(e);
        }
    }

    //****************************************************************
    public void userChecker(String[] parrams) throws IOException {
        String result = "userChecker:";
        if(!parrams[1].equals("")) {
            result = result.concat(parrams[1] + ":");
            if (Server.position.containsKey(parrams[1])) {
                result = result.concat("repeated");
            } else {
                result = result.concat("unique");
            }
            System.out.println("In the user checker : " + result);
            outputStream.writeUTF(result);
            outputStream.flush();
        }
        else {
            result = result.concat("empty:empty");
            outputStream.writeUTF(result);
            outputStream.flush();
        }
    }

    //*************************************************************
    public void signUp(String[] parrams) throws IOException {
        if (Server.position.containsKey(parrams[1])) {
            System.out.println("Into signUp method :" + parrams[1]);
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
            System.out.println("ClientHandler >>> Register success(signUp method)" + parrams[1] + "  " + parrams[2]);
            outputStream.writeUTF("signUp:success");
            outputStream.flush();
        }
    }

    //**********************************************************************
    public void signIn(String[] parrams) throws IOException {
        Person person;
        System.out.println("Into signIn method : " + parrams[1]);
        // checking username
        if (Server.position.containsKey(parrams[1])) {
            person = Server.people.get(Server.position.get(parrams[1]));
            if (person.getPassword().equals(parrams[2])) {
                person.setLoggedIn(true);
                System.out.println("ClientHandle Success sign in >>>(signIn method) " + parrams[1] + "  " + parrams[2]);
                outputStream.writeUTF("signIn:" + parrams[1] + ":success");
                outputStream.flush();
            } else {
                System.out.println("ClientHandler error sign in >>>(signIn method) " + parrams[1] + "  " + parrams[2]);
                outputStream.writeUTF("signIn:" + parrams[1] + ":error");
                outputStream.flush();
            }
        } else {
            outputStream.writeUTF("signIn:" + parrams[1] + ":error");
            outputStream.flush();
        }
    }

    //************************************************************
    public static void createClass(String[] s) throws IOException {
        Person person = Server.people.get(Server.position.get(s[1]));
        Class c = new Class(person, s[2], s[3], s[4]); //teacher(user):name:description:room number
        person.getPersonClasses().add(c); //the creator of the class
        Server.classes.add(c);

        String code;

        while (true) {
            code = Server.codeGenerator();
            if (!Server.classPositions.containsKey(code)) {
                c.setCode(code);
                break;
            }
        }
        Server.classPositions.put(code, Server.classes.size() - 1);
        c.getTAs().add(person);
        System.out.println("our classLists" + Server.classes.toString());
        System.out.println("person arrayList" + Server.people.toString());
        System.out.println("our hashMap for class" + Server.classPositions.toString());
        try {
            outputStream.writeUTF("createClass:success:" + c.getCode());
            outputStream.flush();
            System.out.println("createClass Successful " + c.getCode());
        } catch (IOException e) {
            System.out.println("Exception >> " + e);
        }

    }

    //****************************************************************
    public static void joinClass(String[] s) {

        System.out.println("SERVER::" + s[0] + s[1] + s[2]);
        if (!Server.classPositions.containsKey(s[2])) {
            try {
                String s1 = "joinClass:error";
                outputStream.writeUTF(s1);
                System.out.println("//////" + s1);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Person p = Server.people.get(Server.position.get(s[1]));
            Class c = Server.classes.get(Server.classPositions.get(s[2]));
            c.getStudents().add(p);
            try {
                outputStream.writeUTF("joinClass:success:" + c.getName());
                outputStream.flush();
                System.out.println("SERVER join class success");
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

    }

    //*********************************************************************
    public static void showClassProfile(String code) throws IOException {
        Class c = Server.classes.get(Server.classPositions.get(code));
//        } else if (message.equals("classWork")) {
//            classWork(c);
//
    }

    //******************************************************************
    public static void createHomework(String[] s) throws IOException {
        String name = s[1];
        String description = s[2];
        String point = s[3];
        String topic = s[4];
        String date = s[5];
        String time = s[6];
        String code = s[7];

        Homework homework = new Homework(name, description, point, date, time, topic);
        System.out.println("Before exception " + code);
        System.out.println("Our ClassList of classes" + Server.classes.toString());
        Class c = Server.classes.get(Server.classPositions.get(code));
        c.getHomework().add(homework);
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

    public static void classList(String className) throws IOException {
        Person p = Server.people.get(Server.position.get(className));
        String result = "classList:";
        for (int i = 0; i < p.getPersonClasses().size(); i++) {
            result = result.concat(p.getPersonClasses().get(i).getName() + ":" + p.getPersonClasses().get(i).getDescription() + ":");
        }
        System.out.println("Server:classList >>> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //**********************************************************

    public static void homeworkList(String classCode, String username) throws IOException {
        System.out.println("In the homeworkList function");
        String result = "homeworkList:";
        Person person = Server.people.get(Server.position.get(username));
        //System.out.println("$$$Code: " + classCode);
        Class c = Server.classes.get(Server.classPositions.get(classCode));
        if (c.getTAs().contains(person)) {
            result = result.concat("teacher:");
        } else {
            result = result.concat("student:");
        }

        for (int i = 0; i < c.getHomework().size(); i++) {
            result = result.concat(c.getHomework().get(i).getTitle() + ":" + c.getHomework().get(i).getDate() + ":" + c.getHomework().get(i).getNumberOfComments() + ":");
        }
        System.out.println("CHECK date" + c.getHomework().get(0).getDate());
        System.out.println("server (homeworkList)>>>>>" + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }
    //**********************************************************

    public static void people(String s) throws IOException {
        Class c = Server.classes.get(Server.classPositions.get(s));
        String result = "people:";

        for (int i = 0; i < c.getTAs().size(); i++) {
            result = result.concat(c.getTAs().get(i).getUsername() + "@");
        }

        result = result.concat(":");

        for (int i = 0; i < c.getStudents().size(); i++) {
            result = result.concat(c.getStudents().get(i).getUsername() + "@");
        }

        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //***********************************************************

}
