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
    AuthData createAuth(AuthData auth) throws ResponseException;
    AuthData getAuth(String authToken) throws ResponseException;
    Collection<AuthData> listUsers() throws ResponseException;
    void deleteAuth(String auth) throws ResponseException;
  //  void deleteAuth(int authtoken);

}