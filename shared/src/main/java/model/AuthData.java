
package model;

import com.google.gson.*;
import java.util.UUID;


public record AuthData(String username, String authToken) {


    //    public UserData getuser(String username){
//
//    }
    public String toString() {
        return new Gson().toJson(this);
    }

    //public AuthData(){}

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }




}




