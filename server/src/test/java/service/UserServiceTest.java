package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {


    static final UserService SERVICE_USER = new UserService(new MemoryDataAccess());
    static final AuthService SERVICE_AUTH = new AuthService(new MemoryAuthDataAccess());

    @Test
    void adduser() throws ResponseException {
        var user = new UserData("c", "b", "a");
        user = SERVICE_USER.adduser(user);

        var users = SERVICE_USER.listUsers();
        assertEquals(1, users.size());

        assertTrue(users.contains(user));
    }


@Test
    void adduserFail() throws ResponseException {
        var user = new UserData("c", "b", "a");
        var user2 = new UserData("c", "b", "a");
        user = SERVICE_USER.adduser(user);

        var users = SERVICE_USER.listUsers();
        assertEquals(1, users.size());

        assertTrue(users.contains(user));
    }






    @Test
    void clear() throws ResponseException {

        var user = new UserData("c", "b", "a");
        var user2 = new UserData("c", "b", "a");
        user = SERVICE_USER.adduser(user);
        SERVICE_USER.deleteAllUsers();
        assertEquals(SERVICE_USER.listUsers().size(),0);
    }
    @Test
    void listUsers() throws ResponseException {
        var user = new UserData("c", "b", "a");

        user = SERVICE_USER.adduser(user);

        assertEquals(SERVICE_USER.listUsers().size(),1);
    }



    @Test
    void loginBad() throws ResponseException {
        var user = new UserData("c", "b", "a");
        var authData = new AuthData("c","b");
        SERVICE_AUTH.createAuth(authData);


        assertThrows(ResponseException.class, () -> {
            SERVICE_USER.login(user);
        });
    }

    @Test
    void login() throws ResponseException {
        var user = new UserData("c", "b", "a");
        var user2 = new UserData("b", "c", "a");
        var authData = new AuthData("c","b");
        SERVICE_AUTH.createAuth(authData);

        user = SERVICE_USER.adduser(user);
        user = SERVICE_USER.adduser(user2);
        SERVICE_USER.login(user);

        assertEquals(SERVICE_USER.login(user),true);

    }







}