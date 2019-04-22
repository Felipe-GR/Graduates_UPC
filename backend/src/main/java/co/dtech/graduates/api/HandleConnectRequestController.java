package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
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
public class HandleConnectRequestController {
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/handleconnectrequest")
    public ResponseEntity<Object> handleConnect(@RequestBody String jsonConnectRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonConnectRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject connectRequestObj = obj.getJSONObject("connectRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userTargetProfileID = connectRequestObj.getString("profileUserID");
            String accepted = connectRequestObj.getString("accepted");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userTargetProfileID));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (accepted.equals("1")) {
                userNetworkService.acceptConnectRequest(Integer.parseInt(userTargetProfileID), Integer.parseInt(userIdentifiers.id));
            } else {
                userNetworkService.declineConnectRequest(Integer.parseInt(userTargetProfileID), Integer.parseInt(userIdentifiers.id));
            }

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
