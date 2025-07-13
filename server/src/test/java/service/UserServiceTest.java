package service;

import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    //@Test
//    void adduser() {
//    }
    static final UserService service = new UserService(new MemoryDataAccess());

    @Test
    void adduser() throws ResponseException {
        var user = new UserData("c", "b", "a");
        user = service.adduser(user);

        var users = service.listUsers();
        assertEquals(1, users.size());
        //
        assertTrue(users.contains(user));
    }


@Test
    void adduserFail() throws ResponseException {
        var user = new UserData("c", "b", "a");
        var user2 = new UserData("c", "b", "a");
        user = service.adduser(user);

        var users = service.listUsers();
        assertEquals(1, users.size());
        //
        assertTrue(users.contains(user));
    }




    @Test
    void getuser() throws ResponseException {
        var user = new UserData("c", "b", "a");
        user = service.adduser(user);
        var user2 = new UserData("c", "b", "a");
        user2 = service.getuser("b");

        var users = service.listUsers();
        assertEquals(user, user2);
        //
        //assertTrue(users.contains(user));
    }

    @Test
    void clear() throws ResponseException {
        //var user = new GameData("c","b");
        //var user = service.adduser("test");
        var user = new UserData("c", "b", "a");
        var user2 = new UserData("c", "b", "a");
        user = service.adduser(user);
        service.deleteAllUsers();
        assertEquals(service.listUsers().size(),0);
    }
    @Test
    void listUsers() throws ResponseException {
        var user = new UserData("c", "b", "a");

        user = service.adduser(user);

        assertEquals(service.listUsers().size(),1);
    }



    @Test
    void login() throws ResponseException {
        var user = new UserData("c", "b", "a");

        user = service.adduser(user);

        assertEquals(service.login(user),true);
    }







}