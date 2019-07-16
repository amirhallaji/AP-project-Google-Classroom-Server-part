public class Counter extends Thread{

    @Override
    public void run() {
        while (true){
            for (int i = 0; i < Server.activeClients.size(); i++) {
                System.err.println(Server.activeClients.get(i).toString());
            }
        }
    }
}
