package dataaccess;

import model.UserData;
import model.AuthData;
import exception.ResponseException;

import java.util.Collection;

public interface UserDAO {
    UserData adduser(UserData user) throws ResponseException;

    Boolean checkMatching(UserData loginUser) throws ResponseException;
    void clear() throws ResponseException;
    Collection<UserData> listUsers() throws ResponseException;


}


