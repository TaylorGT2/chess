package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    static final GameService service = new GameService(new MemoryGameDataAccess());
    @Test
    void createGame() {
        assertEquals(2,1+1);
    }

    @Test
    void joinGame() throws ResponseException {

        GameData gametest = service.createGame("testGame");

        service.joinGame(gametest.gameID(),"WHITE", "hybob");

        assertEquals(service.listGames().size(),1);

        assertEquals(service.getGame(gametest.gameID()).whiteUsername(),"hybob");

    }

    @Test
    void joinGameBad() throws ResponseException {

        GameData gametest = service.createGame("testGame");

        assertThrows(ResponseException.class, () -> {
            service.joinGame(gametest.gameID(),"WHIT", "hybob");
        });



    }

@Test
    void listGames() throws ResponseException {

        GameData gametest = service.createGame("testGame");

        GameData gametest2 = service.createGame("testGame2");

        assertEquals(service.listGames().size(),2);




    }



    @Test
    void clear() throws ResponseException {
        //var user = new GameData("c","b");
        var user = service.createGame("test");
        service.clear();
        assertEquals(service.listGames().size(),0);
    }

@Test
    void getGame() throws ResponseException {



        assertThrows(ResponseException.class, () -> {
            service.getGame(333);
        });


    }


}