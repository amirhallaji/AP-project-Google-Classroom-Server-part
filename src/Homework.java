import java.util.ArrayList;

public class Homework {
    private String title ;
    private String decsription ;
    private String points ;
    private String date ;
    private String time ;
    private String topic ;
    private String code ;
    private Integer numberOfComments ;
    private ArrayList <Comment> comments ;

    public Homework(String title, String decsription, String points, String date,String time, String topic) {
        this.title = title;
        this.decsription = decsription;
        this.points = points;
        this.date = date;
        this.time = time ;
        this.topic = topic;
        this.comments = new ArrayList<>() ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecsription() {
        return decsription;
    }

    public void setDecsription(String decsription) {
        this.decsription = decsription;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
