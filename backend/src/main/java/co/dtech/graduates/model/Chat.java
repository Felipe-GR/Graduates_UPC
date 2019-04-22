package co.dtech.graduates.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(ChatPK.class)
public class Chat {
    private int id;
    private int user1;
    private int user2;
    private Byte isActive;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "user_1")
    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    @Id
    @Column(name = "user_2")
    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    @Basic
    @Column(name = "is_active")
    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id == chat.id &&
                user1 == chat.user1 &&
                user2 == chat.user2 &&
                Objects.equals(isActive, chat.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user1, user2, isActive);
    }
}
