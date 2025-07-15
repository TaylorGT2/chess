package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class MemoryDataAccess implements UserDAO {
    private int nextId = 1;
    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData adduser(UserData uData) throws ResponseException{

        if(uData.password()!=null&&uData.username()!=null&&uData.email()!=null) {
            UserData testUser = getUser(uData.username());

            if(users.get(uData.password())==null) {
                uData = new UserData(uData.password(), uData.username(), uData.email());

                users.put(uData.username(), uData);
                return uData;
            }
            else{
                throw new ResponseException(403,"Error: already taken");
            }
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
            throw new ResponseException(401,"Error: unauthorized");
        }
        else{
            throw new ResponseException(400,"Error: bad request");
        }


    }

    private UserData getUser(String name) throws ResponseException {
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