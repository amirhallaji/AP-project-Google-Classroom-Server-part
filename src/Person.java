import java.io.*;
import java.util.*;

public class Person implements Serializable {
    private String username;
    private String password;
    private String iamgeProfile;
    private boolean isLoggedIn = false;
    private ArrayList<Class> personClasses =new ArrayList<>();
    private ArrayList<Notification> notifications = new ArrayList<>();
    //picture


    public ArrayList<Class> getPersonClasses() {
        return personClasses;
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return username;

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

    public void setIamgeProfile(String iamgeProfile) {
        this.iamgeProfile = iamgeProfile;
    }

    public String getIamgeProfile() {
        return iamgeProfile;
    }

    public void setPersonClasses(ArrayList<Class> personClasses) {
        this.personClasses = personClasses;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
}