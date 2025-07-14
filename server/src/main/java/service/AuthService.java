package service;

import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import model.UserData;
import model.AuthData;
import dataaccess.AuthDAO;
import exception.ResponseException;

import java.util.Collection;

public class AuthService {






    //private final UserDAO dataAccess = new UserDAO();
    private final AuthDAO dataAccess;

    public AuthService(AuthDAO dataAccess) {
        this.dataAccess = dataAccess;
    }



    public AuthData createAuth(AuthData user) throws ResponseException{

        return dataAccess.createAuth(user);
    }
    public AuthData getAuth(String username) throws ResponseException{
        return dataAccess.getAuth(username);
    }
    public void deleteAllUsers() throws ResponseException{
        dataAccess.clear();
    }

    public Collection<AuthData> listUsers() throws ResponseException {
        return dataAccess.listUsers();
    }
    public void deleteAuth(String auth) throws ResponseException{
        dataAccess.deleteAuth(auth);
    }
}



