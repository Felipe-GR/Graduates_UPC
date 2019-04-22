package co.dtech.graduates.repositories;

import co.dtech.graduates.model.UserNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNetworkRepository extends JpaRepository<UserNetwork, Long> {
    // Returns all connections of a user
    List<UserNetwork> findByUser1AndIsAcceptedOrUser2AndIsAccepted(int user1, byte isAccepted1, int user2, byte isAccepted2);

    // Returns all the pending incoming connect requests
    List<UserNetwork> findByUser2AndIsAccepted(int user2, byte isAccepted);

    // Returns the connection between two users
    UserNetwork findByUser1AndUser2(int user1, int user2);

    // Returns the connection between two users
    UserNetwork findByUser1AndUser2AndIsAccepted(int user1, int user2, byte isAccepted);
}
