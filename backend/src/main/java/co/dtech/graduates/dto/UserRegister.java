package co.dtech.graduates.dto;

import co.dtech.graduates.model.User;

public class UserRegister {

    public String email;
    public String password_1;
    public String password_2;
    public String firstName;
    public String lastName;
    public String phoneNumber;

    public boolean passwordCheck(UserRegister userRegister) {
        if (userRegister.password_1.equals(userRegister.password_2)) {
            return true;
        }
        return false;
    }

    public User transformToUser(UserRegister userRegister) {
        User user = new User();
        user.setEmail(userRegister.email);
        user.setPassword(userRegister.password_1);
        user.setName(userRegister.firstName);
        user.setSurname(userRegister.lastName);
        user.setPhoneNumber(userRegister.phoneNumber);
        user.setPublicPhoneNumber((byte) 0);
        user.setPublicCity((byte) 0);
        user.setPublicCompany((byte) 0);
        user.setPublicEducation((byte) 0);
        user.setPublicProfession((byte) 0);
        user.setIsAdmin((byte) 0);
        // So that the db doesn't contain null values
        user.setCity("");
        user.setProfession("");
        user.setEducation("");
        user.setCompany("");
        return user;
    }

    // Returns true if empty fields are found
    public boolean checkForEmptyFields(UserRegister userRegister) {
        if (userRegister.email == null || userRegister.password_1 == null || userRegister.password_2 == null ||
                userRegister.firstName == null || userRegister.lastName == null || userRegister.phoneNumber == null) {
            return true;
        }
        return false;
    }
}
