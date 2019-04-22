package co.dtech.graduates.dto;

public class Credentials {
    public String email;
    public String password;

    public boolean checkForEmptyCreds(Credentials credentials) {
        if (credentials.email == null || credentials.password == null) {
            return true;
        }
        return false;
    }
}