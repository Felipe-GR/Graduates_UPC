package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.PostService;
import co.dtech.graduates.services.UserNetworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class GetPostsFromFriendsController {

    @Autowired
    PostService postService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getpostsfromfriends")
    public ResponseEntity<Object> postsFromFriends(@RequestBody String jsonPostsRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonPostsRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject pageRequestObj = obj.getJSONObject("pageRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            int pageNumber = pageRequestObj.getInt("pageNumber");
            int limit = pageRequestObj.getInt("limit");
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Send requested user's profile info
            List friendList = userNetworkService.getConnectedUsersIDsOnly(Integer.parseInt(userIdentifiers.id));

            // If user has friends send all friendly posts
            if (friendList.size() > 0) {
                Page list = postService.getNetworkPostsAndFriendInterestPosts(friendList, Integer.parseInt(userIdentifiers.id), pageNumber, limit);
                return new ResponseEntity<Object>(list, HttpStatus.OK);
            }
            // Else send only his own posts
            else {
                Page list = postService.getUserPosts(Integer.parseInt(userIdentifiers.id), pageNumber, limit);
                return new ResponseEntity<Object>(list, HttpStatus.OK);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
