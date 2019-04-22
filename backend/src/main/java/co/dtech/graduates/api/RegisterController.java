package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserRegister;
import co.dtech.graduates.model.User;
import co.dtech.graduates.services.ImageStorageService;
import co.dtech.graduates.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class RegisterController {

    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestPart("user") String jsonUser,
                                           @RequestPart("profileImage") MultipartFile profileImage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserRegister userRegister = objectMapper.readValue(jsonUser, UserRegister.class);
            System.err.println(userRegister.email);

            // Check for empty fields
            if (userRegister.checkForEmptyFields(userRegister)) {
                System.out.println("EMPTY FIELDS FOUND");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("EMPTY_FIELDS");
            }

            // Check if email is already stored
            if (userService.emailExist(userRegister.email)) {
                System.out.println("EMAIL EXISTS");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("EMAIL_EXISTS");
            }

            // Check if the passwords are not matching
            if (!userRegister.passwordCheck(userRegister)) {
                System.out.println("PASSWORDS DO NOT MATCH");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("PASSWORDS_NOT_MATCHING");
            }

            String imageName;
            if (profileImage != null) {
                // Store profile image
                imageName = imageStorageService.storeImage(profileImage);
            } else {
                imageName = null;
            }

            // Store new user
            User user = userRegister.transformToUser(userRegister);
            user.setProfilePicture(imageName);
            // Hash the password
            String passwordToHash = user.getPassword();
            String hashedPassword = hashPassword(passwordToHash);
            user.setPassword(hashedPassword);
            userService.storeUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    static String hashPassword(String passwordToHash) {
        String generatedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
            generatedPassword = generatedPassword.substring(0, 40);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }
}
