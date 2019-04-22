package co.dtech.graduates.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@IdClass(PostPK.class)
public class Post {
    private int id;
    private String content;
    private Timestamp timestamp;
    private Byte isAdvertisment;
    private Byte isPublic;
    private int userId;
    private String image;
    private Byte hasImage;

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
    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "is_advertisment")
    public Byte getIsAdvertisment() {
        return isAdvertisment;
    }

    public void setIsAdvertisment(Byte isAdvertisment) {
        this.isAdvertisment = isAdvertisment;
    }

    @Basic
    @Column(name = "is_public")
    public Byte getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Byte isPublic) {
        this.isPublic = isPublic;
    }

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Basic
    @Column(name = "has_image")
    public Byte getHasImage() {
        return hasImage;
    }

    public void setHasImage(Byte hasImage) {
        this.hasImage = hasImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id &&
                userId == post.userId &&
                Objects.equals(content, post.content) &&
                Objects.equals(timestamp, post.timestamp) &&
                Objects.equals(isAdvertisment, post.isAdvertisment) &&
                Objects.equals(isPublic, post.isPublic) &&
                Objects.equals(image, post.image) &&
                Objects.equals(hasImage, post.hasImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, timestamp, isAdvertisment, isPublic, userId, image, hasImage);
    }
}
