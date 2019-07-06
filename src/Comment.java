public class Comment {
    private String comment ;
    private Person sender ;
    private boolean isPrivate ;

    public Comment(String comment, Person sender, boolean isPrivate) {
        this.comment = comment;
        this.sender = sender;
        this.isPrivate = isPrivate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


}
