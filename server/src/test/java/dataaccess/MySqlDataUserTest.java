package dataaccess;

import org.junit.jupiter.api.Test;


import exception.ResponseException;
import model.UserData;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDataUserTest {


    private UserDAO getDataAccess(Class<? extends UserDAO> databaseClass) throws ResponseException {
        UserDAO db;
        if (databaseClass.equals(MySqlDataUser.class)) {
            db = new MySqlDataUser();
        } else {
            db = new MemoryDataAccess();
        }
        db.clear();
        return db;
    }
//
//    @Test
//    void listUsers() {
//    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataUser.class, MemoryDataAccess.class})
    void addUser(Class<? extends UserDAO> dbClass) throws ResponseException {


        UserDAO dataAccess = getDataAccess(dbClass);



        var user = new UserData("a", "b", "c");
        assertDoesNotThrow(() -> dataAccess.adduser(user));
    }
//
//    @Test
//    void adduser() {
//    }
//
//    @Test
//    void verifyUser() {
//    }
//
//    @Test
//    void checkMatching() {
//    }
//
//    @Test
//    void getUser() {
//    }
//
//    @Test
//    void clear() {
//    }
}