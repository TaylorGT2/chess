package dataaccess;

import chess.ChessGame;
import model.UserData;
import model.AuthData;
import model.GameData;
import exception.ResponseException;


import java.util.Collection;

public interface GameDao {

    void clear() throws ResponseException;
    GameData createGame(String gameName) throws ResponseException;

    Collection<GameData> listGames() throws ResponseException;

    void joinGame(int gameID, String playerColor, String username) throws ResponseException;

    GameData getGame(int gameID) throws ResponseException;

    // this might kill everything

    void deleteGame(int gameID) throws ResponseException;

    void updateGame(String whiteUsername, int gameID, String name, String blackUsername, ChessGame chess) throws ResponseException;

}
