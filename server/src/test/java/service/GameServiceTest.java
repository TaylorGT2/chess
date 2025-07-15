package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    static final GameService SERVICE = new GameService(new MemoryGameDataAccess());
    @Test
    void createGame() throws ResponseException{

        GameData gametest = SERVICE.createGame("testGame");

        GameData gametest2 = SERVICE.createGame("testGame2");

        assertEquals(SERVICE.listGames().size(),2);

    }
    @Test
    void createGameBad() throws ResponseException{



        assertThrows(ResponseException.class, () -> {
            SERVICE.createGame(null);
        });

    }




    @Test
    void joinGame() throws ResponseException {

        GameData gametest = SERVICE.createGame("testGame");

        SERVICE.joinGame(gametest.gameID(),"WHITE", "hybob");

        assertEquals(SERVICE.listGames().size(),1);

        assertEquals(SERVICE.getGame(gametest.gameID()).whiteUsername(),"hybob");

    }

    @Test
    void joinGameBad() throws ResponseException {

        GameData gametest = SERVICE.createGame("testGame");

        assertThrows(ResponseException.class, () -> {
            SERVICE.joinGame(gametest.gameID(),"WHIT", "hybob");
        });



    }

@Test
    void listGames() throws ResponseException {

        SERVICE.clear();

        GameData gametest = SERVICE.createGame("testGame");

        GameData gametest2 = SERVICE.createGame("testGame2");

        assertEquals(SERVICE.listGames().size(),2);




    }



    @Test
    void clear() throws ResponseException {
        //var user = new GameData("c","b");
        var user = SERVICE.createGame("test");
        SERVICE.clear();
        assertEquals(SERVICE.listGames().size(),0);
    }

@Test
    void getGame() throws ResponseException {



        assertThrows(ResponseException.class, () -> {
            SERVICE.getGame(333);
        });


    }


}