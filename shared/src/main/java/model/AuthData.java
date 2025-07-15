
package model;

import com.google.gson.*;
import java.util.UUID;


public record AuthData(String username, String authToken) {



    public String toString() {
        return new Gson().toJson(this);
    }

    public AuthData setAuth(String auth) {
        return new AuthData(this.username, auth);
    }



    public static String generateToken() {
        return UUID.randomUUID().toString();
    }




}




