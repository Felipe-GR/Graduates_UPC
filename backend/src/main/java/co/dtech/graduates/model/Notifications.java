package co.dtech.graduates.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@IdClass(NotificationsPK.class)
public class Notifications {
    private int id;
    private int userId;
    private int notifiedByUser;
    private int postId;
    private Byte interest;
    private Byte comment;
    private Byte jobapply;
    private Timestamp timestamp;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "notified_by_user")
    public int getNotifiedByUser() {
        return notifiedByUser;
    }

    public void setNotifiedByUser(int notifiedByUser) {
        this.notifiedByUser = notifiedByUser;
    }

    @Id
    @Column(name = "post_id")
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "interest")
    public Byte getInterest() {
        return interest;
    }

    public void setInterest(Byte interest) {
        this.interest = interest;
    }

    @Basic
    @Column(name = "comment")
    public Byte getComment() {
        return comment;
    }

    public void setComment(Byte comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "jobapply")
    public Byte getJobapply() {
        return jobapply;
    }

    public void setJobapply(Byte jobapply) {
        this.jobapply = jobapply;
    }

    @Basic
    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notifications that = (Notifications) o;
        return id == that.id &&
                userId == that.userId &&
                notifiedByUser == that.notifiedByUser &&
                postId == that.postId &&
                Objects.equals(interest, that.interest) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(jobapply, that.jobapply) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, notifiedByUser, postId, interest, comment, jobapply, timestamp);
    }
}
