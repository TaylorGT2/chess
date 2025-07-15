package dataaccess;

import exception.ResponseException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (InputStream in = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties props = new Properties();
                props.load(in);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);

            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws ResponseException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws ResponseException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}


//
//
//
//
//
//
//
//
//
//
//
//
//package dataaccess;
//
//import java.sql.*;
//        import java.util.Properties;
//
//public class DatabaseManager {
//    private static String databaseName;
//    private static String dbUsername;
//    private static String dbPassword;
//    private static String connectionUrl;
//
//    /*
//     * Load the database information for the db.properties file.
//     */
//    static {
//        loadPropertiesFromResources();
//    }
//
//    /**
//     * Creates the database if it does not already exist.
//     */
//    static public void createDatabase() throws DataAccessException {
//        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
//        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
//             var preparedStatement = conn.prepareStatement(statement)) {
//            preparedStatement.executeUpdate();
//        } catch (SQLException ex) {
//            throw new DataAccessException("failed to create database", ex);
//        }
//    }
//
//    /**
//     * Create a connection to the database and sets the catalog based upon the
//     * properties specified in db.properties. Connections to the database should
//     * be short-lived, and you must close the connection when you are done with it.
//     * The easiest way to do that is with a try-with-resource block.
//     * <br/>
//     * <code>
//     * try (var conn = DatabaseManager.getConnection()) {
//     * // execute SQL statements.
//     * }
//     * </code>
//     */
//    static Connection getConnection() throws DataAccessException {
//        try {
//            //do not wrap the following line with a try-with-resources
//            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
//            conn.setCatalog(databaseName);
//            return conn;
//        } catch (SQLException ex) {
//            throw new DataAccessException("failed to get connection", ex);
//        }
//    }
//
//    private static void loadPropertiesFromResources() {
//        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
//            if (propStream == null) {
//                throw new Exception("Unable to load db.properties");
//            }
//            Properties props = new Properties();
//            props.load(propStream);
//            loadProperties(props);
//        } catch (Exception ex) {
//            throw new RuntimeException("unable to process db.properties", ex);
//        }
//    }
//
//    private static void loadProperties(Properties props) {
//        databaseName = props.getProperty("db.name");
//        dbUsername = props.getProperty("db.user");
//        dbPassword = props.getProperty("db.password");
//
//        var host = props.getProperty("db.host");
//        var port = Integer.parseInt(props.getProperty("db.port"));
//        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
//    }
//}




