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
public class ChatGetMessagesController {
    @Autowired
    ChatService chatService;
    @Autowired
    AuthRequestService authRequestService;

    @PostMapping(path = "/getchatmessages")
    public ResponseEntity<Object> getChatMessages(@RequestBody String jsonChatRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonChatRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject chatMessageObj = obj.getJSONObject("chatMessageContent");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            Integer chatID = chatMessageObj.getInt("chatID");

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (!chatService.checkChatWithUserID(chatID, Integer.parseInt(userIdentifiers.id))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List list = chatService.getMessagesFromChat(chatID);
            System.out.print(list);
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

