package co.dtech.graduates.api;

import co.dtech.graduates.dto.ProfilePostsPageRequest;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.services.AdminAuthRequestService;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.PostService;
import co.dtech.graduates.services.UserNetworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ProfilePostsController {

    @Autowired
    PostService postService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;
    @Autowired
    UserNetworkService userNetworkService;

    @PostMapping(path = "/getprofileposts")
    public ResponseEntity<Object> profilePosts(@RequestBody String jsonPostsRequest, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonPostsRequest);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject pageRequestObj = obj.getJSONObject("pageRequest");
            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);
            ProfilePostsPageRequest profilePostsPageRequest = objectMapper.readValue(pageRequestObj.toString(), ProfilePostsPageRequest.class);

            // Authenticate user
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            boolean isAdmin = adminAuthRequestService.authenticateRequest(userIdentifiers, session);

            int userID = Integer.parseInt(profilePostsPageRequest.profileUserID);
            int pageNumber = Integer.parseInt(profilePostsPageRequest.pageNumber);
            int limit = Integer.parseInt(profilePostsPageRequest.limit);
            Page page;
            // If the requested profile belongs to the user that made the request
            // If they are connected
            // If admin requested
            if (profilePostsPageRequest.profileUserID.equals(userIdentifiers.id) ||
                    userNetworkService.checkIfConnected(Integer.parseInt(profilePostsPageRequest.profileUserID), Integer.parseInt(userIdentifiers.id)) ||
                    isAdmin) {
                page = postService.getUserPosts(userID, pageNumber, limit);
                return new ResponseEntity<Object>(page, HttpStatus.OK);
            }
            // If not return only the public posts
            else {
                page = postService.getUsersPublicPosts(userID, pageNumber, limit);
            }
            return new ResponseEntity<Object>(page, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
