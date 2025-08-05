package ui;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import chess.*;
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
import static ui.EscapeSequences.moveCursorToLocation;


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

    public Integer goodGame =1;


    public ChessBoard board;
    //board = board.resetBoard();

    public String color = "banana";

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

                case "watch" -> watch(params);

                case "play" -> playGame(params);

                case "clear" -> clear();

                case "leave" -> leaveGame();

                case "resign" -> resign();

                //case "leave" -> leaveGame();

                case "move" -> makeMove(params);

                case "redraw" -> redraw();

                case "legal" -> highlight(params);


                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException | InvalidMoveException ex) {
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

    public String leaveGame() throws ResponseException{
        state = State.SIGNEDIN;

        ws = new WebSocketFacade(serverUrl, notificationHandler, this);
        ws.leave(bestToken,goodGame);

        return "you left the game";
    }

    public String highlight(String... params) throws ResponseException{

        String letter = params[0];
        String num = params[1];

     //   String letter2 = params[2];
      //  String num2 = params[3];

        int col = 15;
        int row = 15;

//        int colEnd = 15;
//        int rowEnd = 15;

        if(color.equals("white")||color.equals("black")) {


            if (letter.equals("a")) {
                col = 1;
            }
            if (letter.equals("b")) {
                col = 2;
            }
            if (letter.equals("c")) {
                col = 3;
            }
            if (letter.equals("d")) {
                col = 4;
            }
            if (letter.equals("e")) {
                col = 5;
            }
            if (letter.equals("f")) {
                col = 6;
            }
            if (letter.equals("g")) {
                col = 7;
            }
            if (letter.equals("h")) {
                col = 8;
            }



            if (num.equals("8")) {
                row = 8;
            }
            if (num.equals("7")) {
                row = 7;
            }
            if (num.equals("6")) {
                row = 6;
            }
            if (num.equals("5")) {
                row = 5;
            }
            if (num.equals("4")) {
                row = 4;
            }
            if (num.equals("3")) {
                row = 3;
            }
            if (num.equals("2")) {
                row = 2;
            }
            if (num.equals("1")) {
                row = 1;
            }



        }
        ChessGame cheese = new ChessGame();
        cheese.setBoard(board);
        ChessPosition start = new ChessPosition(row,col);

        if(board.getPiece(start)==null){
            return "pick a real piece";
        }

        Collection<ChessMove> all = cheese.validMoves(start);
        ChessBoardBuilder b = new ChessBoardBuilder();
        if(color.equals("black")) {
            makeBlackLit(all, start);
        }
        else{
            makeBoard();
        }

//        for(var move:all){
//            moveCursorToLocation(move.getEndPosition().getRow(),move.getEndPosition().getColumn());
//            b.setGreen(b.out);
//            b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
//
//        }

        return "i hope your happy";
    }



    public void makeBlack(){
        ChessBoardBuilder b = new ChessBoardBuilder();
        b.totalBlackBoard(b.out);
        boolean color = true;
        //board = new ChessBoard();
        //board.resetBoard();

        for(int i = 1; i<9; i++){
            for(int j = 8; j>0; j--){

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

                if(j==1){
                    b.setBlack(b.out);
                }
                if(j==1&&i==1){
                    b.printHeaderText(b.out," 1 ");
                }
                if(j==1&&i==2){
                    b.printHeaderText(b.out," 2 ");
                }
                if(j==1&&i==3){
                    b.printHeaderText(b.out," 3 ");
                }
                if(j==1&&i==4){
                    b.printHeaderText(b.out," 4 ");
                }
                if(j==1&&i==5){
                    b.printHeaderText(b.out," 5 ");
                }
                if(j==1&&i==6){
                    b.printHeaderText(b.out," 6 ");
                }
                if(j==1&&i==7){
                    b.printHeaderText(b.out," 7 ");
                }
                if(j==1&&i==8){
                    b.printHeaderText(b.out," 8 ");
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




    }



    public void makeBlackLit(Collection<ChessMove> all, ChessPosition start){
        ChessBoardBuilder b = new ChessBoardBuilder();
        b.totalBlackBoard(b.out);
        boolean color = true;
        //board = new ChessBoard();
        //board.resetBoard();

        for(int i = 1; i<9; i++){
            for(int j = 8; j>0; j--){

                ChessPiece p = board.getPiece(new ChessPosition(i,j));
                ChessPosition green = new ChessPosition(i,j);
                ChessMove test = new ChessMove(start,green,null);
//                if(all.contains(test)){
//                    b.setRed(b.out);
//                    b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
//                }
                if(p==null&&color==true&&!all.contains(test)){
                    b.setRed(b.out);
                    b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
                }
                else if(p==null&&color==true&&all.contains(test)){
                    b.setGreen(b.out);
                    b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
                }
                else if(p==null&&color==false&&!all.contains(test)){
                    b.setWhite(b.out);
                    b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
                }
                else if(p==null&&color==false&&all.contains(test)){
                    b.setBlue(b.out);
                    b.out.print(b.EMPTY.repeat((int) (b.SQUARE_SIZE_IN_PADDED_CHARS)));
                }

                else if(p.getPieceType()==ROOK && p.getTeamColor() == BLACK && !all.contains(test)){
                    b.printPlayer(b.out," r ",color);
                }
                else if(p.getPieceType()==ROOK && p.getTeamColor() == BLACK && all.contains(test)){
                    b.printPlayerHigh(b.out," r ",color);
                }
                else if(p.getPieceType()==BISHOP && p.getTeamColor() == BLACK&& !all.contains(test)){
                    b.printPlayer(b.out," b ",color);
                }
                else if(p.getPieceType()==BISHOP && p.getTeamColor() == BLACK&& all.contains(test)){
                    b.printPlayerHigh(b.out," b ",color);
                }
                else if(p.getPieceType()==KNIGHT && p.getTeamColor() == BLACK&&!all.contains(test)){
                    b.printPlayer(b.out," n ",color);
                }
                else if(p.getPieceType()==KNIGHT && p.getTeamColor() == BLACK&&all.contains(test)){
                    b.printPlayerHigh(b.out," n ",color);
                }

                else if(p.getPieceType()==QUEEN && p.getTeamColor() == BLACK&&!all.contains(test)){
                    b.printPlayer(b.out," q ",color);
                }
                else if(p.getPieceType()==QUEEN && p.getTeamColor() == BLACK&&all.contains(test)){
                    b.printPlayerHigh(b.out," q ",color);
                }
                else if(p.getPieceType()==KING && p.getTeamColor() == BLACK&&!all.contains(test)){
                    b.printPlayer(b.out," k ",color);
                }
                else if(p.getPieceType()==KING && p.getTeamColor() == BLACK&&all.contains(test)){
                    b.printPlayerHigh(b.out," k ",color);
                }
                else if(p.getPieceType()==PAWN && p.getTeamColor() == BLACK&&!all.contains(test)){
                    b.printPlayer(b.out," p ",color);
                }
                else if(p.getPieceType()==PAWN && p.getTeamColor() == BLACK&&all.contains(test)){
                    b.printPlayerHigh(b.out," p ",color);
                }
                else if(p.getPieceType()==ROOK && p.getTeamColor() == WHITE&&!all.contains(test)){
                    b.printPlayer(b.out," R ",color);
                }
                else if(p.getPieceType()==ROOK && p.getTeamColor() == WHITE&&all.contains(test)){
                    b.printPlayerHigh(b.out," R ",color);
                }
                else if(p.getPieceType()==BISHOP && p.getTeamColor() == WHITE&&!all.contains(test)){
                    b.printPlayer(b.out," B ",color);
                }
                else if(p.getPieceType()==BISHOP && p.getTeamColor() == WHITE&&all.contains(test)){
                    b.printPlayerHigh(b.out," B ",color);
                }
                else if(p.getPieceType()==KNIGHT && p.getTeamColor() == WHITE&&!all.contains(test)){
                    b.printPlayer(b.out," N ",color);
                }
                else if(p.getPieceType()==KNIGHT && p.getTeamColor() == WHITE&&all.contains(test)){
                    b.printPlayerHigh(b.out," N ",color);
                }
                else if(p.getPieceType()==QUEEN && p.getTeamColor() == WHITE&&!all.contains(test)){
                    b.printPlayer(b.out," Q ",color);
                }
                else if(p.getPieceType()==QUEEN && p.getTeamColor() == WHITE&&all.contains(test)){
                    b.printPlayerHigh(b.out," Q ",color);
                }
                else if(p.getPieceType()==KING && p.getTeamColor() == WHITE&&!all.contains(test)){
                    b.printPlayer(b.out," K ",color);
                }
                else if(p.getPieceType()==KING && p.getTeamColor() == WHITE&&all.contains(test)){
                    b.printPlayerHigh(b.out," K ",color);
                }
                else if(p.getPieceType()==PAWN && p.getTeamColor() == WHITE&&!all.contains(test)){
                    b.printPlayer(b.out," P ",color);
                }
                else if(p.getPieceType()==PAWN && p.getTeamColor() == WHITE&&all.contains(test)){
                    b.printPlayerHigh(b.out," P ",color);
                }

                if(j==1){
                    b.setBlack(b.out);
                }
                if(j==1&&i==1){
                    b.printHeaderText(b.out," 1 ");
                }
                if(j==1&&i==2){
                    b.printHeaderText(b.out," 2 ");
                }
                if(j==1&&i==3){
                    b.printHeaderText(b.out," 3 ");
                }
                if(j==1&&i==4){
                    b.printHeaderText(b.out," 4 ");
                }
                if(j==1&&i==5){
                    b.printHeaderText(b.out," 5 ");
                }
                if(j==1&&i==6){
                    b.printHeaderText(b.out," 6 ");
                }
                if(j==1&&i==7){
                    b.printHeaderText(b.out," 7 ");
                }
                if(j==1&&i==8){
                    b.printHeaderText(b.out," 8 ");
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




    }










    public void makeBoard(){
        ChessBoardBuilder b = new ChessBoardBuilder();
        b.totalWhiteBoard(b.out);
        boolean color = true;
        //board = new ChessBoard();
        //board.resetBoard();

        for(int i = 8; i>0; i--){
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
                if(j==8&&i==1){
                    b.printHeaderText(b.out," 1 ");
                }
                if(j==8&&i==2){
                    b.printHeaderText(b.out," 2 ");
                }
                if(j==8&&i==3){
                    b.printHeaderText(b.out," 3 ");
                }
                if(j==8&&i==4){
                    b.printHeaderText(b.out," 4 ");
                }
                if(j==8&&i==5){
                    b.printHeaderText(b.out," 5 ");
                }
                if(j==8&&i==6){
                    b.printHeaderText(b.out," 6 ");
                }
                if(j==8&&i==7){
                    b.printHeaderText(b.out," 7 ");
                }
                if(j==8&&i==8){
                    b.printHeaderText(b.out," 8 ");
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

    }

    public String watch(String... params) throws ResponseException{
        //int q = params.length+1;
        if (params.length >= 1) {

            String reqGame = params[0];



            try {
                //gameNum = Integer.parseInt(gameNumber);
                Integer check = Integer.parseInt(reqGame);
            }
            catch (NumberFormatException e){
                throw new ResponseException(404, "please use a number, not word");
            }
            Integer check = Integer.parseInt(reqGame);
            gameList.size();
            if(gameList.contains(check)) {
                makeBoard();
                return "behold";

            }
            throw new ResponseException(404, "Please use a valid list number");
        }
        throw new ResponseException(404, "Expected: <GameNumber>");


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
            result.append(game.gameName()).append(' ').append(game.whiteUsername()).append(' ').append(game.blackUsername()).append('\n');
            numbering+=1;
        }
        return result.toString();
    }




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
            board = new ChessBoard();
            board.resetBoard();

            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);
            return String.format("You created a game as %s.", name);
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    public String resign() throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler, this);
        ws.resign(bestToken,goodGame);


        return "you resigned";
    }

    public String playGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.PLAYING;
            //visitorName = String.join("-", params);
            var gameNumber = params[0];
            int gameNum = 0;
            try {
                gameNum = Integer.parseInt(gameNumber);
            }
            catch (NumberFormatException e){
                throw new ResponseException(404, "please use a number, not word");
            }



            //var gameID = 8+4*gameNum;
            var gameID = gameNum;
            goodGame=gameID;

            var playerColor = params[1];

            color = playerColor.toLowerCase();
            String[] ingredients = {String.valueOf(gameID),playerColor};
            String good = new Gson().toJson(params);

            //GameData game = new GameData(0,null,null,name,null);
            //String goodParams = params.toString();
            server.playGame(gameID,playerColor, bestToken);
            //login(username,password);

            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);




            ws = new WebSocketFacade(serverUrl, notificationHandler, this);
            ws.connect(bestToken,gameID);


//            if(playerColor.equals("white")){
//                ChessBoardBuilder b = new ChessBoardBuilder();
//
//
//
//
//                makeBoard();
//
//                return "behold";
//            }
//            else{
//                ChessBoardBuilder b = new ChessBoardBuilder();
//
//
//
//                makeBlack();
//
//                return "behold";
//            }

            return "behold";



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

    public String redraw(){
        if(color.equals("black")){
            makeBlack();
        }
        else {
            makeBoard();
        }
        return "redrawn";
    }

    public String makeMove(String... params) throws ResponseException, InvalidMoveException {
        String letter = params[0];
        String num = params[1];

        String letter2 = params[2];
        String num2 = params[3];

        int col = 15;
        int row = 15;

        int colEnd = 15;
        int rowEnd = 15;

        if(color.equals("white")||color.equals("black")) {


            if (letter.equals("a")) {
                col = 1;
            }
            if (letter.equals("b")) {
                col = 2;
            }
            if (letter.equals("c")) {
                col = 3;
            }
            if (letter.equals("d")) {
                col = 4;
            }
            if (letter.equals("e")) {
                col = 5;
            }
            if (letter.equals("f")) {
                col = 6;
            }
            if (letter.equals("g")) {
                col = 7;
            }
            if (letter.equals("h")) {
                col = 8;
            }

            if (letter2.equals("a")) {
                colEnd = 1;
            }
            if (letter2.equals("b")) {
                colEnd = 2;
            }
            if (letter2.equals("c")) {
                colEnd = 3;
            }
            if (letter2.equals("d")) {
                colEnd = 4;
            }
            if (letter2.equals("e")) {
                colEnd = 5;
            }
            if (letter2.equals("f")) {
                colEnd = 6;
            }
            if (letter2.equals("g")) {
                colEnd = 7;
            }
            if (letter2.equals("h")) {
                colEnd = 8;
            }

            if (num.equals("8")) {
                row = 8;
            }
            if (num.equals("7")) {
                row = 7;
            }
            if (num.equals("6")) {
                row = 6;
            }
            if (num.equals("5")) {
                row = 5;
            }
            if (num.equals("4")) {
                row = 4;
            }
            if (num.equals("3")) {
                row = 3;
            }
            if (num.equals("2")) {
                row = 2;
            }
            if (num.equals("1")) {
                row = 1;
            }


            if (num2.equals("8")) {
                rowEnd = 8;
            }
            if (num2.equals("7")) {
                rowEnd = 7;
            }
            if (num2.equals("6")) {
                rowEnd = 6;
            }
            if (num2.equals("5")) {
                rowEnd = 5;
            }
            if (num2.equals("4")) {
                rowEnd = 4;
            }
            if (num2.equals("3")) {
                rowEnd = 3;
            }
            if (num2.equals("2")) {
                rowEnd = 2;
            }
            if (num2.equals("1")) {
                rowEnd = 1;
            }

        }
        else{

            if (letter.equals("a")) {
                col = 1;
            }
            if (letter.equals("b")) {
                col = 2;
            }
            if (letter.equals("c")) {
                col = 3;
            }
            if (letter.equals("d")) {
                col = 4;
            }
            if (letter.equals("e")) {
                col = 5;
            }
            if (letter.equals("f")) {
                col = 6;
            }
            if (letter.equals("g")) {
                col = 7;
            }
            if (letter.equals("h")) {
                col = 8;
            }

            if (letter2.equals("a")) {
                colEnd = 1;
            }
            if (letter2.equals("b")) {
                colEnd = 2;
            }
            if (letter2.equals("c")) {
                colEnd = 3;
            }
            if (letter2.equals("d")) {
                colEnd = 4;
            }
            if (letter2.equals("e")) {
                colEnd = 5;
            }
            if (letter2.equals("f")) {
                colEnd = 6;
            }
            if (letter2.equals("g")) {
                colEnd = 7;
            }
            if (letter2.equals("h")) {
                colEnd = 8;
            }

            if (num.equals("8")) {
                row = 8;
            }
            if (num.equals("7")) {
                row = 7;
            }
            if (num.equals("6")) {
                row = 6;
            }
            if (num.equals("5")) {
                row = 5;
            }
            if (num.equals("4")) {
                row = 4;
            }
            if (num.equals("3")) {
                row = 3;
            }
            if (num.equals("2")) {
                row = 2;
            }
            if (num.equals("1")) {
                row = 1;
            }


            if (num2.equals("8")) {
                rowEnd = 8;
            }
            if (num2.equals("7")) {
                rowEnd = 7;
            }
            if (num2.equals("6")) {
                rowEnd = 6;
            }
            if (num2.equals("5")) {
                rowEnd = 5;
            }
            if (num2.equals("4")) {
                rowEnd = 4;
            }
            if (num2.equals("3")) {
                rowEnd = 3;
            }
            if (num2.equals("2")) {
                rowEnd = 2;
            }
            if (num2.equals("1")) {
                rowEnd = 1;
            }



        }

            ChessMove move = new ChessMove(new ChessPosition(row,col),new ChessPosition(rowEnd,colEnd),null);


        ws = new WebSocketFacade(serverUrl, notificationHandler, this);
        ws.makeMove(bestToken,goodGame,move);

       // board =
        //ChessGame play = new ChessGame();

       // play.setBoard(board);

       // play.makeMove(move);

      //  board = play.getBoard();




        return "move made";
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
                    - Make move: "move" <startcol> <start row> <endcol> <endrow>
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
