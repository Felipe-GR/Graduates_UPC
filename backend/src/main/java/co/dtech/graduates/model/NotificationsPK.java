package co.dtech.graduates.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class NotificationsPK implements Serializable {
    private int id;
    private int userId;
    private int notifiedByUser;
    private int postId;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_id")
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "notified_by_user")
    @Id
    public int getNotifiedByUser() {
        return notifiedByUser;
    }

    public void setNotifiedByUser(int notifiedByUser) {
        this.notifiedByUser = notifiedByUser;
    }

    @Column(name = "post_id")
    @Id
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationsPK that = (NotificationsPK) o;
        return id == that.id &&
                userId == that.userId &&
                notifiedByUser == that.notifiedByUser &&
                postId == that.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, notifiedByUser, postId);
    }
}
