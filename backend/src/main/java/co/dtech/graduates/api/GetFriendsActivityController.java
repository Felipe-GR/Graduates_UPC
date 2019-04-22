package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.PostService;
import co.dtech.graduates.services.UserNetworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class GetFriendsActivityController {
    @Autowired
    PostService postService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getfriendsactivity")
    public ResponseEntity<Object> postsFromFriends(@RequestBody String jsonPostsRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonPostsRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List friendList = userNetworkService.getConnectedUsersIDsOnly(Integer.parseInt(userIdentifiers.id));

            // Return results if only the user has friends
            if (friendList.size() > 0) {
                List interestsList = postService.getInterestingPostsAndFriendsIDs(friendList, Integer.parseInt(userIdentifiers.id));
                List commentsList = postService.getCommentedPostsAndFriendsIDs(friendList, Integer.parseInt(userIdentifiers.id));
                List result[] = {interestsList, commentsList};
                return new ResponseEntity<Object>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>("null", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
