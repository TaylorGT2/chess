package model;

public class AuthData {

    String username;
    String authToken;


    private AuthData(String username, String authToken){

        this.authToken=authToken;
        this.username=username;
    }

}
