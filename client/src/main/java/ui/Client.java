package ui;



import java.util.Arrays;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import model.UserData;
import model.AuthData;
import exception.ResponseException;
import server.ServerFacade;
//import client.websocket.NotificationHandler;


//import server.ServerFacade;


//import client.websocket.WebSocketFacade;

public class Client {

    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private NotificationHandler notificationHandler;
    public Repl r;
    //private WebSocketFacade ws;
    private State state = State.SIGNEDOUT;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;

    }

    public Client(String serverUrl, Repl r) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.r = r;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> login(params);

                case "login" -> signOut();
                case "register" -> register(params);
                case "r" -> register(params);


                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            var username = params[0];
            var password = params[1];
            var email = params[2];
            UserData use = new UserData(username, password, email);
            var userData = server.addUser(use);
            login(username,password);

            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }


    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            var username = params[0];
            var password = params[1];
            var userData = getUser(username);

            var loginable = server.login(userData);

            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }


    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                    - Register a new user: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                    - Exit the program: "q", "quit"
                    - Print this message: "h", "help"
                    """;
        }
        return """
                - Print this message: "h", "help"
                - Ends session: "logout"
                - Creates a new game: "create" <GAMENAME>
                - Lists all the games: "list"
                - Play a Game: "play"
                - Observe a game: "watch"
                """;
    }

    public String signOut() throws ResponseException {
        assertSignedIn();
        //ws.leavePetShop(visitorName);
        //ws = null;
        state = State.SIGNEDOUT;
        return String.format("%s left the session", visitorName);
    }

    private UserData getUser(String username) throws ResponseException {
        for (var users : server.listUsers()) {
            if (users.username() == username) {
                return users;
            }
        }
        return null;
    }





    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }



}
