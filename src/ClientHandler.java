import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String receivedText = inputStream.readUTF();
                    System.out.println("The first part");
                    String[] arr = receivedText.split(":");
                    System.out.println("Received TExt : " + receivedText);
                    if (arr[0].equals("classList")) {
                        System.out.println("into class list");
                        outputStream.writeUTF("balalala");
                        Person p = Server.people.get(Server.position.get(arr[1]));
                        int n = p.getPersonClasses().size();
                        for (int i = 0; i < n; i++) {
                            outputStream.writeUTF(p.getPersonClasses().get(i).getName() + "@" + p.getPersonClasses().get(i).getNumber() + ":");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void createClass(String s) {
        String[] params = s.split(":"); //user:classname:description:roomNumber
        Person p = Server.people.get(Server.position.get(params[1]));
        Class c = new Class(p, params[1], params[2], params[3]);
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
        System.out.println("Class " + c.getCode() + "created");
        try {
            outputStream.writeUTF("makeClass:success:" + c.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void joinClass(String s) {
        String[] params = s.split(":");

        if (!Server.classCodes.containsKey(params[2])) {
            System.out.println("Error for invalid code");
            try {
                outputStream.writeUTF("joinClass:invalidCode");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Person p = Server.people.get(Server.position.get(params[1]));
            Class c = Server.classes.get(Server.classCodes.get(params[2]));
            c.getStudents().add(p);
            System.out.println("joined Successfully");
            try {
                outputStream.writeUTF("joinClass:success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}