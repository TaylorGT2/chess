package dataaccess;

import model.UserData;
import model.AuthData;
import exception.ResponseException;
//import model.Pet;

import java.util.Collection;

public interface UserDAO {
    UserData adduser(UserData user) throws ResponseException;
    UserData getUser(String username) throws ResponseException;
    void clear() throws ResponseException;
//    AuthData createAuth(AuthData auth);
//    AuthData getAuth(int authtoken);
//    void deleteAuth(int authtoken);

}
//public class UserDAO {
//
//    public createUser(){
//        // creates a new json for the database
//    }
//    public UserData getUser(String username){
//        // returns the json of the user by searching with its name
//    }
//}
