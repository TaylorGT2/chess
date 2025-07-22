package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDataGameTest {

//    @Test
//    void getGame() {
//    }

    private GameDao getDataAccess(Class<? extends GameDao> databaseClass) throws ResponseException {
        GameDao db;
        if (databaseClass.equals(MySqlDataGame.class)) {
            db = new MySqlDataGame();
        } else {
            db = new MemoryGameDataAccess();
        }
        db.clear();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataGame.class, MemoryGameDataAccess.class})
    void createGame(Class<? extends GameDao> dbClass) throws ResponseException {
        GameDao dataAccess = getDataAccess(dbClass);

        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        //var user = new GameData(123, "","","mygame", new ChessGame());

        assertDoesNotThrow(() -> dataAccess.createGame("mygame"));


    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataGame.class, MemoryGameDataAccess.class})
    void createGameBad(Class<? extends GameDao> dbClass) throws ResponseException {
        GameDao dataAccess = getDataAccess(dbClass);



        assertThrows(ResponseException.class, () -> {
            dataAccess.createGame(null);
        });


    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataGame.class, MemoryGameDataAccess.class})
    void listGame(Class<? extends GameDao> dbClass) throws ResponseException {
        GameDao dataAccess = getDataAccess(dbClass);

        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        //var user = new GameData(123, "","","mygame", new ChessGame());
        dataAccess.createGame("mygame");
        dataAccess.createGame("mygame2");

        var games = dataAccess.listGames();
        assertEquals(2,games.size());


        //assertDoesNotThrow(() -> dataAccess.createGame("mygame"));


    }



    @ParameterizedTest
    @ValueSource(classes = {MySqlDataGame.class})
    void joinGame(Class<? extends GameDao> dbClass) throws ResponseException {

        
        GameDao dataAccess = getDataAccess(dbClass);

        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        //var user = new GameData(123, "","","mygame", new ChessGame());
        GameData gameOn= dataAccess.createGame("mygame");
        dataAccess.joinGame(gameOn.gameID(),"WHITE",  "thisIsAUser");
        GameData done = dataAccess.getGame(gameOn.gameID());
        //var games = dataAccess.listGames();
        assertEquals("thisIsAUser",dataAccess.getGame(gameOn.gameID()).whiteUsername());


        //assertDoesNotThrow(() -> dataAccess.createGame("mygame"));


    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlDataGame.class})
    void joinGameBad(Class<? extends GameDao> dbClass) throws ResponseException {


        GameDao dataAccess = getDataAccess(dbClass);

        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        //var user = new GameData(123, "","","mygame", new ChessGame());
        GameData gameOn= dataAccess.createGame("mygame");
        //dataAccess.joinGame(gameOn.gameID(),"WHITE",  "thisIsAUser");
        assertThrows(ResponseException.class, () -> {
            dataAccess.joinGame(gameOn.gameID(),"WHIT", "hybob");
        });


        //assertDoesNotThrow(() -> dataAccess.createGame("mygame"));


    }



//
//    @Test
//    void joinGame() {
//    }
//
//    @Test
//    void listGames() {
//    }
//
//    @Test
//    void createGame() {
//    }
//
//    @Test
//    void clear() {
//    }
}