package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class MemoryDataAccess implements UserDAO {
    private int nextId = 1;
    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData adduser(UserData UData) {

        UData = new UserData(UData.password(), UData.username(), UData.email());
        // Create an Authtoken??
        users.put(UData.username(), UData);
        return UData;
    }

    @Override
    public Boolean checkMatching(UserData checkUser) {
        String name = checkUser.username();
        UserData loginUser = getUser(name);
        if(loginUser!=null) {
            if (!Objects.equals(loginUser.password(), checkUser.password())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public UserData getUser(String name) {
        int i = users.size();
        return users.get(name);
    }

    public void clear(){
        users.clear();
    }

    public Collection<UserData> listUsers() {
        return users.values();
    }


}