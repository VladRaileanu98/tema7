package futures;

public class Comment {
    private int postId;
    private int id;
    private String name;
    private String email;

    public String getEmail() {
        return "\n"+email+"\n";
    }

    @Override
    public String toString() {
        return "Comment{" +
                "postId=" + postId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Comment(int postId, int id, String name, String email) {
        this.postId = postId;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getPostId() {
        return postId;
    }

    public String getName() {
        return name;
    }
}
