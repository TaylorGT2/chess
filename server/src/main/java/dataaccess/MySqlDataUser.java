package dataaccess;


import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
public class MySqlDataUser implements UserDAO{

    public MySqlDataUser() throws ResponseException {
        configureDatabase();
    }
    public Collection<UserData> listUsers() throws ResponseException {
        var result = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readUser(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public UserData adduser(UserData user) throws ResponseException{

        if(user.password()==""||user.username()==""||user.email()==""){
            throw new ResponseException(400, "Error: bad request");
        }

        if(getUser(user.username())!=null){
            throw new ResponseException(403,"Error: username already taken");
        }

        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        //var statement = "INSERT INTO auth VALUES (?, ?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        UserData clearUser = new UserData(user.username(),hashedPassword,user.email());
        var json = new Gson().toJson(clearUser);
        //var cryptedPassword =
        var id = executeUpdate(statement, user.username(), hashedPassword, user.email(), json);

        return new UserData(user.username(), hashedPassword, user.email());
    }




    boolean verifyUser(String username, String providedClearTextPassword) throws ResponseException {


        UserData userLog = getUser(username);
        if(userLog==null){
            throw new ResponseException(401, "Error: unauthorized");
        }


        var hashedPassword = BCrypt.hashpw(providedClearTextPassword, BCrypt.gensalt());

        String checkWord = userLog.password();


        //return BCrypt.checkpw(providedClearTextPassword,checkWord);
        if(providedClearTextPassword.equals(userLog.password())|| BCrypt.checkpw(providedClearTextPassword,checkWord)){
            return true;
        }
        else{
            throw new ResponseException(401,"Error: unauthorized");
        }

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
            throw new ResponseException(500, String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }catch (DataAccessException ex) {
            throw new ResponseException(500,"Error: execute error");
        }
    }

    public Boolean checkMatching(UserData checkUser) throws ResponseException{

        if (checkUser.username()==null || checkUser.password()==null){
            throw new ResponseException(400, "Error: bad request");
        }

        return verifyUser(checkUser.username(),checkUser.password());


    }


    public UserData getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var json = rs.getString("json");
        var auth = new Gson().fromJson(json, UserData.class);
        return auth.setUser(username);
    }

    public void clear() throws ResponseException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              
              INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public void configureDatabase() throws ResponseException {
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
