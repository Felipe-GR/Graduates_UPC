package co.dtech.graduates.repositories;

import co.dtech.graduates.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // TODO add limit to 30-50 results
    List<User> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(String userName, String userSurname);
    List<User> findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String userName, String userSurname);
    List<User> findAllByIsAdmin(byte adminFlag);
    User findByEmail(String userEmail);
    User findById(Integer userID);
}