package co.dtech.graduates.services;

import co.dtech.graduates.dto.NewPostData;
import co.dtech.graduates.model.Post;
import co.dtech.graduates.model.User;
import co.dtech.graduates.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserNetworkService userNetworkService;

    public void createPost(NewPostData newPostData, User user, String imageName) {
        Post post = new Post();
        post.setContent(newPostData.postContent);
        if (newPostData.isAd) {
            post.setIsAdvertisment((byte) 1);
        } else {
            post.setIsAdvertisment((byte) 0);
        }
        if (newPostData.isPublic) {
            post.setIsPublic((byte) 1);
        } else {
            post.setIsPublic((byte) 0);
        }
        Date date = new java.util.Date();
        Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        post.setTimestamp(sqlDate);
        post.setUserId(user.getId());
        if (!imageName.equals("")) {
            post.setHasImage((byte) 1);
            post.setImage(imageName);
        } else {
            post.setHasImage((byte) 0);
            post.setImage("");
        }
        postRepository.save(post);
    }

    public Post returnPostByID(int postID) {
        Post post = postRepository.findById(postID);
        if (post != null) {
            return post;
        }
        return null;
    }

    public List<Post> getAllUserPosts(int userID) {
        return postRepository.findByUserIdOrderByTimestampAsc(userID);
    }

    public Page<Post> getUserPosts(int userID, int pageNumber, int limit) {
        return postRepository.findByUserIdOrderByTimestampDesc(new PageRequest(pageNumber, limit), userID);
    }

    public Page<Post> getUsersPublicPosts(int userID, int pageNumber, int limit) {
        return postRepository.findByUserIdAndIsPublicOrderByTimestampDesc(new PageRequest(pageNumber, limit), userID, (byte) 1);
    }

    public Page<Post> getNetworkPosts(List userIDs, int pageNumber, int limit) {
        return postRepository.findByUserIdInOrderByTimestampDesc(new PageRequest(pageNumber, limit), userIDs);
    }

    public Page getNetworkPostsAndFriendInterestPosts(List friendsIDs, int userID, int pageNumber, int limit) {
        return postRepository.findAllPostsFromFriendsInterestsWithPagination(new PageRequest(pageNumber, limit), friendsIDs, userID);
    }

    public List getInterestingPostsAndFriendsIDs(List friendsIDs, int userID) {
        return postRepository.friendsIDsAndInterestingPostsIDS(friendsIDs, userID);
    }

    public List getCommentedPostsAndFriendsIDs(List friendsIDs, int userID) {
        return postRepository.friendsIDsAndCommentedPostsIDS(friendsIDs, userID);
    }

    public List<Post> getAllPublicPosts() {
        Date date = new java.util.Date();
        Timestamp twoMonthsAgo = new java.sql.Timestamp(date.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(twoMonthsAgo);
        cal.add(Calendar.MONTH, -2);
        twoMonthsAgo.setTime(cal.getTime().getTime());
        return postRepository.findAllByIsPublicAndTimestampGreaterThan(new PageRequest(0, 500), (byte) 1, twoMonthsAgo);
    }

    public List<Post> getAllPublicAds(int userID) {
        Date date = new java.util.Date();
        Timestamp twoMonthsAgo = new java.sql.Timestamp(date.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(twoMonthsAgo);
        cal.add(Calendar.MONTH, -2);
        twoMonthsAgo.setTime(cal.getTime().getTime());
        List<Post> list = postRepository.findAllByIsAdvertismentAndTimestampGreaterThan(new PageRequest(0, 500), (byte) 1, twoMonthsAgo);
        List<Post> returnList = new ArrayList<>();
        for (Post element : list) {
            Integer postOwnerID = element.getUserId();
            if (element.getIsPublic() == 0 && !userNetworkService.checkIfConnected(userID, postOwnerID)) {
                continue;
            }
            returnList.add(element);
        }
        return returnList;
    }
}
