package model;

public class UserData {
    String username;
    String password;
    String email;

    private UserData(String username, String password, String email){
        this.email=email;
        this.password=password;
        this.username=username;
    }


}
