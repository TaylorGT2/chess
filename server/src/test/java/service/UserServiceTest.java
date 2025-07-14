package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {


    static final UserService serviceUser = new UserService(new MemoryDataAccess());
    static final AuthService serviceAuth = new AuthService(new MemoryAuthDataAccess());

    @Test
    void adduser() throws ResponseException {
        var user = new UserData("c", "b", "a");
        user = serviceUser.adduser(user);

        var users = serviceUser.listUsers();
        assertEquals(1, users.size());

        assertTrue(users.contains(user));
    }


@Test
    void adduserFail() throws ResponseException {
        var user = new UserData("c", "b", "a");
        var user2 = new UserData("c", "b", "a");
        user = serviceUser.adduser(user);

        var users = serviceUser.listUsers();
        assertEquals(1, users.size());

        assertTrue(users.contains(user));
    }






    @Test
    void clear() throws ResponseException {

        var user = new UserData("c", "b", "a");
        var user2 = new UserData("c", "b", "a");
        user = serviceUser.adduser(user);
        serviceUser.deleteAllUsers();
        assertEquals(serviceUser.listUsers().size(),0);
    }
    @Test
    void listUsers() throws ResponseException {
        var user = new UserData("c", "b", "a");

        user = serviceUser.adduser(user);

        assertEquals(serviceUser.listUsers().size(),1);
    }



    @Test
    void loginBad() throws ResponseException {
        var user = new UserData("c", "b", "a");
        var authData = new AuthData("c","b");
        serviceAuth.createAuth(authData);


        assertThrows(ResponseException.class, () -> {
            serviceUser.login(user);
        });
    }

    @Test
    void login() throws ResponseException {
        var user = new UserData("c", "b", "a");
        var user2 = new UserData("b", "c", "a");
        var authData = new AuthData("c","b");
        serviceAuth.createAuth(authData);

        user = serviceUser.adduser(user);
        user = serviceUser.adduser(user2);
        serviceUser.login(user);

        assertEquals(serviceUser.login(user),true);

    }







}