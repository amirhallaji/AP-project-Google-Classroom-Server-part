public class Assignment {
    private String username;
    private String text;
    private String image;
    private String state;
    private String point;

    public Assignment(String username, String text, String image) {
        this.username = username;
        this.text = text;
        this.image = image;
        state = "Assigned";
        point = "0";
    }


    public String getUsername() {
        return username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
