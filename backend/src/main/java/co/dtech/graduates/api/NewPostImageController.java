package co.dtech.graduates.api;

import co.dtech.graduates.dto.NewPostData;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.ImageStorageService;
import co.dtech.graduates.services.PostService;
import co.dtech.graduates.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
public class NewPostImageController {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping(path = "/postimage")
    public ResponseEntity<String> newPostImage(@RequestPart("userIdentifiers") String jsonUserIdentifiers,
                                               @RequestPart("postData") String jsonPostData,
                                               @RequestPart("image") MultipartFile image,
                                               HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserIdentifiers userIdentifiers = objectMapper.readValue(jsonUserIdentifiers, UserIdentifiers.class);
            JSONObject postObj = new JSONObject(jsonPostData);
            NewPostData newPostData = objectMapper.readValue(postObj.toString(), NewPostData.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String imageName;
            if (image != null) {
                // Store profile image
                imageName = imageStorageService.storeImage(image);
            } else {
                imageName = null;
            }

            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            postService.createPost(newPostData, user, imageName);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
