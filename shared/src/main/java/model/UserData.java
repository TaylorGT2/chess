package model;

import com.google.gson.*;

//public class UserData {
//    String username;
//    String password;
//    String email;
//
//    private UserData(String username, String password, String email){
//        this.email=email;
//        this.password=password;
//        this.username=username;
//    }
//
//
//}


public record UserData(String username, String password, String email) {


//    public UserData getuser(String username){
//
//    }
    public String toString() {
        return new Gson().toJson(this);
    }



}
