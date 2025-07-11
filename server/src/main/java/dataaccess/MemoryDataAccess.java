package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class MemoryDataAccess implements UserDAO {
    private int nextId = 1;
    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData adduser(UserData UData) throws ResponseException{

        if(UData.password()!=null&&UData.username()!=null&&UData.email()!=null) {

            UData = new UserData(UData.password(), UData.username(), UData.email());
            // Create an Authtoken??
            users.put(UData.username(), UData);
            return UData;
        }
        else{
            throw new ResponseException(400,"Error: bad request");
        }
    }

    @Override
    public Boolean checkMatching(UserData checkUser) throws ResponseException{
        String name = checkUser.password();
        UserData loginUser = getUser(name);
        if(checkUser.username()!=null&&checkUser.password()!=null) {
            if (loginUser != null) {
                if (!Objects.equals(loginUser.username(), checkUser.username())) {
                    return true;
                }
                return false;
            }
            return false;
        }
        else{
            throw new ResponseException(400,"Error: bad request");
        }


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