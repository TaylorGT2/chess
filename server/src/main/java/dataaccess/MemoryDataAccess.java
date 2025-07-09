package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements UserDAO {
    private int nextId = 1;
    final private HashMap<Integer, UserData> users = new HashMap<>();

    public UserData adduser(UserData UData) {

        UData = new UserData(UData.password(), UData.username(), UData.email());
        // Create an Authtoken??
        //users.put(UData.id(), UData);
        return UData;
    }




    public UserData getUser(String name) {
        return users.get(name);
    }

    public void clear(){
        users.clear();
    }


}