package co.dtech.graduates.services;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AdminAuthRequestService {
    @Autowired
    UserService userService;
    public boolean authenticateRequest(UserIdentifiers userIdentifiers, HttpSession session) {
        if (session == null) {
            return false;
        }

        if (userIdentifiers.userToken == null || userIdentifiers.id == null) {
            return false;
        }
        if(session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME) == null){
            return false;
        }
        if (session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME).equals(userIdentifiers.id) &&
                session.getAttribute("userToken").equals(userIdentifiers.userToken)) {

            User user = userService.returnUserByID(Integer.parseInt(userIdentifiers.id));
            if(user.getIsAdmin() == 1) {
                System.err.println("admin is authenticated!");
                return true;
            } else {
                System.err.println("admin is NOT authenticated!");
                return false;
            }
        } else {
            System.err.println("user is NOT authenticated!");
            return false;
        }
    }
}
