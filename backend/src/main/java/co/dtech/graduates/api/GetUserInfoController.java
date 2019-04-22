package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.dto.UserInfo;
import co.dtech.graduates.model.User;
import co.dtech.graduates.services.AdminAuthRequestService;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.UserNetworkService;
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
public class GetUserInfoController {

    @Autowired
    UserService userService;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;

    @PostMapping(path = "/getuserinfo")
    public ResponseEntity<Object> userInfo(@RequestBody String jsonRequestUserInfo, HttpSession session) {
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

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            User user = userService.returnUserByID(Integer.parseInt(userId));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // If the requested profile belongs to the user that made the request OR
            // If the requested info belongs to a friend
            // If admin requested
            if (userId.equals(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(Integer.parseInt(userId), Integer.parseInt(userIdentifiers.id)) ||
                    isAdmin) {
                UserInfo userInfo = userService.getUserInfo(userId);
                return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
            }
            // If not return public info
            else {
                UserInfo userInfo = userService.getPublicUserInfo(userId);
                return new ResponseEntity<Object>(userInfo, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
