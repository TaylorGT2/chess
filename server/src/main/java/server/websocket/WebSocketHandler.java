package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.MySqlDataAuth;
import dataaccess.MySqlDataGame;
import dataaccess.GameDao;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;


import static javax.management.remote.JMXConnectorFactory.connect;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {
    //public final ConnectionManager connections = new ConnectionManager();// = ConnectionManager.connections;
    public ConnectionManager connections;
    public boolean resignation = false;

    public int legalMove = 0;

    public WebSocketHandler(ConnectionManager connections){
        this.connections=connections;
    }

    //private final ConnectionManager connectionsALl = connections.getConnections();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException, ResponseException, InvalidMoveException {
        UserGameCommand commandCheck = new Gson().fromJson(msg, UserGameCommand.class);

        int b = connections.connections.size();

        //connections = new ConnectionManager();


        if(commandCheck.getCommandType()==MAKE_MOVE){





            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            String testToken = command.getAuthToken();
            int gameID = command.getGameID();
            if(resignation==true){
                var notify = new ServerMessage(ERROR, null);
                notify.setErrorMessage("errorMessage");
                connections.broadcastToOne(gameID, notify,session);
                return;
            }

            boolean shutdown = false;
            //saveSession(command.getGameID(), session);

            AuthDAO dataAccess = new MySqlDataAuth();
            AuthData test = dataAccess.getAuth(testToken);
            if (test == null) {
                //saveSession(command.getGameID(), session);
                //connections.add(gameID,session);
                String error = "this is an error";
                var notify = new ServerMessage(ERROR, null);
                notify.setErrorMessage("errorMessage");
                connections.broadcastToOne(gameID, notify,session);
                shutdown = true;

            }

            else {


                makeMove(session, "test", commandCheck.getGameID(), commandCheck.getMove(), command);
            }
        }
        else {


            try {
                // playing fast a loose
                UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

                String testToken = command.getAuthToken();
                int gameID = command.getGameID();

                boolean shutdown = false;
                //saveSession(command.getGameID(), session);

                try {
                    AuthDAO dataAccess = new MySqlDataAuth();
                    AuthData test = dataAccess.getAuth(testToken);
                    if (test == null) {
                        saveSession(command.getGameID(), session);
                        String error = "this is an error";
                        var notify = new ServerMessage(ERROR, null);
                        notify.setErrorMessage("errorMessage");
                        connections.broadcastToOne(gameID, notify,session);
                        shutdown = true;

                    }
                } catch (ResponseException e) {

                    String error = "this is an error";
                    var notify = new ServerMessage(ERROR, null);
                    notify.setErrorMessage("errorMessage");
                    connections.broadcastToOne(gameID, notify, session);



                }

                //String username = getUsername(command.getAuthToken());
                //int gameID = command.getGameID();


                //saveSession(command.getGameID(),session);

                if (shutdown == false) {

                    AuthDAO dataAccess = new MySqlDataAuth();
                    AuthData test = dataAccess.getAuth(testToken);


                    String playerDidSomething = test.username();


                    switch (command.getCommandType()) {
                        case CONNECT -> connect(session, gameID, CONNECT);
                        case MAKE_MOVE -> makeMove(session, playerDidSomething, gameID, command.getMove(), command);
                        case LEAVE -> leaveGame(session, gameID, command);
                        case RESIGN -> resign(session, gameID, command);
                    }
                }
            } catch (IOException | ResponseException | InvalidMoveException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void resign(Session session, int gameID, UserGameCommand commandType) throws ResponseException, IOException {

        String testToken = commandType.getAuthToken();
        AuthDAO dataAccess = new MySqlDataAuth();
        AuthData test = dataAccess.getAuth(testToken);
        String oberserving = test.username();

        GameDao observerCheck = new MySqlDataGame();
        GameData checking = observerCheck.getGame(gameID);

        if(!(checking.whiteUsername().equals(oberserving)) && !(checking.blackUsername().equals(oberserving))){
            var error2 = "Observers aren't players so they can't be quitters";
            var errorNote = new ServerMessage(ERROR,null);
            errorNote.setErrorMessage(error2);
            connections.broadcastToOne(gameID, errorNote, session);
        }
        else if(resignation==true){
            var error2 = "Only one quitter per game";
            var errorNote = new ServerMessage(ERROR,null);
            errorNote.setErrorMessage(error2);
            connections.broadcastToOne(gameID, errorNote, session);
        }
        else {


            var finished = String.format("%s resigned", test.username());
            var notify = new ServerMessage(NOTIFICATION, null);
            notify.setMessage(finished);
            connections.broadcastToOne(gameID, notify, session);
            connections.broadcastToAll(gameID, notify, session, gameID);
            resignation = true;
        }

    }

    private void leaveGame(Session session, int gameID, UserGameCommand commandType) throws IOException, ResponseException {
        String testToken = commandType.getAuthToken();
        AuthDAO dataAccess = new MySqlDataAuth();
        AuthData test = dataAccess.getAuth(testToken);
        String user = test.username();


        GameDao gameChange = new MySqlDataGame();
        GameData game = gameChange.getGame(gameID);

//        if(user.equals(game.blackUsername())){
//            gameChange.updateGame(game.whiteUsername(),gameID, game.gameName(), null, game.game());
//        }
//        if(user.equals(game.whiteUsername())){
//            gameChange.updateGame(null,gameID, game.gameName(), game.blackUsername(), game.game());
//        }



        //dataGameAccess.updateGame(test.whiteUsername(), test.gameID(), test.gameName(), test.blackUsername(), change);

        connections.remove(gameID, session);
        var message = String.format("%s is gone", test.username());
        var notify = new ServerMessage(NOTIFICATION, null);
        notify.setMessage(message);
        connections.broadcastToAll(gameID,notify,session, gameID);

        gameChange.deleteGame(gameID);
        if(user.equals(game.blackUsername())){
            gameChange.updateGame(game.whiteUsername(),gameID, game.gameName(), null, game.game());
        }
        if(user.equals(game.whiteUsername())){
            gameChange.updateGame(null,gameID, game.gameName(), game.blackUsername(), game.game());
        }

    }

    private void makeMove(Session session, String username, int gameID, ChessMove move, UserGameCommand commandType) throws IOException, ResponseException, InvalidMoveException {

        //connections.add(gameID,session);
        String error = String.format("user %s made a move", username);



        GameDao dataGameAccess = new MySqlDataGame();
        GameData test = dataGameAccess.getGame(gameID);

        ChessGame change = test.game();

        Collection<ChessMove> goodMoves = change.validMoves(move.getStartPosition());

        if(!goodMoves.contains(move)){
            var error2 = "not a good move error";
            var errorNote = new ServerMessage(ERROR,null);
            errorNote.setErrorMessage(error2);
            connections.broadcastToOne(gameID, errorNote, session);
        }
        else {

            String userExtract = commandType.getAuthToken();
            AuthDAO dataAccess = new MySqlDataAuth();
            AuthData userNameHolder = dataAccess.getAuth(userExtract);
            String usernameColor = userNameHolder.username();

            boolean shutdown = false;
            if(test.blackUsername().equals(usernameColor) || test.whiteUsername().equals(usernameColor)){
                // its blacks turn
                ChessBoard b = change.getBoard();
                ChessPiece color = b.getPiece(new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()));
                ChessGame.TeamColor trueColor = ChessGame.TeamColor.BLACK;
                if(test.blackUsername().equals(usernameColor)){
                    trueColor = ChessGame.TeamColor.BLACK;
                }
                else{
                    trueColor = ChessGame.TeamColor.WHITE;
                }


                if(color.getTeamColor() != trueColor){
                    var error2 = "its not your turn error";
                    var errorNote = new ServerMessage(ERROR,null);
                    errorNote.setErrorMessage(error2);
                    connections.broadcastToOne(gameID, errorNote, session);
                    shutdown = true;
                }


//                else if(test.blackUsername().equals(usernameColor)&&legalMove%2==0){
//                    var error2 = "its not your turn error";
//                    var errorNote = new ServerMessage(ERROR,null);
//                    errorNote.setErrorMessage(error2);
//                    connections.broadcastToOne(gameID, errorNote, session);
//                    shutdown = true;
//                }
//                else if(test.whiteUsername().equals(usernameColor)&&legalMove%2!=0){
//
//                    var error2 = "its not your turn error";
//                    var errorNote = new ServerMessage(ERROR,null);
//                    errorNote.setErrorMessage(error2);
//                    connections.broadcastToOne(gameID, errorNote, session);
//                    shutdown = true;
//                }


            }


//            else if(test.whiteUsername().equals(usernameColor)){
//                // its whites turn
//                ChessBoard b = change.getBoard();
//                ChessPiece color = b.getPiece(new ChessPosition(move.getStartPosition().getRow(),move.getStartPosition().getColumn()));
//                if(color.getTeamColor()!= ChessGame.TeamColor.WHITE){
//                    var error2 = "its not your turn error";
//                    var errorNote = new ServerMessage(ERROR,null);
//                    errorNote.setErrorMessage(error2);
//                    connections.broadcastToOne(gameID, errorNote, session);
//                    shutdown = true;
//                }
//            }
            else{
                //its no ones turn throw an error
                var error2 = "its not your turn error";
                var errorNote = new ServerMessage(ERROR,null);
                errorNote.setErrorMessage(error2);
                connections.broadcastToOne(gameID, errorNote, session);
                shutdown = true;


            }



            if(shutdown==false) {


                change.makeMove(move);

                ((MySqlDataGame) dataGameAccess).deleteGame(gameID);

                dataGameAccess.updateGame(test.whiteUsername(), test.gameID(), test.gameName(), test.blackUsername(), change);

                var game = new Gson().toJson(dataGameAccess.getGame(gameID));

                GameData full = dataGameAccess.getGame(gameID);

                ChessGame g = full.game();
                //game = dataGameAccess.getGame(gameID).game();
                var notify = new ServerMessage(LOAD_GAME, g);
                connections.broadcastToOne(gameID, notify, session);
                connections.broadcastToAll(gameID, notify, session, gameID);


                var notify2 = new ServerMessage(NOTIFICATION, null);
                notify2.setMessage("Message");
                connections.broadcastToAll(gameID, notify2, session, gameID);


                legalMove+=1;
            }
        }



    }

    private void connect(Session session, int gameID, UserGameCommand.CommandType commandType) throws IOException, ResponseException {
        connections.add(gameID,session);
        var game = "abc";

        //GameDao check = new GameDao;
        //check.getGame(gameID);

        //if()


        GameDao dataGameAccess = new MySqlDataGame();
        GameData test = dataGameAccess.getGame(gameID);
        ChessGame g = new ChessGame();
        if(test!=null) {
            g = test.game();
        }



        try {
            //GameDao dataGameAccess = new MySqlDataGame();
            //GameData test = dataGameAccess.getGame(gameID);

            if(test==null){
                String error = "this is an error";
                var notify = new ServerMessage(ERROR, null);
                notify.setErrorMessage("errorMessage");
                connections.broadcastToOne(gameID, notify,session);
            }

            else {

                var notify = new ServerMessage(LOAD_GAME, g);
                connections.broadcastToOne(gameID, notify, session);
                var notifyReal = new ServerMessage(NOTIFICATION, null);
                notifyReal.setMessage("this is a test message");
                connections.broadcastToAll(gameID,notifyReal, session, gameID);

            }
        } catch (Exception e) {
            var notify = new ServerMessage(ERROR, null);
            connections.broadcastToOne(gameID, notify, session);
        }
    }
    // This might have something to do with loadinig the games...
    private void saveSession(Integer gameID, Session session) {

        connections.add(gameID,session);

    }


    public void connectTest(int gameID) throws ResponseException {
        try {
            var game = String.format("joined");
            //var notification = new ServerMessage(NOTIFICATION, "connectTest");
            //var load = new ServerMessage(LOAD_GAME, game);
            //connections.add(gameID,session);
            //connections.add
            //connections.broadcast(gameID, load);
            //connections.broadcast(gameID, notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }




}



