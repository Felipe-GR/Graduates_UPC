package co.dtech.graduates.services;

import co.dtech.graduates.dto.ListUsers;
import co.dtech.graduates.dto.UserBasicInfo;
import co.dtech.graduates.model.PostInterest;
import co.dtech.graduates.model.User;
import co.dtech.graduates.repositories.PostInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostInterestService {

    @Autowired
    PostInterestRepository postInterestRepository;
    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;

    public void addInterest(int postID, int userID) {
        // Check if user already is interested to this post
        if (postInterestRepository.findByPostIdAndUserId(postID, userID) != null) {
            return;
        }
        // Save interest
        PostInterest postInterest = new PostInterest();
        postInterest.setPostId(postID);
        postInterest.setUserId(userID);
        postInterestRepository.save(postInterest);
    }

    public void deleteInterest(int postID, int userID) {
        PostInterest postInterest = postInterestRepository.findByPostIdAndUserId(postID, userID);
        if (postInterest != null) {
            postInterestRepository.delete(postInterest);
        }
    }

    public int checkIfInterested(int postID, int userID) {
        // Check if user already is interested to this post
        if (postInterestRepository.findByPostIdAndUserId(postID, userID) != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getInterestsNumber(int postID) {
        return postInterestRepository.countAllByPostId(postID);
    }

    public ListUsers getInterestedUsersInfo(int postID) {
        List<PostInterest> interests = postInterestRepository.findAllByPostId(postID);
        ArrayList<UserBasicInfo> userList = new ArrayList<UserBasicInfo>();
        for (PostInterest element : interests) {
            int targetUserID = element.getUserId();
            User targetUser = userService.returnUserByID(targetUserID);
            UserBasicInfo targetUserInfo = new UserBasicInfo();
            targetUserInfo.id = Integer.toString(targetUserID);
            targetUserInfo.name = targetUser.getName();
            targetUserInfo.surname = targetUser.getSurname();
            targetUserInfo.image = imageStorageService.getImage(targetUser.getProfilePicture());
            userList.add(targetUserInfo);
        }
        String totalResults = Integer.toString(userList.size());
        ListUsers results = new ListUsers();
        results.list = userList;
        results.numberOfResults = totalResults;
        return results;
    }

    public List<PostInterest> getAllUserInterests(int userID) {
        return postInterestRepository.findAllByUserId(userID);
    }
}
