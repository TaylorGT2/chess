package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryGameDataAccess implements GameDao {

    private int gameID = 1;
    final private HashMap<Integer, GameData> users = new HashMap<>();

    //void clear() throws ResponseException;


    public void clear(){
        users.clear();
    }

    public GameData createGame(String gameName){
        GameData UData = new GameData(gameID,null,null,gameName,new ChessGame());
        // Create an Authtoken??
        users.put(gameID, UData);

        return UData;
    }
    //GameData createGame(String gameName);
//    GameData getGame(int gameID);
//    Collection<GameData> listGames() throws ResponseException;
//    GameData updateGame(int gameID, String gameName);




}
