import java.util.ArrayList;

public class Homework {
    private String title;
    private String description;
    private String points;
    private String date;
    private String time;
    private String topic;
    private String homeworkCode;
    private Integer numberOfComments;
    private ArrayList<Comment> comments;
    private String assignment ;
    private String imageAssignment ;

    public Homework(String title, String description, String points, String date, String time, String topic, String code) {
        this.title = title;
        this.description = description;
        this.points = points;
        this.date = date;
        this.time = time;
        this.topic = topic;
        this.assignment = "noAssignment";
        this.comments = new ArrayList<>();
        numberOfComments = 0;
    }

    public Integer getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(Integer numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void serDescription(String description) {
        this.description = description;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHomeworkCode() {
        return homeworkCode;
    }

    public void setHomeworkCode(String homeworkCode) {
        this.homeworkCode = homeworkCode;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getImageAssignment() {
        return imageAssignment;
    }

    public void setImageAssignment(String imageAssignment) {
        this.imageAssignment = imageAssignment;
    }
}
