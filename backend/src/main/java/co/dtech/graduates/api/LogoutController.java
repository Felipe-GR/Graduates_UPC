package co.dtech.graduates.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LogoutController {

    @Autowired
    private HttpSession session;

    @PostMapping(path = "/logout")
    public ResponseEntity<String> logout() {
        System.err.println("logout");
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}