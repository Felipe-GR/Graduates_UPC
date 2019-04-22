package co.dtech.graduates.services;

import co.dtech.graduates.dto.ListUsers;
import co.dtech.graduates.dto.UserBasicInfo;
import co.dtech.graduates.model.PostApplications;
import co.dtech.graduates.model.User;
import co.dtech.graduates.repositories.PostApplicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostApplicationService {
    @Autowired
    PostApplicationsRepository postApplicationsRepository;
    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;

    public void addApply(int postID, int userID) {
        // Check if user already is applied to this post
        if (postApplicationsRepository.findByPostIdAndUserId(postID, userID) != null) {
            return;
        }
        // Save application
        PostApplications postApplication = new PostApplications();
        postApplication.setPostId(postID);
        postApplication.setUserId(userID);
        postApplicationsRepository.save(postApplication);
    }

    public void deleteApply(int postID, int userID) {
        PostApplications postApplication = postApplicationsRepository.findByPostIdAndUserId(postID, userID);
        if (postApplication != null) {
            postApplicationsRepository.delete(postApplication);
        }
    }

    public int checkIfApplied(int postID, int userID) {
        // Check if user already is interested to this post
        if (postApplicationsRepository.findByPostIdAndUserId(postID, userID) != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getApplicationsNumber(int postID) {
        return postApplicationsRepository.countAllByPostId(postID);
    }

    public ListUsers getAppliedUsersInfo(int postID) {
        List<PostApplications> applications = postApplicationsRepository.findAllByPostId(postID);
        ArrayList<UserBasicInfo> userList = new ArrayList<UserBasicInfo>();
        for (PostApplications element : applications) {
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
}
