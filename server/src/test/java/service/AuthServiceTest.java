package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    static final AuthService service = new AuthService(new MemoryAuthDataAccess());

    @Test
    void createAuth() throws ResponseException {

        var user = new AuthData("c", "b");
        user = service.createAuth(user);

        var users = service.listUsers();
        assertEquals(1, users.size());
        //
        assertTrue(users.contains(user));

    }
//
//    @Test
//    void getAuth()throws ResponseException{
//        try {
//            var user = new AuthData("c", "b");
//            user = service.createAuth(user);
//            //var user2 = service.getAuth("c");
//            assertEquals(service.getAuth("c"), user);
//        }
//        catch(ResponseException){
//
//        }
//
//    }


    @Test
    void listUsers() throws ResponseException{
        //createAuth()
        var user = new AuthData("c", "b");
        user = service.createAuth(user);
        assertEquals(service.listUsers().size(),1);
    }

    @Test
    void deleteAllUsers() throws ResponseException{
        var user = new AuthData("c", "b");
        user = service.createAuth(user);
        service.deleteAllUsers();
        assertEquals(service.listUsers().size(),0);
    }

    @Test
    void deleteAuth() throws ResponseException {
        var user = new AuthData("c","b");
        user = service.createAuth(user);
        service.deleteAuth(user.authToken());
        assertEquals(service.listUsers().size(),0);
    }



}