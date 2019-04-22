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

@RestController
public class ChatSendMessageController {
    @Autowired
    ChatService chatService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/sendchatmessage")
    public ResponseEntity<Object> sendChatMessage(@RequestBody String jsonChatRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonChatRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject chatMessageObj = obj.getJSONObject("chatMessageContent");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Integer chatID = chatMessageObj.getInt("chatID");
            String message = chatMessageObj.getString("messageContent");
            String senderID = userIdentifiers.id;

            if (!chatService.checkChatWithUserID(chatID, Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            chatService.sendMessage(chatID, Integer.parseInt(senderID), message);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
