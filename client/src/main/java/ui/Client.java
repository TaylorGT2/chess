package ui;



import java.util.ArrayList;
import java.util.Arrays;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
//import com.sun.nio.sctp.NotificationHandler;
import model.GameData;
import model.UserData;
import exception.ResponseException;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;


//import server.ServerFacade;


//import client.websocket.WebSocketFacade;

public class Client {

    private String visitorName = null;
    public ServerFacade server;
    private final String serverUrl;
    private NotificationHandler notificationHandler;
    public Repl r;

    private WebSocketFacade ws;

    ArrayList<Integer> gameList;
    //private WebSocketFacade ws;

    public String bestToken;

    private State state = State.SIGNEDOUT;

    public Integer fakeGameID;


    public ChessBoard board;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        this.bestToken="";
        gameList = new ArrayList<>();
        fakeGameID=1;
        board = new ChessBoard();

    }

    public Client(String serverUrl, Repl r) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.r = r;
        this.bestToken="";
        gameList = new ArrayList<>();
        fakeGameID=1;
        board = new ChessBoard();
    }

    public Client(String serverUrl){
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.bestToken="";
        gameList = new ArrayList<>();
        fakeGameID=1;
        board = new ChessBoard();
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

    public void makeBoard(){
        ChessBoardBuilder b = new ChessBoardBuilder();
        b.totalWhiteBoard(b.out);
        boolean color = true;
        board = new ChessBoard();
        board.resetBoard();

        for(int i = 1; i<9; i++){
            for(int j = 1; j<9; j++){

                ChessPiece p = board.getPiece(new ChessPosition(i,j));
                if(p==null&&color==true){
                    b.setRed(b.out);
                    b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
                }
                else if(p==null&&color==false){
                    b.setWhite(b.out);
                    b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
                }
                else if(p.getPieceType()==ROOK && p.getTeamColor() == BLACK){
                    b.printPlayer(b.out," r ",color);
                }
                else if(p.getPieceType()==BISHOP && p.getTeamColor() == BLACK){
                    b.printPlayer(b.out," b ",color);
                }
                else if(p.getPieceType()==KNIGHT && p.getTeamColor() == BLACK){
                    b.printPlayer(b.out," n ",color);
                }
                else if(p.getPieceType()==QUEEN && p.getTeamColor() == BLACK){
                    b.printPlayer(b.out," q ",color);
                }
                else if(p.getPieceType()==KING && p.getTeamColor() == BLACK){
                    b.printPlayer(b.out," k ",color);
                }
                else if(p.getPieceType()==PAWN && p.getTeamColor() == BLACK){
                    b.printPlayer(b.out," p ",color);
                }
                else if(p.getPieceType()==ROOK && p.getTeamColor() == WHITE){
                    b.printPlayer(b.out," R ",color);
                }
                else if(p.getPieceType()==BISHOP && p.getTeamColor() == WHITE){
                    b.printPlayer(b.out," B ",color);
                }
                else if(p.getPieceType()==KNIGHT && p.getTeamColor() == WHITE){
                    b.printPlayer(b.out," N ",color);
                }
                else if(p.getPieceType()==QUEEN && p.getTeamColor() == WHITE){
                    b.printPlayer(b.out," Q ",color);
                }
                else if(p.getPieceType()==KING && p.getTeamColor() == WHITE){
                    b.printPlayer(b.out," K ",color);
                }
                else if(p.getPieceType()==PAWN && p.getTeamColor() == WHITE){
                    b.printPlayer(b.out," P ",color);
                }

                if(j==8){
                    b.setBlack(b.out);
                }

                if(color == true){
                    color = false;
                }
                else{
                    color = true;
                }

            }
            b.out.println();
            if(color == true){
                color = false;
            }
            else{
                color = true;
            }

        }



        //b.drawHeaders(b.out);
        //b.drawChessBoard(b.out);
        //b.totalWhiteBoard(b.out);
        //return "behold";
    }

    public String watch(String... params) throws ResponseException{
        if (params.length >= 1) {

            String reqGame = params[0];
            Integer check = Integer.parseInt(reqGame);
            gameList.size();
            if(gameList.contains(check)) {
                makeBoard();
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
            state = State.PLAYING;
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




            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.connect(bestToken,gameID);


            if(playerColor=="WHITE"){
                ChessBoardBuilder b = new ChessBoardBuilder();

                //b.totalWhiteBoard(b.out);


                makeBoard();

                return "behold";
            }
            else{
                ChessBoardBuilder b = new ChessBoardBuilder();

                //b.totalBlackBoard(b.out);

                makeBoard();

                return "behold";
            }



            //return String.format("You entered a game");
        }
        throw new ResponseException(400, "Expected: <gameNum> <BLACK|WHITE");
    }



    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            //visitorName = String.join("-", params);
            var username = params[0];
            var password = params[1];
            visitorName=username;
            UserData logging = new UserData(username,password, "fill");
            //var userData = getUser(username);

            var loginable = server.login(logging);

            bestToken=loginable.authToken();


//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.connect(bestToken,gameID);


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
        if (state == State.PLAYING) {
            return """
                    - Redraw Chess Board: "redraw"
                    - Resign: "resign"
                    - Highlight Legal Moves: "highlight" <piece>
                    - Make move: "move" <move>
                    - Exit the game: "leave"
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
