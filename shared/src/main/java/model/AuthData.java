
package model;

import com.google.gson.*;
import java.util.UUID;


public record AuthData(String username, String authToken) {



    public String toString() {
        return new Gson().toJson(this);
    }



    public static String generateToken() {
        return UUID.randomUUID().toString();
    }




}




