package dataaccess;


import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
//import model.PetType;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataGame implements GameDao{

    public MySqlDataGame() throws ResponseException {
        configureDatabase();
    }


    public GameData getGame(int gameID) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void joinGame(int gameID, String playerColor, String username) throws ResponseException{
        GameData joining = getGame(gameID);


//        if(playerColor!=null) {
//            if (playerColor.equals("WHITE")) {
//
//                if (joining.whiteUsername() == null) {
//                    String whiteUsername = username;
//                    GameData joined = new GameData(gameID, whiteUsername, joining.blackUsername(), joining.gameName(), joining.game());
//                    deleteGame(gameID);
//                    users.put(gameID, joined);
//                } else {
//                    throw new ResponseException(403, "Error: forbidden");
//                }
//            } else if (playerColor.equals("BLACK")) {
//                if (joining.blackUsername() == null) {
//                    GameData joined = new GameData(gameID, joining.whiteUsername(), username, joining.gameName(), joining.game());
//                    deleteGame(gameID);
//                    users.put(gameID, joined);
//                } else {
//                    throw new ResponseException(403, "Error: forbidden");
//                }
//            } else {
//
//                throw new ResponseException(400, "Error: bad request");
//
//            }
//        }
//        else {
//            throw new ResponseException(400, "Error: bad request");
//        }
    }




    public Collection<GameData> listGames() throws ResponseException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameName, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
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
        var statement = "INSERT INTO game (gameName, gameID, whiteUsername, blackUsername, game, json) VALUES (?, ?, ?, ?, ?, ?)";
        var json = new Gson().toJson(gameName);
        var id = executeUpdate(statement, gameName, json);
        return new GameData(id, null, null, gameName, new ChessGame());
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
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                        //else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }

    }



    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `game` varchar(256) DEFAULT NULL,
              
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }


}
