package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;


import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAuth implements AuthDAO{


    public MySqlDataAuth() throws ResponseException {
        configureDatabase();
    }


    public AuthData createAuth(AuthData auth) throws ResponseException {
        if(auth.username()==null){
            throw new ResponseException(400,"Error: bad request");
        }
        var statement = "INSERT INTO auth (username, authToken, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(auth);
        String t = generateToken();
        var id = executeUpdate(statement, auth.username(), t, json);
        return new AuthData(auth.username(), t);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void clear() throws ResponseException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var json = rs.getString("json");
        var auth = new Gson().fromJson(json, AuthData.class);
        return auth.setAuth(authToken);
    }


    public Collection<AuthData> listUsers() throws ResponseException {
        var result = new ArrayList<AuthData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM auth";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readAuth(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteAuth(String authToken) throws ResponseException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        AuthData authorize = getAuth(authToken);
        if (authorize==null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        executeUpdate(statement, authToken);
    }



    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p){ ps.setString(i + 1, p);}
                    else if (param instanceof Integer p){ ps.setInt(i + 1, p);}

                    else if (param == null){ ps.setNull(i + 1, NULL);}
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
            CREATE TABLE IF NOT EXISTS  auth (
              
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              
              INDEX(username)
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


