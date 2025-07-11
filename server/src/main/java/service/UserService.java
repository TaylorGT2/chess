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






    //private final UserDAO dataAccess = new UserDAO();
    private final UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    //public UserService(){};




    //these both need coressponding return statements
    //public RegisterResult register(RegisterRequest registerRequest) {}
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}

    public UserData adduser(UserData user) throws ResponseException{

        return dataAccess.adduser(user);
    }
    public UserData getuser(String username) throws ResponseException{
        return dataAccess.getUser(username);
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