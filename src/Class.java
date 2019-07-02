import java.io.*;
import java.util.*;

class Class implements Serializable {
    private String name;
    private String description;
    private String number;
    private String code;
    private Person teacher;
    private ArrayList<Person> TAs = new ArrayList<>();
    private ArrayList<Person> students = new ArrayList<>();
    private ArrayList<Homework>homework = new ArrayList<>();
    private ArrayList<String>topic = new ArrayList<>();



    public Class(Person p,String name, String description, String number) {
        teacher = p ;
        this.name = name;
        this.description = description;
        this.number = number;
    }

    @Override
    public String toString() {
        return this.code ;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Person> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }

    public ArrayList<Homework> getHomework() {
        return homework;
    }

    public void setHomework(ArrayList<Homework> homework) {
        this.homework = homework;
    }

    public ArrayList<String> getTopic() {
        return topic;
    }

    public void setTopic(ArrayList<String> topic) {
        this.topic = topic;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public ArrayList<Person> getTAs() {
        return TAs;
    }

    public void setTAs(ArrayList<Person> TAs) {
        this.TAs = TAs;
    }

    public void setStudents(ArrayList<Person> students) {
        this.students = students;
    }
}