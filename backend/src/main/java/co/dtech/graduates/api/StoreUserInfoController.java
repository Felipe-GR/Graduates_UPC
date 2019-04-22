package co.dtech.graduates.api;

import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.dto.UserInfo;
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
public class StoreUserInfoController {

    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/setuserinfo")
    public ResponseEntity<Object> setUserInfo(@RequestBody String jsonRequestUserInfo, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonRequestUserInfo);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject userInfoRequestObj = obj.getJSONObject("userInfoUpdate");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            UserInfo userInfo = objectMapper.readValue(userInfoRequestObj.toString(), UserInfo.class);;

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            user.setPhoneNumber(userInfo.phoneNumber);
            user.setPublicPhoneNumber((byte) userInfo.isPhonePublic);
            user.setCity(userInfo.city);
            user.setPublicCity((byte) userInfo.isCityPublic);
            user.setProfession(userInfo.profession);
            user.setPublicProfession((byte) userInfo.isProfessionPublic);
            user.setCompany(userInfo.company);
            user.setPublicCompany((byte) userInfo.isCompanyPublic);
            user.setEducation(userInfo.education);
            user.setPublicEducation((byte) userInfo.isEducationPublic);
            userService.storeUser(user);
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
