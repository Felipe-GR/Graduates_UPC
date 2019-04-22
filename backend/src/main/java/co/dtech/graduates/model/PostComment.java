package co.dtech.graduates.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "post_comment", schema = "web_dev_db", catalog = "")
@IdClass(PostCommentPK.class)
public class PostComment {
    private int id;
    private String content;
    private Timestamp commentTimestamp;
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

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "comment_timestamp")
    public Timestamp getCommentTimestamp() {
        return commentTimestamp;
    }

    public void setCommentTimestamp(Timestamp commentTimestamp) {
        this.commentTimestamp = commentTimestamp;
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
        PostComment that = (PostComment) o;
        return id == that.id &&
                postId == that.postId &&
                userId == that.userId &&
                Objects.equals(content, that.content) &&
                Objects.equals(commentTimestamp, that.commentTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, commentTimestamp, postId, userId);
    }
}
