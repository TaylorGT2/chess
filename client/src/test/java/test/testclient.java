package test;


import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDao;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import service.GameService;
import service.UserService;
import service.AuthService;
import ui.Client;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class testclient {

    static private Server server;
    static Client client;

    @BeforeAll
    static void startServer() throws Exception {
        var service = new UserService(new UserDAO() {
            @Override
            public UserData adduser(UserData user) throws ResponseException {
                return null;
            }

            @Override
            public Boolean checkMatching(UserData loginUser) throws ResponseException {
                return null;
            }

            @Override
            public void clear() throws ResponseException {

            }

            @Override
            public Collection<UserData> listUsers() throws ResponseException {
                return List.of();
            }
        });
        var gameSer = new GameService(new GameDao() {
            @Override
            public void clear() throws ResponseException {

            }

            @Override
            public GameData createGame(String gameName) throws ResponseException {
                return null;
            }

            @Override
            public Collection<GameData> listGames() throws ResponseException {
                return List.of();
            }

            @Override
            public void joinGame(int gameID, String playerColor, String username) throws ResponseException {

            }

            @Override
            public GameData getGame(int gameID) throws ResponseException {
                return null;
            }
        });

        var authSer = new AuthService(new AuthDAO() {
            @Override
            public void clear() throws ResponseException {

            }

            @Override
            public AuthData createAuth(AuthData auth) throws ResponseException {
                return null;
            }

            @Override
            public AuthData getAuth(String authToken) throws ResponseException {
                return null;
            }

            @Override
            public Collection<AuthData> listUsers() throws ResponseException {
                return List.of();
            }

            @Override
            public void deleteAuth(String auth) throws ResponseException {

            }
        });
        server = new Server();
        server.run(0);
        var url = "http://localhost:" + server.port();
        client = new Client(url);
        //client.signIn("tester");
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        //var id = getId(client.rescuePet("joe", "frog"));
        //client.rescuePet("sally", "cat");

        var result = assertDoesNotThrow(() -> client.register("aadfs","bdfsa","cccc"));
        //assertEquals("joe says ribbit", result);
    }

    @Test
    void logout() throws Exception {
        //var id = getId(client.rescuePet("joe", "frog"));
        //client.rescuePet("sally", "cat");
        server.

        client.register("a6","b6","c6");

        var result = assertDoesNotThrow(() -> client.signOut());
        //assertEquals("joe says ribbit", result);
    }



}
