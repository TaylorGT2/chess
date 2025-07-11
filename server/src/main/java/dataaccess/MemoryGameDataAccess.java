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



    public void clear(){
        users.clear();
    }

    public GameData createGame(String gameName) throws ResponseException{

        if(gameName!=null) {
            ChessGame c = new ChessGame();
            GameData UData = new GameData(gameID, null, null, gameName, c);
            // Create an Authtoken??
            users.put(gameID, UData);

            gameID += 1;

            return UData;
        }
        else{
            throw new ResponseException(400,"Error: bad request");
        }
    }

    public Collection<GameData> listGames() {
        return users.values();
    }

    public GameData getGame(int gameID){
        return users.get(gameID);
    }

    private void deleteGame(int gameID){
        users.remove(gameID);
    }

    public void joinGame(int gameID, String playerColor, String username){
        GameData joining = getGame(gameID);

        if(playerColor.equals("WHITE")){
            //users.get(gameID).whiteUsername()=username;
            String whiteUsername = username;
            GameData joined = new GameData(gameID,whiteUsername,joining.blackUsername(), joining.gameName(), joining.game());
            deleteGame(gameID);
            users.put(gameID,joined);
        }
        else if(playerColor.equals("BLACK")){
            GameData joined = new GameData(gameID, joining.whiteUsername(), username, joining.gameName(), joining.game());
            deleteGame(gameID);
            users.put(gameID,joined);
        }
        else{
            //you should throw something here
            //return;
        }
    }
    //GameData createGame(String gameName);
//    GameData getGame(int gameID);
//    Collection<GameData> listGames() throws ResponseException;
//    GameData updateGame(int gameID, String gameName);




}
