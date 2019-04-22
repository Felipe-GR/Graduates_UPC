package co.dtech.graduates.api;

import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.UserNetworkService;
import co.dtech.graduates.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.dtech.graduates.dto.ListUsers;
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
public class GetConnectedUsersController {
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getconnectedusers")
    public ResponseEntity<Object> connectedUsers(@RequestBody String jsonGetRequests, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonGetRequests);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject profileNetworkObj = obj.getJSONObject("profileNetwork");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userTargetProfileID = profileNetworkObj.getString("profileUserID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userTargetProfileID));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // If the requested network belongs to the user that made the request OR
            // If the network belongs to a friend
            if (userTargetProfileID.equals(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(Integer.parseInt(userTargetProfileID), Integer.parseInt(userIdentifiers.id))) {
                ListUsers connectedUsers = userNetworkService.getConnectedUsers(Integer.parseInt(userTargetProfileID));
                return new ResponseEntity<Object>(connectedUsers, HttpStatus.OK);
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
