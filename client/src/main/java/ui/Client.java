package ui;



import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import model.GameData;
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

    ArrayList<Integer> gameList;
    //private WebSocketFacade ws;

    String bestToken;

    private State state = State.SIGNEDOUT;

    public Integer fakeGameID;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        this.bestToken="";
        gameList = new ArrayList<>();
        fakeGameID=1;

    }

    public Client(String serverUrl, Repl r) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.r = r;
        this.bestToken="";
        gameList = new ArrayList<>();
        fakeGameID=1;
    }

    public Client(String serverUrl){
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.bestToken="";
        gameList = new ArrayList<>();
        fakeGameID=1;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> login(params);

                case "login" -> login(params);
                case "logout" -> signOut();
                case "register" -> register(params);
                case "r" -> register(params);

                case "create" -> createGame(params);

                case "list" -> listGames();

                case "watch" -> watch();

                case "play" -> playGame(params);

                case "clear" -> clear();




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
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String watch(String... params) throws ResponseException{
        if (params.length >= 1) {

            String reqGame = params[0];
            Integer check = Integer.parseInt(reqGame);
            gameList.size();
            if(gameList.contains(check)) {

                ChessBoardBuilder b = new ChessBoardBuilder();
                //b.drawHeaders(b.out);
                //b.drawChessBoard(b.out);
                b.totalWhiteBoard(b.out);
                return "behold";
            }
            throw new ResponseException(404, "Please use a valid list number");
        }
        throw new ResponseException(404, "Expected: <GameNumber>");
        //return "behold";

    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames(bestToken);
        var result = new StringBuilder();
        var gson = new Gson();
        int numbering = 1;
        for (var game: games) {
            game.gameName();
            result.append(numbering);
            //gameList
            //result.append(gson.toJson(game)).append('\n');
            result.append(game.gameName()).append('\n');
            numbering+=1;
        }
        return result.toString();
    }

//    public String listPets() throws ResponseException {
//        assertSignedIn();
//        var pets = server.listPets();
//        var result = new StringBuilder();
//        var gson = new Gson();
//        for (var pet : pets) {
//            result.append(gson.toJson(pet)).append('\n');
//        }
//        return result.toString();
//    }


    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            var name = params[0];

            GameData game = new GameData(0,null,null,name,null);
            var gameData = server.createGame(game, bestToken);
            //login(username,password);
            gameList.add(fakeGameID);
            fakeGameID+=1;

            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);
            return String.format("You created a game as %s.", name);
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    public String playGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            //visitorName = String.join("-", params);
            var gameNumber = params[0];
            int gameNum = Integer.parseInt(gameNumber);

            //var gameID = 8+4*gameNum;
            var gameID = gameNum;;

            var playerColor = params[1];
            String[] ingredients = {String.valueOf(gameID),playerColor};
            String good = new Gson().toJson(params);

            //GameData game = new GameData(0,null,null,name,null);
            //String goodParams = params.toString();
            server.playGame(gameID,playerColor, bestToken);
            //login(username,password);

            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);

            if(playerColor=="WHITE"){
                ChessBoardBuilder b = new ChessBoardBuilder();
                //b.drawHeaders(b.out);
                //b.drawChessBoard(b.out);
                b.totalWhiteBoard(b.out);
                return "behold";
            }
            else{
                ChessBoardBuilder b = new ChessBoardBuilder();
                //b.drawHeaders(b.out);
                //b.drawChessBoard(b.out);
                b.totalBlackBoard(b.out);
                return "behold";
            }



            //return String.format("You entered a game");
        }
        throw new ResponseException(400, "Expected: <gameNum> <BLACK|WHITE");
    }



    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            var username = params[0];
            var password = params[1];
            UserData logging = new UserData(username,password, "fill");
            //var userData = getUser(username);

            var loginable = server.login(logging);

            bestToken=loginable.authToken();

            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", username);
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
        server.logout(bestToken);
        state = State.SIGNEDOUT;

        return String.format("%s left the session", visitorName);
    }

    public String clear() throws ResponseException{
        server.deleteAllUsers();
        return "done";
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
