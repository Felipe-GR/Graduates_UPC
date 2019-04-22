package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.Post;
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

@RestController
public class ViewPostController {

    @Autowired
    PostService postService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getpost")
    public ResponseEntity<Object> viewPost(@RequestBody String jsonRequestPost, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestPost);
        System.err.println(obj);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject postRequestObj = obj.getJSONObject("postRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String postID = postRequestObj.getString("postID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Post post = postService.returnPostByID(Integer.parseInt(postID));
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            // If post is public OR
            // If the requested profile belongs to the user that made the request OR
            // If they are connected
            if (post.getIsPublic() == 1 ||
                    (Integer.toString(post.getUserId())).equals(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(post.getUserId(), Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<Object>(post, HttpStatus.OK);
            }
            // If not return error
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
