package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.ImageStorageService;
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
public class GetImageController {

    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping(path = "/getimage")
    public ResponseEntity<Object> gendImage(@RequestBody String jsonImage, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonImage);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject imageObj = obj.getJSONObject("image");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            String imageName = imageObj.getString("imageName");
            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String imageBytes = imageStorageService.getImage(imageName);
            String returnJsonImage = new JSONObject()
                    .put("image", imageBytes).toString();
            return new ResponseEntity<>(returnJsonImage, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
