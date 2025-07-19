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
//import model.PetType;

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
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteGame(int gameID) throws ResponseException {
        var statement = "DELETE FROM game WHERE gameID=?";
        executeUpdate(statement, gameID);
    }



    public void joinGame(int gameID, String playerColor, String username) throws ResponseException{
        //GameData joining = getGame(gameID);
        //var statement = "INSERT gameID, json FROM game WHERE gameID=?";


        //try (var conn = DatabaseManager.getConnection()) {

            if(playerColor.equals("WHITE")) {
                // INSERT INTO game (gameName, gameID, chessGame, json) VALUES (?, ?, ?, ?)
                //var statement = "INSERT INTO game (whiteUsername) VALUE (?) WHERE gameID=?";
                //var statement = "UPDATE game SET whiteUsername = (?) WHERE gameID=(?)";
                GameData joining = getGame(gameID);
                deleteGame(gameID);
                updateGame(username,gameID, joining.gameName(),joining.blackUsername(),joining.game());

            }
            else if(playerColor.equals("BLACK")) {
                // INSERT INTO game (gameName, gameID, chessGame, json) VALUES (?, ?, ?, ?)
                //var statement = "INSERT INTO game (whiteUsername) VALUE (?) WHERE gameID=?";
                //var statement = "UPDATE game SET whiteUsername = (?) WHERE gameID=(?)";
                GameData joining = getGame(gameID);
                deleteGame(gameID);
                updateGame(joining.whiteUsername(),gameID, joining.gameName(),username,joining.game());

            }
            else{
                throw new ResponseException(500, "wrong color");
            }

//            if(playerColor=="Black"){
//                var statement = "INSERT INTO game (blackUsername) VALUES (?) WHERE gameID=?";
//                try (var ps = conn.prepareStatement(statement)) {
//                    ps.setInt(1, gameID);
//                    try (var rs = ps.executeQuery()) {
//                        if (rs.next()) {
//                            //return readGame(rs);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
//        }
        //return null;



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
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        var statement = "INSERT INTO game (gameName, gameID, chessGame, json, whiteUsername, blackUsername) VALUES (?, ?, ?, ?, ?, ?)";
        var game = new ChessGame();
        var chess = new Gson().toJson(game);
        GameData current = new GameData(gameID,"","",gameName,game);
        var json = new Gson().toJson(current);
        var id = executeUpdate(statement, gameName, gameID, chess, json, "", "");
        String bigChess = "I need to learn serialization";
        gameID = gameID+1;
        return new GameData(gameID, "", "", gameName, game);
    }


    public void updateGame(String whiteUsername, int gameID, String name, String blackUsername, ChessGame chess) throws ResponseException {
        var statement = "INSERT INTO game (gameName, gameID, whiteUsername, blackUsername, chessGame, json) VALUES (?, ?, ?, ?, ?, ?)";
        //var game = new ChessGame();
        var board = new Gson().toJson(chess);
        GameData current = new GameData(gameID,whiteUsername,blackUsername,name,chess);
        var json = new Gson().toJson(current);
        var id = executeUpdate(statement, name, gameID, whiteUsername, blackUsername, board, json);
        //String bigChess = "I need to learn serialization";
        //gameID = gameID+1;
        //return new GameData(gameID, null, null, gameName, game);
    }


//    public static Gson createSerializer() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//
//        gsonBuilder.registerTypeAdapter(ChessPiece.class,
//                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> {
//                    ChessPiece chessPiece = null;
//                    if (el.isJsonObject()) {
//                        String pieceType = el.getAsJsonObject().get("type").getAsString();
//                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
//                            case PAWN -> chessPiece = ctx.deserialize(el, ChessPiece.setChessPiece.(PAWN));
//                            case ROOK -> chessPiece = ctx.deserialize(el, Rook.class);
//                            case KNIGHT -> chessPiece = ctx.deserialize(el, Knight.class);
//                            case BISHOP -> chessPiece = ctx.deserialize(el, Bishop.class);
//                            case QUEEN -> chessPiece = ctx.deserialize(el, Queen.class);
//                            case KING -> chessPiece = ctx.deserialize(el, King.class);
//                        }
//                    }
//                    return chessPiece;
//                });
//
//        return gsonBuilder.create();
//    }





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
              `chessGame` TEXT DEFAULT NULL,
              
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
