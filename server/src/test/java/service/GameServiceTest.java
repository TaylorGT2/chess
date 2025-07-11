package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import exception.ResponseException;
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
}