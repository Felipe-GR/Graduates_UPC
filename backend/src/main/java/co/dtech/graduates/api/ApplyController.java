package co.dtech.graduates.api;

import co.dtech.graduates.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.Post;
import com.linkdin.app.services.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ApplyController {
    @Autowired
    PostApplicationService postApplicationService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    NotificationsService notificationsService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    PostService postService;

    @PostMapping(path = "/apply")
    public ResponseEntity<Object> apply(@RequestBody String jsonApplyData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonApplyData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject applyObj = obj.getJSONObject("apply");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = applyObj.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Only ads posts has the apply feature
            if (post.getIsAdvertisment() != 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            int userIDPostOwner = post.getUserId();

            // Check if the post belongs to the user that clicked interest button OR
            // Check if post belongs to connected user
            if (userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    post.getIsPublic() == 1) {
                postApplicationService.addApply(Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                // Don't push notification with comments/interests from the user that owns the post
                if(userIDPostOwner != Integer.parseInt(userIdentifiers.id)) {
                    notificationsService.createNewNotification(Integer.parseInt(userIdentifiers.id), userIDPostOwner, Integer.parseInt(postID), 3);
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
