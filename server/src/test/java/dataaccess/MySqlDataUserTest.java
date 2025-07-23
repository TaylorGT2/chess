package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
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

//    @BeforeAll
//    static
//
//    @Test
//    void listUsers() {
//    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataUser.class, MemoryDataAccess.class})
    void addUser(Class<? extends UserDAO> dbClass) throws ResponseException {


        UserDAO dataAccess = getDataAccess(dbClass);


        //dataAccess.adduser(user);
        var user = new UserData("a", "b", "c");
        var user2 = new UserData("aa", "bb", "cc");
        dataAccess.adduser(user);
        assertDoesNotThrow(() -> dataAccess.adduser(user2));
    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataUser.class})
    void adduserBad(Class<? extends UserDAO> dbClass) throws ResponseException {
        UserDAO dataAccess = getDataAccess(dbClass);

        assertThrows(NullPointerException.class, () -> {
            dataAccess.adduser(null);
        });
    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataUser.class, MemoryDataAccess.class})
    void deleteAllUsers(Class<? extends UserDAO> dbClass) throws Exception {
        UserDAO dataAccess = getDataAccess(dbClass);

        dataAccess.adduser(new UserData("a", "b", "c"));
        //dataAccess.addPet(new Pet(0, "sally", PetType.CAT));

        dataAccess.clear();

        var actual = dataAccess.listUsers();
        assertEquals(0, actual.size());
    }



    @ParameterizedTest
    @ValueSource(classes = {MySqlDataUser.class, MemoryDataAccess.class})
    void login(Class<? extends UserDAO> dbClass) throws Exception {
        UserDAO dataAccess = getDataAccess(dbClass);

        dataAccess.adduser(new UserData("a", "b", "c"));
        //dataAccess.addPet(new Pet(0, "sally", PetType.CAT));

        assertEquals(dataAccess.checkMatching(new UserData("a","b","c")),true);

        //var actual = dataAccess.listUsers();
        //assertEquals(0, actual.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataUser.class})
    void loginBad(Class<? extends UserDAO> dbClass) throws ResponseException {
        UserDAO dataAccess = getDataAccess(dbClass);

        assertThrows(NullPointerException.class, () -> {
            dataAccess.checkMatching(null);
        });
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