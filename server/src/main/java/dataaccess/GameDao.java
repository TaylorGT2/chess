package dataaccess;

import model.UserData;
import model.AuthData;
import model.GameData;
import exception.ResponseException;
//import model.Pet;

import java.util.Collection;

public interface GameDao {

    void clear() throws ResponseException;
    GameData createGame(AuthData auth);
    GameData getGame(String authToken);
    Collection<AuthData> listGames() throws ResponseException;
    GameData updateGame(int gameID, String gameName);

}
