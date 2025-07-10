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
}