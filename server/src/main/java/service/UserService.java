package service;

import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import model.UserData;
import dataaccess.UserDAO;
import exception.ResponseException;

import java.util.Collection;

public class UserService {






    private final UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }


    public UserData adduser(UserData user) throws ResponseException{

        return dataAccess.adduser(user);
    }

    public void deleteAllUsers() throws ResponseException{
        dataAccess.clear();
    }
    public Collection<UserData> listUsers() throws ResponseException {
        return dataAccess.listUsers();
    }
    public Boolean login(UserData loginUser) throws ResponseException{
        return dataAccess.checkMatching(loginUser);
    }
}