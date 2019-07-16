import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

class ClientHandler extends Thread {
    private static Socket clientSocket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static byte[] picture;
    static int numberOfActiveClients = 0;
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
                System.out.println(" + Message((from Android)) " + message);
                String[] parrams = message.split(":");
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
                        classList(parrams[1]);
                        break;
                    }
                    case "homeworkList": {
                        homeworkList(parrams[1], parrams[2]);
                        break;
                    }
                    case "studentList": {
                        studentList(parrams[1]);
                        break;
                    }
                    case "teacherList": {
                        teacherList(parrams[1]);
                        break;
                    }
                    case "homeworkProfile": {
                        homeworkProfile(parrams[1]);
                        break;
                    }
                    case "imageProfile": {
                        imageProfile(parrams);
                        break;
                    }
                    case "imageAssignment": {
                        //imageAssignment(message);
                        break;
                    }
                    case "classSetting": {
                        classSetting(parrams);
                        break;
                    }
                    case "classInfo": {
                        classInfo(parrams);
                        break;
                    }
                    case "personProfile": {
                        personProfile(parrams);
                        break;
                    }
                    case "notification": {
                        notification(parrams);
                        break;
                    }
                    case "studentWork": {
                        studentWork(parrams);
                        break;
                    }
                    case "instructions": {
                        instructions(parrams);
                        break;
                    }
                    case "publicComment": {
                        publicComment(parrams);
                        break;
                    }

                    case "privateComment": {
                        privateComment(parrams);
                        break;
                    }
                    case "imageProvider": {
                        Iterator iterator;
                    }
                    case "addPublicComment": {
                        addPublicComment(parrams);
                        break;
                    }
                    case "addPrivateComment": {
                        addPrivateComment(parrams);
                        break;
                    }
                    case "topics":{
                        topics(parrams);
                        break;
                    }
                    case "addTopic":{
                        addTopic(parrams);
                        break;
                    }
                    case "addStudent":{
                        addStudent(parrams);
                        break;
                    }
                    case "addTeacher":{
                        addTeacher(parrams);
                        break;
                    }
                    case "salam" :
                        parsa(parrams);


                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
            //System.out.println(e);
        }
    }

    private void parsa(String[] parrams) {
        System.out.println("MY TASK >>>>" + parrams[0]);
    }


    //****************************************************************
    public void userChecker(String[] parrams) throws IOException {
        String result = "userChecker:";
        if (!parrams[1].equals("")) {
            result = result.concat(parrams[1] + ":");
            if (Server.position.containsKey(parrams[1])) {
                result = result.concat("repeated");
            } else {
                result = result.concat("unique");
            }
            //System.out.println("In the user checker : " + result);
            outputStream.writeUTF(result);
            outputStream.flush();
            System.out.println("-- SERVER >>> " + result);
        } else {
            result = result.concat("empty:empty");
            outputStream.writeUTF(result);
            outputStream.flush();
            System.out.println(" -- SERVER >>> " + result);
        }
    }

    //*************************************************************
    public void signUp(String[] parrams) throws IOException {
        if (Server.position.containsKey(parrams[1])) {
            outputStream.writeUTF("error:" + parrams[1] + ":repeatedUsername");
            outputStream.flush();
            System.out.println(" -- SERVER >>> " + "error:repeatedUsername");
        } else {
            Person person1 = new Person(parrams[1], parrams[2]);
            Server.people.add(person1);
            Server.position.put(parrams[1], Server.people.size() - 1);
            userToFile(person1);
            // System.out.println("ClientHandler >>> Register success(signUp method)" + parrams[1] + "  " + parrams[2]);
            outputStream.writeUTF("signUp:" + parrams[1] + ":success");
            outputStream.flush();
            System.out.println(" ****result**** " + "signUp:success");
        }
    }

    //**********************************************************************
    public void signIn(String[] parrams) throws IOException {
        Person person;
        //System.out.println("Into signIn method : " + parrams[1]);
        // checking username
        if (Server.position.containsKey(parrams[1])) {
            person = Server.people.get(Server.position.get(parrams[1]));
            if (person.getPassword().equals(parrams[2])) {
                person.setLoggedIn(true);
                outputStream.writeUTF("signIn:" + parrams[1] + ":success");
                outputStream.flush();
                System.out.println("*****TEST****** " + "signIn:" + parrams[1] + "success");
            } else {
                outputStream.writeUTF("signIn:" + parrams[1] + ":error");
                outputStream.flush();
                System.out.println(" -- SERVER >>> " + "signIn:" + parrams[1] + ":error");
            }
        } else {
            outputStream.writeUTF("signIn:" + parrams[1] + ":error");
            outputStream.flush();
            System.out.println(" -- SERVER >>> " + "signIn:" + parrams[1] + ":error");
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
                c.setClassCode(code);
                break;
            }
        }
        Server.classPositions.put(code, Server.classes.size() - 1);
        c.getTAs().add(person);
        try {
            classToFile(c);
            outputStream.writeUTF("createClass:success:" + c.getClassCode());
            outputStream.flush();
            System.out.println(" -- SERVER >>> " + "createClass:success:" + c.getClassCode());
        } catch (IOException e) {
            //  System.out.println("Exception >> " + e);
        }

    }

    //****************************************************************
    public static void joinClass(String[] s) throws IOException {

        //System.out.println("SERVER::" + s[0] + s[1] + s[2]);
        if (!Server.classPositions.containsKey(s[2])) {
            try {
                String s1 = "joinClass:error";
                outputStream.writeUTF(s1);
                System.out.println("SERVER >>> " + s1);
                // System.out.println("//////" + s1);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Person p = Server.people.get(Server.position.get(s[1]));
            Class c = Server.classes.get(Server.classPositions.get(s[2]));
            p.getPersonClasses().add(c);
            c.getStudents().add(p);
            updateUser(p);
            updateClass(c);
            try {
                outputStream.writeUTF("joinClass:success:" + c.getName());
                outputStream.flush();
                System.out.println(" -- SERVER >>> " + "joinClass:success:" + c.getName());
                //  System.out.println("SERVER join class success");
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
        String classCode = s[7];
        String homeworkCode; //Devoting a Unique Code to each Homework to Prevent Name Conflicts
        while (true) {
            String string = Server.codeGenerator();
            if (!Server.homeworkCode.contains(string)) {
                homeworkCode = string;
                break;
            }
        }
        Homework homework = new Homework(name, description, point, date, time, topic, homeworkCode);
        Class c = Server.classes.get(Server.classPositions.get(classCode));
        c.getHomework().add(homework);
        c.getHomework().get(c.getHomework().size() - 1).setHomeworkCode(homeworkCode);

        Server.homeworks.add(homework);
        Server.homeworkPositions.put(homeworkCode, Server.homeworks.size() - 1);

        homeworkToFile(homework);
        outputStream.writeUTF("createHomework:success:" + homeworkCode + ":");
        System.out.println(" -- SERVER >>>" + "createHomework:success:" + homeworkCode + ":");
    }

    //***************************************************************
    public static void classWork(Class c) throws IOException {

        String message = "classWork:";
        for (int i = 0; i < c.getTopics().size(); i++) {
            message = message.concat(c.getTopics().get(i) + "@");
            for (int j = 0; j < c.getHomework().size(); j++) {
                if (c.getHomework().get(i).getTopic().equals(c.getTopics().get(i))) {
                    message = message.concat(c.getName() + "@");
                }
            }
            message = message.concat(":");
        }
        outputStream.writeUTF(message);
        outputStream.flush();
        System.out.println(" -- SERVER >>> " + message);
    }

    //********************************************************

    public static void classList(String username) throws IOException {
        Person p = Server.people.get(Server.position.get(username));
        String result = "classList:" + p.getUsername() + ":";
        for (int i = 0; i < p.getPersonClasses().size(); i++) {
            Class c = p.getPersonClasses().get(i);
            result = result.concat(c.getName() + ":" + c.getDescription() + ":" + c.getClassCode() + ":");
        }
        outputStream.writeUTF(result);
        outputStream.flush();
        System.out.println(" -- SERVER >>> " + result);
    }

    //**********************************************************

    public static void homeworkList(String classCode, String username) throws IOException {
        // System.out.println("In the homeworkList function");
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
            result = result.concat(c.getHomework().get(i).getTitle() + ":" + c.getHomework().get(i).getDate() + ":" + c.getHomework().get(i).getNumberOfComments() + ":" + c.getHomework().get(i).getHomeworkCode() + ":");
        }
        //  System.out.println("CHECK date" + c.getHomework().get(0).getDate());
        // System.out.println("server (homeworkList)>>>>>" + result);
        outputStream.writeUTF(result);
        outputStream.flush();
        System.out.println(" -- SERVER >>>" + result);
    }
    //**********************************************************

    public static void teacherList(String s) throws IOException {
        Class c = Server.classes.get(Server.classPositions.get(s));

        String result = "teacherList:" + c.getClassCode() + ":";

        for (int i = 0; i < c.getTAs().size(); i++) {
            result = result.concat(c.getTAs().get(i).getUsername() + ":imageTest");
        }
        System.out.println("Amir >>>>>> " + c.getTAs().get(0).getUsername());
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //***********************************************************

    public static void studentList(String s) throws IOException {
        Class c = Server.classes.get(Server.classPositions.get(s));

        String result = "studentList:" + c.getClassCode() + ":";

        for (int i = 0; i < c.getStudents().size(); i++) {
            result = result.concat(c.getStudents().get(i).getUsername() + ":" + "imageTest" + ":");
        }
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //***********************************************************

    public void homeworkProfile(String homeworkCode) throws IOException {
        String result = "homeworkProfile:";
        Homework homework = Server.homeworks.get(Server.homeworkPositions.get(homeworkCode));

        result = result.concat(homework.getHomeworkCode() + ":" + homework.getTitle() + ":");
        if (homework.getPublicComments().size() == 0) {
            result = result.concat("noComments");
        } else {
            for (int i = 0; i < homework.getPublicComments().size(); i++) {
                result = result.concat(homework.getPublicComments().get(i) + "@");
            }
        }
        if (homework.getAssignment().equals("   ")) {
            result = result.concat(":noAssignments");
        } else {
            result = result.concat(":" + homework.getAssignment());
        }
        outputStream.writeUTF(result);
        System.out.println(" -- SERVER >> " + result);
        outputStream.flush();
    }
    //*************************************************************

    private void imageAssignment(String message) {
        String[] parrams = message.split(":");

        String image = parrams[1];
    }

    //**************************************************************
    private void imageProfile(String[] parrams) {
        String result = parrams[0];
        result = result.concat(":" + parrams[1] + ":" + parrams[2]);
        //System.out.println("-- SERVER >> " + result);
    }
    //*************************************************************

    private void classSetting(String[] parrams) throws IOException {
        String classCode = parrams[1];
        String title = parrams[2];
        String description = parrams[3];
        String roomNumber = parrams[4];

        Class c = Server.classes.get(Server.classPositions.get(classCode));

        if (!title.equals("noTitle")) {
            c.setName(title);
        }
        if (!description.equals("noDescription")) {
            c.setDescription(description);
        }
        if (!roomNumber.equals("noRoomNumber")) {
            c.setNumber(roomNumber
            );
        }
        updateClass(c);
        outputStream.writeUTF("classSetting:success");
        outputStream.flush();
    }

    //**************************************************************

    private void classInfo(String[] parrams) throws IOException {
        String result = "classInfo:";

        String classCode = parrams[1];
        Class c = Server.classes.get(Server.classPositions.get(classCode));

        result = result.concat(c.getName() + ":" + c.getDescription() + ":" + c.getNumber());
        outputStream.writeUTF(result);
        System.out.println("-- SERVER >> " + result);
        outputStream.flush();
    }

    //*****************************************************************

    private void personProfile(String[] parrams) throws IOException {
        String username = parrams[1];
        Person person = Server.people.get(Server.position.get(username));
        String result = "personProfile:" + person.getUsername() + ":";
        String image = person.getImageProfile();
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //*****************************************************************


    private void notification(String[] parrams) {
        String username = parrams[1];
    }


    //*****************************************************************

    private void instructions(String[] parrams) {

    }

    //*****************************************************************

    private void studentWork(String[] parrams) throws IOException {
        String homeworkCode = parrams[1];
        Homework homework = Server.homeworks.get(Server.homeworkPositions.get(homeworkCode));
        ArrayList<Assignment> assignments = homework.getAssignments();
        String result = "studentWork:" + homework.getHomeworkCode() + ":";
        for (int i = 0; i < assignments.size(); i++) {
            result = result.concat(assignments.get(i).getImage() + ":" + assignments.get(i).getUsername() + ":" + assignments.get(i).getState() + ":" + assignments.get(i).getPoint() + ":");
        }
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //*****************************************************************

    private void privateComment(String[] parrams) throws IOException {
        Homework homework = Server.homeworks.get(Server.homeworkPositions.get(parrams[1])); //homeworks code
        ArrayList<Comment> comments = homework.getPrivateComments();

        String result = "privateComment:" + homework.getHomeworkCode() + ":";

        for (int i = 0; i < comments.size(); i++) {
            result = result.concat(comments.get(i).getSender().getUsername() + ":" + comments.get(i).getComment() + ":");
        }
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }
    //*****************************************************************

    private void publicComment(String[] parrams) throws IOException {
        Homework homework = Server.homeworks.get(Server.homeworkPositions.get(parrams[1])); //homeworks code
        ArrayList<Comment> comments = homework.getPublicComments();

        String result = "publicComment:" + homework.getHomeworkCode() + ":";

        for (int i = 0; i < comments.size(); i++) {
            result = result.concat(comments.get(i).getSender().getUsername() + ":" + comments.get(i).getComment() + ":");
        }
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //*****************************************************************
    private void addPrivateComment(String[] parrams) throws IOException {
        String result = "addPrivateComment:";
        Homework homework = Server.homeworks.get(Server.homeworkPositions.get(parrams[1]));
        Comment comment = new Comment(parrams[3], Server.people.get(Server.position.get(parrams[2])), true);
        homework.getPrivateComments().add(comment);

        result = result.concat(comment.getSender().getUsername() + ":success");
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //***************************************************************
    private void addPublicComment(String[] parrams) throws IOException {
        String result = "addPublicComment:";
        Homework homework = Server.homeworks.get(Server.homeworkPositions.get(parrams[1]));
        Comment comment = new Comment(parrams[3], Server.people.get(Server.position.get(parrams[2])), false);
        homework.getPublicComments().add(comment);

        result = result.concat(comment.getSender().getUsername() + ":success");
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();

    }
    //****************************************************************

    public static void userToFile(Object object) {
        try {
            File file = new File(Server.userPath + Server.numberOfFiles + ".txt") ;
            if(!file.exists()){
                file.createNewFile();
                Server.numberOfFiles++;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            System.out.println("USER SUCCESS !!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //*************************************************************

    public static void classToFile (Object object){
        try {
            File file = new File(Server.classPath + Server.numberOfClasses + ".txt");
            if (file.exists()) {
                file.createNewFile();
                Server.numberOfClasses++;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            System.out.println(" CLASS  SUCCESS !!!!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //*************************************************************

    public static void homeworkToFile (Object object){
        try {
            File file = new File(Server.homeworkPath + Server.numberOfHomework + ".txt");
            if (!file.exists()){
                file.createNewFile();
                Server.numberOfHomework++;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            System.out.println(" HOMEWORK SUCCESS !!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //*************************************************************

    public static void updateUser(Object object) throws IOException {
        Person person = (Person) object;
        int position = Server.position.get(person.getUsername());
        File file = new File(Server.userPath + position + ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(person);
        objectOutputStream.flush();
    }

    //*************************************************************

    public static void updateClass(Object object) throws IOException {
        Class c = (Class) object;
        int position = Server.classPositions.get(c.getClassCode());
        File file = new File(Server.classPath + position + ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(c);
        objectOutputStream.flush();
    }

    //*************************************************************

    public static void updateHomework(Object object) throws IOException {
        Homework homework = (Homework) object;
        int position = Server.homeworkPositions.get(homework.getHomeworkCode());
        File file = new File(Server.homeworkPath + position + ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(homework);
        objectOutputStream.flush();

    }
    //*************************************************************
    private void topics(String[] parrams) throws IOException {
        Class c = Server.classes.get(Server.classPositions.get(parrams[1]));
        String result = "topics:" + c.getClassCode() + ":";
        for (int i = 0; i < c.getTopics().size(); i++) {
            result = result.concat(c.getTopics().get(i) + ":");
        }
        System.out.println(" -- SERVER >> " + result);
        outputStream.writeUTF(result);
        outputStream.flush();
    }
    //*************************************************************
    private void addTopic(String[] parrams) {
        Class c = Server.classes.get(Server.classPositions.get(parrams[1]));
        c.getTopics().add(parrams[2]);
        String result = "success";

    }

    //*************************************************************

    private void addTeacher(String[] parrams) throws IOException {
        Class c = Server.classes.get(Server.classPositions.get(parrams[1]));
        String result = "addTeacher:";
        if (!Server.position.containsKey(parrams[2])){
            result = result.concat(c.getClassCode() + ":" + "error");
        }
        else {
            Person p = Server.people.get(Server.position.get(parrams[2]));
            c.getTAs().add(p);
        }
        outputStream.writeUTF(result);
        outputStream.flush();
    }

    //*************************************************************
    private void addStudent(String[] parrams) throws IOException {
        Class c = Server.classes.get(Server.classPositions.get(parrams[1]));
        String result = "addStudent:";
        if (!Server.position.containsKey(parrams[2])){
            result = result.concat(c.getClassCode() + ":" + "error");
        }
        else {
            Person p = Server.people.get(Server.position.get(parrams[2]));
            c.getStudents().add(p);
        }
        outputStream.writeUTF(result);
        outputStream.flush();
    }

}
