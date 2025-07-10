package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDAO {
    private int nextId = 1;
    final private HashMap<String, AuthData> users = new HashMap<>();

    public AuthData createAuth(AuthData UData) {

        UData = new AuthData(UData.username(), generateToken());
        // Create an Authtoken??
        users.put(UData.authtoken(), UData);
        return UData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }




    public AuthData getAuth(String name) {
        return users.get(name);
    }

    public void clear(){
        users.clear();
    }


}