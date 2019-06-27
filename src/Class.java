import java.util.ArrayList;

class Class {
    private String name;
    private String description;
    private String number;
    private String code;
    private Person teacher;
    private ArrayList<Person> students = new ArrayList<>();

    public Class(Person p,String name, String description, String number) {
        teacher = p ;
        this.name = name;
        this.description = description;
        this.number = number;
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

    public String getNumber() {
        return number;
    }
}