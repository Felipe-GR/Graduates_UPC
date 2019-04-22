package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ChatGetActiveChatsController {
    @Autowired
    ChatService chatService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getactivechats")
    public ResponseEntity<Object> getActiveChats(@RequestBody String jsonChatRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonChatRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List list = chatService.getUserActiveChats(Integer.parseInt(userIdentifiers.id));

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
