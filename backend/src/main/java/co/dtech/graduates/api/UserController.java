package co.dtech.graduates.api;

import co.dtech.graduates.services.AdminAuthRequestService;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.ImageStorageService;
import co.dtech.graduates.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping(path = "/user")
    public ResponseEntity<Object> user(@RequestBody String jsonProfileRequest, HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonProfileRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject profileObj = obj.getJSONObject("requestProfile");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            int profileUserID = profileObj.getInt("profileUserID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Send requested user's profile info
            User user = userService.returnUserByID(profileUserID);
            if(user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            // If the user profile that requested belongs to admin
            // Return Unauthorized
            if(user.getIsAdmin() == 1) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String jsonUser = new JSONObject()
                    .put("firstName", user.getName())
                    .put("lastName", user.getSurname())
                    .toString();

            String image = imageStorageService.getImage(user.getProfilePicture());
            String responseObject = new JSONObject()
                    .put("user", jsonUser)
                    .put("profileImage", image)
                    .toString();

            return new ResponseEntity<Object>(responseObject, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
