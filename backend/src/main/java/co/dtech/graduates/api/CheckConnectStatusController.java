package co.dtech.graduates.api;

import co.dtech.graduates.dto.ConnectionAttributes;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
import co.dtech.graduates.model.UserNetwork;
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
public class CheckConnectStatusController {

    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    UserService userService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/connectstatus")
    public ResponseEntity<Object> connectStatus(@RequestBody String jsonConnectStatus, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonConnectStatus);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject targetProfileObj = obj.getJSONObject("targetProfile");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String userTargetProfileID = targetProfileObj.getString("profileUserID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User user = userService.returnUserByID(Integer.parseInt(userTargetProfileID));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            UserNetwork userNetwork = userNetworkService.returnConnection(Integer.parseInt(userIdentifiers.id), Integer.parseInt(userTargetProfileID));
            ConnectionAttributes connectionAttributes = new ConnectionAttributes();

            // If they are not friends
            if (userNetwork == null) {
                connectionAttributes.friends = "0";
                connectionAttributes.pending = "0";
            } else {
                connectionAttributes.friends = "1";
                // If they are friends
                if (userNetwork.getIsAccepted() == 1) {
                    connectionAttributes.pending = "0";
                }
                // If there is pending request
                else {
                    connectionAttributes.pending = "1";
                }
            }
            return new ResponseEntity<Object>(connectionAttributes, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
