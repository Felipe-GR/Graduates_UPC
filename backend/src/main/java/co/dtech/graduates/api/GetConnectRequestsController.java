package co.dtech.graduates.api;

import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.UserNetworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.dtech.graduates.dto.ListUsers;
import co.dtech.graduates.dto.UserIdentifiers;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class GetConnectRequestsController {
    @Autowired
    UserNetworkService userNetworkService;

    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getconnectrequests")
    public ResponseEntity<Object> connectRequests(@RequestBody String jsonGetRequests, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonGetRequests);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            ListUsers pendingConnectResults = userNetworkService.getPendingRequests(Integer.parseInt(userIdentifiers.id));

            return new ResponseEntity<Object>(pendingConnectResults, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
