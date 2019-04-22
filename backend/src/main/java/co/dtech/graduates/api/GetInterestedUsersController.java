package co.dtech.graduates.api;

import co.dtech.graduates.dto.ListUsers;
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
public class GetInterestedUsersController {
    @Autowired
    PostInterestService postInterestService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    PostService postService;

    @PostMapping(path = "/interestedusersinfo")
    public ResponseEntity<Object> interestedUsers(@RequestBody String jsonInterestData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonInterestData);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject interestedUsersObj = obj.getJSONObject("interestedUsers");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = interestedUsersObj.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            int userIDPostOwner = post.getUserId();

            // Check if post belongs to connected user
            // or if the post belongs to the user that clicked interest button
            // or requested by admin
            if (userNetworkService.checkIfConnected(userIDPostOwner, Integer.parseInt(userIdentifiers.id)) ||
                    userIDPostOwner == Integer.parseInt(userIdentifiers.id) ||
                    post.getIsPublic() == 1 ||
                    isAdmin) {
                ListUsers users = postInterestService.getInterestedUsersInfo(Integer.parseInt(postID));
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
