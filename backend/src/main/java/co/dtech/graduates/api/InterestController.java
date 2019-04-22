package co.dtech.graduates.api;

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
public class InterestController {

    @Autowired
    PostInterestService postInterestService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    PostService postService;
    @Autowired
    NotificationsService notificationsService;

    @PostMapping(path = "/interest")
    public ResponseEntity<Object> interest(@RequestBody String jsonInterestData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonInterestData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject interestObj = obj.getJSONObject("interest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = interestObj.getString("postID");
            String interested = interestObj.getString("isInterested");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            int userIDPostOwner = post.getUserId();

            // Check if the post belongs to the user that clicked interest button OR
            // Check if post belongs to connected user
            if (userIDPostOwner == Integer.parseInt(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    post.getIsPublic() == 1) {
                postInterestService.addInterest(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                // Don't push notification with comments/interests from the user that owns the post
                if (userIDPostOwner != Integer.parseInt(userIdentifiers.id)) {
                    notificationsService.createNewNotification(Integer.parseInt(userIdentifiers.id), userIDPostOwner, Integer.parseInt(postID), 1);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
