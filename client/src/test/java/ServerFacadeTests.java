package client;




import org.junit.jupiter.api.*;
import server.Server;



import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDao;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.GameService;
import service.UserService;
import service.AuthService;
import ui.Client;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerFacadeTests {

    private static Server server;

    //static public Server server;
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

        client.clear();
        ServerFacade fakeServe = client.server;

        UserData testUser = new UserData("a","b", "c");

        //asssertDoesNotThrow(()->fakeServe.addUser(testUser));

        assertDoesNotThrow(() -> fakeServe.addUser(testUser));

        var result = assertDoesNotThrow(() -> client.register("aadfs","bdfsa","cccc"));

    }

    @Test
    void registerBad() throws Exception {

        ServerFacade fakeServe = client.server;

        client.clear();

        UserData testUser = new UserData("a","b", null);

        //var result = assertDoesNotThrow(() -> client.register("aadfs","bdfsa","cccc"));

        assertThrows(ResponseException.class, () -> {
            fakeServe.addUser(testUser);
        });

        assertThrows(ResponseException.class, () -> {
            client.register("aadfs","bdfsa",null);
        });

    }

    @Test
    void logout() throws ResponseException {

        ServerFacade fakeServe = client.server;

        client.clear();

        client.register("a8","b8","c8");

        String authToken = client.bestToken;

        assertDoesNotThrow(() -> fakeServe.logout(authToken));
        //var result = assertDoesNotThrow(() -> client.signOut());

    }

    @Test
    void logoutBad() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();


        assertThrows(ResponseException.class, () -> {
            fakeServe.logout("abc");
        });


        assertThrows(ResponseException.class, () -> {
            client.signOut();
        });


    }


    @Test
    void game() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();

        //String authToken = client.bestToken;

        GameData test = new GameData(1,null,null,"name",null);

        client.register("a8","b8","c8");
        String authToken = client.bestToken;

        assertDoesNotThrow(() -> fakeServe.createGame(test,authToken));
        var result = assertDoesNotThrow(() -> client.createGame("gameName"));

    }





    @Test
    void createGame() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();

        client.register("a8","b8","c8");
        String authToken = client.bestToken;
        client.createGame("gameName");
        assertDoesNotThrow(() -> fakeServe.listGames(authToken));
        var result = assertDoesNotThrow(() -> client.listGames());

    }

    @Test
    void createGameBad() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();

        client.register("a8","b8","c8");
        client.createGame("gameName");

        var result = assertDoesNotThrow(() -> client.listGames());
        assertThrows(ResponseException.class, () -> {
            fakeServe.createGame(null,null);
        });
        assertThrows(NullPointerException.class, () -> {
            client.createGame(null);
        });

    }



    @Test
    void playGame() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();

        client.register("a8","b8","c8");
        client.createGame("gameName");

        String authToken = client.bestToken;



        assertDoesNotThrow(() -> fakeServe.playGame(1,"WHITE",authToken));

        //var result = assertDoesNotThrow(() -> client.playGame("1","WHITE"));

    }


    @Test
    void playGameBad() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();

        client.register("a8","b8","c8");
        client.createGame("gameName");

        //var result = assertDoesNotThrow(() -> client.playGame("1","WHITE"));
        assertThrows(ResponseException.class, () -> {
            client.playGame("1","WHIT");
        });

    }

    @Test
    void watch() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();

        client.register("a8","b8","c8");
        client.createGame("gameName");

        var result = assertDoesNotThrow(() -> client.watch("1"));

    }
    @Test
    void watchBad() throws ResponseException {
        ServerFacade fakeServe = client.server;

        client.clear();

        client.register("a8", "b8", "c8");
        client.createGame("gameName");

        //var result = assertThrows(() -> client.watch("a"));
        assertThrows(NumberFormatException.class, () -> {
            client.watch("a");
        });
    }

}
