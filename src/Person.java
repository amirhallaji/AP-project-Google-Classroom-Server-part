import java.util.*;

public class Person{
    private String username;
    private String password;
    private boolean isLoggedIn = false;
    private ArrayList<Class> personClasses =new ArrayList<>();
    //picture


    public ArrayList<Class> getPersonClasses() {
        return personClasses;
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
        Class c = new Class(this,"Test","test","123");
        personClasses.add(c);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }


}