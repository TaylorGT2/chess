package dataaccess;

import model.UserData;
import model.AuthData;
import model.GameData;
import exception.ResponseException;
//import model.Pet;

import java.util.Collection;

public interface GameDao {

    void clear() throws ResponseException;
    GameData createGame(String gameName);
//    GameData getGame(int gameID);
    Collection<GameData> listGames() throws ResponseException;
//    GameData updateGame(int gameID, String gameName);
   // void joinGame()
    void joinGame(int gameID, String playerColor, String username);

    GameData getGame(int gameID);

}
