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
    void getuser() throws ResponseException {
        var user = new UserData("c", "b", "a");
        user = service.adduser(user);

        var user2 = service.getuser("c");

        var users = service.listUsers();
        assertEquals(user, user2);
        //
        //assertTrue(users.contains(user));
    }





}