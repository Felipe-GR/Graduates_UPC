package co.dtech.graduates.api;

import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.ImageStorageService;
import co.dtech.graduates.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.dtech.graduates.dto.UserBasicInfo;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetUserBasicInfoController {

    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getuserbasicinfo")
    public ResponseEntity<Object> userBasicInfo(@RequestBody String jsonRequestUserInfo, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestUserInfo);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject userInfoRequestObj = obj.getJSONObject("userInfoRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userId = userInfoRequestObj.getString("userIdInfo");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userId));
            if(user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            UserBasicInfo userBasicInfo = new UserBasicInfo();
            userBasicInfo.id = Integer.toString(user.getId());
            userBasicInfo.name = user.getName();
            userBasicInfo.surname = user.getSurname();
            userBasicInfo.image = imageStorageService.getImage(user.getProfilePicture());
            return new ResponseEntity<Object>(userBasicInfo, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
