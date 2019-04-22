package co.dtech.graduates.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "post_applications", schema = "web_dev_db", catalog = "")
@IdClass(PostApplicationsPK.class)
public class PostApplications {
    private int id;
    private int postId;
    private int userId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "post_id")
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostApplications that = (PostApplications) o;
        return id == that.id &&
                postId == that.postId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, userId);
    }
}
