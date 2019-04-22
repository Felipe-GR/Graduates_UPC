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
public class PostCommentController {
    @Autowired
    PostCommentService postCommentService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    PostService postService;
    @Autowired
    NotificationsService notificationsService;

    @PostMapping(path = "/postcomment")
    public ResponseEntity<Object> postComment(@RequestBody String jsonCommentData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonCommentData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject commentDataObj = obj.getJSONObject("commentData");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = commentDataObj.getString("postID");
            String comment = commentDataObj.getString("comment");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            int userIDPostOwner = post.getUserId();

            // Check if the post belongs to the user that posted the comment OR
            // Check if post belongs to connected user
            if (userIDPostOwner == Integer.parseInt(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    post.getIsPublic() == 1) {
                postCommentService.addComment(comment, Integer.parseInt(postID), Integer.parseInt(userIdentifiers.id));
                // Don't push notification with comments/interests from the user that owns the post
                if(userIDPostOwner != Integer.parseInt(userIdentifiers.id)) {
                    notificationsService.createNewNotification(Integer.parseInt(userIdentifiers.id), userIDPostOwner, Integer.parseInt(postID), 2);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
