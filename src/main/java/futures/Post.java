package futures;

public class Post {
    private int userId;
    private int id;
    private String title;
    private String body;

    @Override
    public String toString() {
        return "Post{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public Post(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getAllPostAttributesInStringForm(){
        return "\n\n\nuserId: "+this.getUserId()+"\nid: "+this.getId()+"\ntitle: "+this.getTitle()+"\nbody: "+this.getBody();
    }
}
