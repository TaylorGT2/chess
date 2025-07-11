package service;

import dataaccess.AuthDAO;
import model.AuthData;
import model.GameData;
import dataaccess.GameDao;
import exception.ResponseException;

import java.util.Collection;

public class GameService {
    private final GameDao dataAccess;

    public GameService(GameDao dataAccess) {
        this.dataAccess = dataAccess;
    }

    public GameData createGame(String gameName) throws ResponseException{

        return dataAccess.createGame(gameName);
    }
//    public GameData getGame(int gameID) throws ResponseException{
//        return dataAccess.getGame(gameID);
//    }
//    public void deleteAllUsers() throws ResponseException{
//        dataAccess.clear();
//    }
//
//    public Collection<GameData> listGames() throws ResponseException {
//        return dataAccess.listGames();
//    }



}
