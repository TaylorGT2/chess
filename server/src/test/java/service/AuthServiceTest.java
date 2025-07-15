package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    static final AuthService SERVICE = new AuthService(new MemoryAuthDataAccess());

    @Test
    void createAuth() throws ResponseException {

        var user = new AuthData("c", "b");
        user = SERVICE.createAuth(user);

        var users = SERVICE.listUsers();
        assertEquals(1, users.size());

        assertTrue(users.contains(user));



    }

    @Test
    void createAuthbad() throws ResponseException {

        var user = new AuthData("", "");

        assertThrows(ResponseException.class, () -> {
            SERVICE.createAuth(null);
        });




    }



    @Test
    void getAuth()throws ResponseException{

        var user = new AuthData("c", "b");
        user = SERVICE.createAuth(user);

        assertThrows(ResponseException.class, () -> {
            SERVICE.getAuth("c");
        });


    }
    @Test
    void getAuthGood()throws ResponseException{

        var user = new AuthData("c", "b");
        user = SERVICE.createAuth(user);

        assertEquals(SERVICE.getAuth(user.authToken()),user);


    }


    @Test
    void listUsers() throws ResponseException{

        var user = new AuthData("c", "b");
        user = SERVICE.createAuth(user);
        assertEquals(SERVICE.listUsers().size(),1);
    }


    @Test
    void listUsersbad() throws ResponseException{



        assertThrows(ResponseException.class, () -> {
            SERVICE.getAuth("c");
        });
    }




    @Test
    void deleteAllUsers() throws ResponseException{
        var user = new AuthData("c", "b");
        user = SERVICE.createAuth(user);
        SERVICE.deleteAllUsers();
        assertEquals(SERVICE.listUsers().size(),0);
    }

    @BeforeEach
    void deleteScrub() throws ResponseException{

        SERVICE.deleteAllUsers();

    }




    @Test
    void deleteAuth() throws ResponseException {
        var user = new AuthData("c","b");
        user = SERVICE.createAuth(user);
        SERVICE.deleteAuth(user.authToken());
        assertEquals(SERVICE.listUsers().size(),0);
    }
    @Test
    void deleteAuthbad() throws ResponseException{



        assertThrows(ResponseException.class, () -> {
            SERVICE.getAuth("c");
        });
    }



}