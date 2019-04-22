package co.dtech.graduates.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_network", schema = "web_dev_db", catalog = "")
@IdClass(UserNetworkPK.class)
public class UserNetwork {
    private int id;
    private int user1;
    private int user2;
    private Byte isAccepted;

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
    @Column(name = "is_accepted")
    public Byte getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Byte isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNetwork that = (UserNetwork) o;
        return id == that.id &&
                user1 == that.user1 &&
                user2 == that.user2 &&
                Objects.equals(isAccepted, that.isAccepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user1, user2, isAccepted);
    }
}
