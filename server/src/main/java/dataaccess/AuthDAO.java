package dataaccess;

import model.UserData;
import model.AuthData;
import exception.ResponseException;
//import model.Pet;

import java.util.Collection;

public interface AuthDAO {
    //UserData adduser(UserData user) throws ResponseException;
    //UserData getUser(String username) throws ResponseException;
    void clear() throws ResponseException;
    AuthData createAuth(AuthData auth);
    AuthData getAuth(String authtoken);
  //  void deleteAuth(int authtoken);

}