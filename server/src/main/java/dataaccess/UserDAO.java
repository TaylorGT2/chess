package dataaccess;

import model.UserData;
import model.AuthData;
import exception.ResponseException;
//import model.Pet;

import java.util.Collection;

public interface UserDAO {
    UserData adduser(UserData user) throws ResponseException;
    UserData getUser(String username) throws ResponseException;
    Boolean checkMatching(UserData loginUser);
    void clear() throws ResponseException;
    Collection<UserData> listUsers() throws ResponseException;
//    AuthData createAuth(AuthData auth);
//    AuthData getAuth(int authtoken);
//    void deleteAuth(int authtoken);

}


