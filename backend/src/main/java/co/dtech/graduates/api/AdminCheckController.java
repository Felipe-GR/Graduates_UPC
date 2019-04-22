package co.dtech.graduates.api;

import co.dtech.graduates.services.UserService;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class AdminCheckController {
    @Autowired
    UserService userService;

    @PostMapping(path = "/admincheck")
    public ResponseEntity<String> adminCheck(@RequestBody UserIdentifiers userIdentifiers, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (userIdentifiers.userToken == null || userIdentifiers.id == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).equals(userIdentifiers.id) &&
                session.getAttribute("userToken").equals(userIdentifiers.userToken)) {
            System.err.println("user is authenticated!");
            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            if(user.getIsAdmin() == 1) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            System.err.println("user is NOT authenticated!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
