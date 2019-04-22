package co.dtech.graduates.api;

import co.dtech.graduates.dto.InterestData;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.Post;
import co.dtech.graduates.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetApplicationsDataController {
    @Autowired
    PostApplicationService postApplicationService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    PostService postService;

    @PostMapping(path = "/applicationsdata")
    public ResponseEntity<Object> applicationsNumber(@RequestBody String jsonApplyData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonApplyData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject appliedUsersObj = obj.getJSONObject("appliedUsers");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = appliedUsersObj.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Only ads posts has the apply feature
            if (post.getIsAdvertisment() != 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            int userIDPostOwner = post.getUserId();

            // Check if the post belongs to the user that owns the ad
            // or requested by admin
            InterestData interestData = new InterestData();
            if (userIDPostOwner == Integer.parseInt(userIdentifiers.id) ||
                    isAdmin) {
                interestData.numberOfInterestedUsers = postApplicationService.getApplicationsNumber(Integer.parseInt(postID));
                interestData.isUserInterested = postApplicationService.checkIfApplied(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                return new ResponseEntity<>(interestData, HttpStatus.OK);
            }
            // If is requested by other user return only if this user is already applied
            else if (userIDPostOwner != Integer.parseInt(userIdentifiers.id)) {
                interestData.numberOfInterestedUsers = 0;
                interestData.isUserInterested = postApplicationService.checkIfApplied(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                return new ResponseEntity<>(interestData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
