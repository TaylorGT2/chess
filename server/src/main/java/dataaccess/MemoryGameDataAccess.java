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

    public GameData getGame(int gameID) throws ResponseException{
        //return users.get(gameID);

        if(users.get(gameID)!=null) {
            return users.get(gameID);
        }
        else{
            throw new ResponseException(400,"Error: unauthorized");
        }
    }

    private void deleteGame(int gameID){
        users.remove(gameID);
    }

    public void joinGame(int gameID, String playerColor, String username) throws ResponseException{
        GameData joining = getGame(gameID);


        if(playerColor!=null) {
            if (playerColor.equals("WHITE")) {
                //users.get(gameID).whiteUsername()=username;
                if (joining.whiteUsername() == null) {
                    String whiteUsername = username;
                    GameData joined = new GameData(gameID, whiteUsername, joining.blackUsername(), joining.gameName(), joining.game());
                    deleteGame(gameID);
                    users.put(gameID, joined);
                } else {
                    throw new ResponseException(403, "Error: forbidden");
                }
            } else if (playerColor.equals("BLACK")) {
                if (joining.blackUsername() == null) {
                    GameData joined = new GameData(gameID, joining.whiteUsername(), username, joining.gameName(), joining.game());
                    deleteGame(gameID);
                    users.put(gameID, joined);
                } else {
                    throw new ResponseException(403, "Error: forbidden");
                }
            } else {
                //you should throw something here
                //return;
                //throw new ResponseException(401,"Error: unauthorized")
                throw new ResponseException(400, "Error: bad request");
//                GameData joined = new GameData(gameID, joining.whiteUsername(), username, joining.gameName(), joining.game());
//                deleteGame(gameID);
//                users.put(gameID, joined);
            }
        }
        else {
            throw new ResponseException(400, "Error: bad request");
        }
    }
    //GameData createGame(String gameName);
//    GameData getGame(int gameID);
//    Collection<GameData> listGames() throws ResponseException;
//    GameData updateGame(int gameID, String gameName);




}
