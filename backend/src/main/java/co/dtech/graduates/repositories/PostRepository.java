package co.dtech.graduates.repositories;

import co.dtech.graduates.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Returns all user's posts
    List<Post> findByUserIdOrderByTimestampAsc(Integer userID);

    // Returns the 3 most recent posts of the user given
    List<Post> findTop3ByUserIdAndIsPublicOrderByTimestampDesc(Integer userID, byte isPublic);

    // Returns all user's posts with pagination
    Page<Post> findByUserIdOrderByTimestampDesc(Pageable pageable, Integer userID);

    // Returns all user's public posts with pagination
    Page<Post> findByUserIdAndIsPublicOrderByTimestampDesc(Pageable pageable, Integer userID, byte isPublic);

    // Returns all posts that belongs to multiple users with pagination order by timestamp
    Page<Post> findByUserIdInOrderByTimestampDesc(Pageable pageable, List userIDs);

    // Returns all posts that belongs to multiple users with pagination
    Page<Post> findByUserIdInAndIsPublic(Pageable pageable, List userIDs, byte isPublic);

    // Returns all public adverts for given time period
    List<Post> findAllByIsAdvertismentAndTimestampGreaterThan(Pageable pageable, byte isAdvert, Timestamp limitTimestamp);

    // Returns all public posts for given time period
    List<Post> findAllByIsPublicAndTimestampGreaterThan(Pageable pageable, byte isPublic, Timestamp limitTimestamp);

    // Returns a post by its own id
    Post findById(Integer postID);

    // Select users posts &
    // Select posts from friends &
    // Select public posts that friends are interested in from other users &
    // Select public posts that friends have commented
    @Query(value = "SELECT DISTINCT p FROM Post p WHERE p.userId IN :userID " +
            "OR (p.id) IN " +
            "(SELECT DISTINCT p.id FROM Post p WHERE p.userId IN :friendsID) " +
            "OR ((p.id) IN " +
            "(SELECT DISTINCT i.postId FROM PostInterest i WHERE i.userId IN :friendsID) AND p.isPublic = 1) " +
            "OR ((p.id) IN " +
            "(SELECT DISTINCT c.postId FROM PostComment c WHERE c.userId IN :friendsID) AND p.isPublic = 1) " +
            "ORDER BY p.timestamp DESC")
    Page<Post> findAllPostsFromFriendsInterestsWithPagination(Pageable pageable, @Param("friendsID") List friendsID, @Param("userID") int userID);

    @Query(value = "SELECT i.userId, p.id FROM Post p, PostInterest i WHERE i.userId IN :friendsID " +
            "AND p.id = i.postId " +
            "AND p.userId NOT IN :userID " +
            "AND p.userId NOT IN :friendsID " +
            "AND p.isPublic = 1 " +
            "ORDER BY p.timestamp DESC ")
    List<Object[]> friendsIDsAndInterestingPostsIDS(@Param("friendsID") List friendsID, @Param("userID") int userID);

    @Query(value = "SELECT c.userId, p.id FROM Post p, PostComment c WHERE c.userId IN :friendsID " +
            "AND p.id = c.postId " +
            "AND p.userId NOT IN :userID " +
            "AND p.userId NOT IN :friendsID " +
            "AND p.isPublic = 1 " +
            "ORDER BY p.timestamp DESC ")
    List<Object[]> friendsIDsAndCommentedPostsIDS(@Param("friendsID") List friendsID, @Param("userID") int userID);

}
