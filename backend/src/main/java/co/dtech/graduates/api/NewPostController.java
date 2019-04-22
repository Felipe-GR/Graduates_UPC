package co.dtech.graduates.api;

import co.dtech.graduates.dto.NewPostData;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.PostService;
import co.dtech.graduates.services.UserService;
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
public class NewPostController {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/newpost")
    public ResponseEntity<Object> newPost(@RequestBody String jsonNewPost, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonNewPost);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject postObj = obj.getJSONObject("postData");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            NewPostData newPostData = objectMapper.readValue(postObj.toString(), NewPostData.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            postService.createPost(newPostData, user, "");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
