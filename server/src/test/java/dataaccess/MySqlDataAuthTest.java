package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;


import exception.ResponseException;
import model.AuthData;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import exception.ResponseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDataAuthTest {


    private AuthDAO getDataAccess(Class<? extends AuthDAO> databaseClass) throws ResponseException {
        AuthDAO db;
        if (databaseClass.equals(MySqlDataAuth.class)) {
            db = new MySqlDataAuth();
        } else {
            db = new MemoryAuthDataAccess();
        }
        db.clear();
        return db;
    }



    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAuth.class, MemoryAuthDataAccess.class})
    void createAuth(Class<? extends AuthDAO> dbClass) throws ResponseException {
        AuthDAO dataAccess = getDataAccess(dbClass);

        var user = new AuthData("a", "b");
        assertDoesNotThrow(() -> dataAccess.createAuth(user));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAuth.class})
    void createAuthBad(Class<? extends AuthDAO> dbClass) throws ResponseException {
        AuthDAO dataAccess = getDataAccess(dbClass);

        var user = new AuthData("a", null);
        //assertThrows(dataAccess.createAuth(user));
        assertThrows(NullPointerException.class, () -> {
            dataAccess.createAuth(null);
        });
    }







    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAuth.class})
    void logout(Class<? extends AuthDAO> dbClass) throws ResponseException {
        AuthDAO dataAccess = getDataAccess(dbClass);

        var user = new AuthData("a", "b");
        dataAccess.createAuth(user);
        var user2 = new AuthData("aa", "bb");
        AuthData d = dataAccess.createAuth(user2);

        dataAccess.deleteAuth(d.authToken());

        var actual = dataAccess.listUsers();
        assertEquals(1, actual.size());



        //assertDoesNotThrow(() -> dataAccess.createAuth(user));
    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAuth.class})
    void logoutBad(Class<? extends AuthDAO> dbClass) throws ResponseException {
        AuthDAO dataAccess = getDataAccess(dbClass);

        var user = new AuthData("a", null);
        //assertThrows(dataAccess.createAuth(user));
        assertThrows(ResponseException.class, () -> {
            dataAccess.deleteAuth(null);
        });
    }







}