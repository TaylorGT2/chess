package dataaccess;


import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.Objects;

import static chess.ChessPiece.PieceType.PAWN;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataGame implements GameDao{

    int gameID;

    public MySqlDataGame() throws ResponseException {
        configureDatabase();
        gameID = 0;
    }


    public GameData getGame(int gameID) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameName, json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseException(500, String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteGame(int gameID) throws ResponseException {
        var statement = "DELETE FROM game WHERE gameID=?";
        executeUpdate(statement, gameID);
    }



    public void joinGame(int gameID, String playerColor, String username) throws ResponseException{

        GameData joining2 = getGame(gameID);
        if(joining2==null){
            throw new ResponseException(400, "Error: Unauthorized");
        }


            if(playerColor.equals("WHITE")) {

                GameData joining = getGame(gameID);
                if(joining.whiteUsername()==null) {
                    deleteGame(gameID);
                    updateGame(username, gameID, joining.gameName(), joining.blackUsername(), joining.game());
                }
                else{
                    throw new ResponseException(403,"Error: color taken");
                }


            }
            else if(playerColor.equals("BLACK")) {

                GameData joining = getGame(gameID);
                if(joining.blackUsername()==null) {
                    deleteGame(gameID);
                    updateGame(joining.whiteUsername(), gameID, joining.gameName(), username, joining.game());
                }
                else{
                    throw new ResponseException(403,"Error: color taken");
                }

            }
            else{
                throw new ResponseException(400, "wrong color");
            }


    }




    public Collection<GameData> listGames() throws ResponseException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, gameName, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseException(500, String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return result;
    }


    private GameData readGame(ResultSet rs) throws SQLException {
        var username = rs.getString("gameName");
        var json = rs.getString("json");
        var auth = new Gson().fromJson(json, GameData.class);
        return auth.setGame(username);
    }

    public GameData createGame(String gameName) throws ResponseException {

        if(gameName==null){
            throw new ResponseException(400, "Error: bad request");
        }




        var statement = "INSERT INTO game (gameName, chessGame, json, whiteUsername, blackUsername) VALUES (?, ?, ?, ?, ?)";
        var game = new ChessGame();
        var chess = new Gson().toJson(game);
        GameData current = new GameData(gameID,null,null,gameName,game);
        var json = new Gson().toJson(current);
        var id = executeUpdate(statement, gameName, chess, json, null, null);
        String bigChess = "I need to learn serialization";
        gameID = gameID+4;
        return new GameData(id, null, null, gameName, game);
    }


    public void updateGame(String whiteUsername, int gameID, String name, String blackUsername, ChessGame chess) throws ResponseException {
        var statement = "INSERT INTO game (json, gameName, gameID, whiteUsername, blackUsername, chessGame) VALUES (?, ?, ?, ?, ?, ?)";

        var board = new Gson().toJson(chess);
        GameData current = new GameData(gameID,whiteUsername,blackUsername,name,chess);
        var json = new Gson().toJson(current);
        var id = executeUpdate(statement, json, name, gameID, whiteUsername, blackUsername, board);

    }







    public void clear() throws ResponseException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p){ ps.setString(i + 1, p);}
                    else if (param instanceof Integer p){ ps.setInt(i + 1, p);}

                    else if (param == null) {ps.setNull(i + 1, NULL);}
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }catch (DataAccessException ex) {
            throw new ResponseException(500,"Error: execute error");
        }

    }



    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `chessGame` TEXT DEFAULT NULL,
              
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException {
        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {
                for (var statement : createStatements) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            } catch (SQLException ex) {
                throw new ResponseException(500, String.format("Error: Unable to configure database: %s", ex.getMessage()));
            }
        }catch (DataAccessException ex){
            throw new ResponseException(500, "Error: creation error");
        }
    }



}
