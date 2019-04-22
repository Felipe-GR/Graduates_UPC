package co.dtech.graduates.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserNetworkPK implements Serializable {
    private int id;
    private int user1;
    private int user2;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_1")
    @Id
    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    @Column(name = "user_2")
    @Id
    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNetworkPK that = (UserNetworkPK) o;
        return id == that.id &&
                user1 == that.user1 &&
                user2 == that.user2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user1, user2);
    }
}
