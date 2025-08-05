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


import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static javax.management.remote.JMXConnectorFactory.connect;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {

    public ConnectionManager connections;
    public boolean resignation = false;

    public int legalMove = 0;

    public WebSocketHandler(ConnectionManager connections){
        this.connections=connections;
    }



    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException, ResponseException, InvalidMoveException {
        UserGameCommand commandCheck = new Gson().fromJson(msg, UserGameCommand.class);

        int b = connections.connections.size();




        if(commandCheck.getCommandType()==MAKE_MOVE){





            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            String testToken = command.getAuthToken();
            int gameID = command.getGameID();


            GameDao observerCheck = new MySqlDataGame();
            GameData checking = observerCheck.getGame(gameID);

            if(checking.gameName().equals("RERERESIGNED")){
                var notify = new ServerMessage(ERROR, null);
                notify.setErrorMessage("errorMessage");
                connections.broadcastToOne(gameID, notify,session);

                return;
            }

            boolean shutdown = false;


            AuthDAO dataAccess = new MySqlDataAuth();
            AuthData test = dataAccess.getAuth(testToken);
            if (test == null) {

                String error = "this is an error";
                var notify = new ServerMessage(ERROR, null);
                notify.setErrorMessage("errorMessage");

                connections.errorBroadcast(session, notify);
                shutdown = true;

            }

            else {


                makeMove(session, test.username(), commandCheck.getGameID(), commandCheck.getMove(), command);
            }
        }
        else {


            try {

                UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

                String testToken = command.getAuthToken();
                int gameID = command.getGameID();

                boolean shutdown = false;


                try {
                    AuthDAO dataAccess = new MySqlDataAuth();
                    AuthData test = dataAccess.getAuth(testToken);
                    if (test == null) {
                        saveSession(command.getGameID(), session);
                        String error = "this is an error";
                        var notify = new ServerMessage(ERROR, null);
                        notify.setErrorMessage("errorMessage");

                        connections.errorBroadcast(session, notify);
                        shutdown = true;

                    }
                } catch (ResponseException e) {

                    String error = "this is an error";
                    var notify = new ServerMessage(ERROR, null);
                    notify.setErrorMessage("errorMessage");
                    connections.errorBroadcast(session, notify);



                }


                if (shutdown == false) {

                    AuthDAO dataAccess = new MySqlDataAuth();
                    AuthData test = dataAccess.getAuth(testToken);


                    String playerDidSomething = test.username();


                    switch (command.getCommandType()) {
                        case CONNECT -> connect(session, gameID, command);
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

            connections.errorBroadcast(session, errorNote);
        }
        else if(checking.gameName().equals("RERERESIGNED")){
            var error2 = "Only one quitter per game";
            var errorNote = new ServerMessage(ERROR,null);
            errorNote.setErrorMessage(error2);

            connections.errorBroadcast(session, errorNote);
        }
        else {


            var finished = String.format("%s resigned", test.username());
            var notify = new ServerMessage(NOTIFICATION, null);
            notify.setMessage(finished);
            connections.broadcastToOne(gameID, notify, session);
            connections.broadcastToAll(gameID, notify, session, gameID);
            resignation = true;
            observerCheck.deleteGame(gameID);
            observerCheck.updateGame(checking.whiteUsername(),gameID, "RERERESIGNED", checking.blackUsername(), checking.game());
        }

    }

    private void leaveGame(Session session, int gameID, UserGameCommand commandType) throws IOException, ResponseException {
        String testToken = commandType.getAuthToken();
        AuthDAO dataAccess = new MySqlDataAuth();
        AuthData test = dataAccess.getAuth(testToken);
        String user = test.username();


        GameDao gameChange = new MySqlDataGame();
        GameData game = gameChange.getGame(gameID);


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

    private void makeMove(Session session, String username, int gameID,
                          ChessMove move, UserGameCommand commandType) throws IOException, ResponseException, InvalidMoveException {


        String error = String.format("user %s made a move", username);



        GameDao dataGameAccess = new MySqlDataGame();
        GameData test = dataGameAccess.getGame(gameID);

        ChessGame change = test.game();

        ChessPosition empty = move.getStartPosition();

        if(change.getBoard().getPiece(empty)==null){
            var error2 = "not a good move error";
            var errorNote = new ServerMessage(ERROR,null);
            errorNote.setErrorMessage(error2);

            connections.errorBroadcast(session, errorNote);
            //return;
        }
        else {

            Collection<ChessMove> goodMoves = change.validMoves(move.getStartPosition());

            if (!goodMoves.contains(move)) {
                var error2 = "not a good move error";
                var errorNote = new ServerMessage(ERROR, null);
                errorNote.setErrorMessage(error2);

                connections.errorBroadcast(session, errorNote);
            } else {

                String userExtract = commandType.getAuthToken();
                AuthDAO dataAccess = new MySqlDataAuth();
                AuthData userNameHolder = dataAccess.getAuth(userExtract);
                String usernameColor = userNameHolder.username();

                boolean shutdown = false;

                if (usernameColor.equals(test.blackUsername()) || usernameColor.equals(test.whiteUsername())) {
                    // its blacks turn
                    ChessBoard b = change.getBoard();
                    ChessPiece color = b.getPiece(new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn()));
                    ChessGame.TeamColor trueColor = BLACK;
                    if (usernameColor.equals(test.blackUsername())) {
                        trueColor = BLACK;
                    } else {
                        trueColor = WHITE;
                    }


                    if (color.getTeamColor() != trueColor) {
                        var error2 = "its not your turn error";
                        var errorNote = new ServerMessage(ERROR, null);
                        errorNote.setErrorMessage(error2);

                        connections.errorBroadcast(session, errorNote);
                        shutdown = true;
                    } else if (usernameColor.equals(test.blackUsername()) && change.getTurnNumber() % 2 == 0) {
                        var error2 = "its not your turn error";
                        var errorNote = new ServerMessage(ERROR, null);
                        errorNote.setErrorMessage(error2);

                        connections.errorBroadcast(session, errorNote);
                        shutdown = true;
                    } else if (usernameColor.equals(test.whiteUsername()) && change.getTurnNumber() % 2 != 0) {

                        var error2 = "its not your turn error";
                        var errorNote = new ServerMessage(ERROR, null);
                        errorNote.setErrorMessage(error2);

                        connections.errorBroadcast(session, errorNote);
                        shutdown = true;
                    }


                }



                else {

                    var error2 = "its not your turn error";
                    var errorNote = new ServerMessage(ERROR, null);
                    errorNote.setErrorMessage(error2);

                    connections.errorBroadcast(session, errorNote);
                    shutdown = true;


                }


                if (shutdown == false) {


                    change.makeMove(move);

                    ((MySqlDataGame) dataGameAccess).deleteGame(gameID);

                    change.setTurnNumber(change.getTurnNumber() + 1);

                    dataGameAccess.updateGame(test.whiteUsername(), test.gameID(), test.gameName(), test.blackUsername(), change);

                    var game = new Gson().toJson(dataGameAccess.getGame(gameID));

                    GameData full = dataGameAccess.getGame(gameID);

                    ChessGame g = full.game();

                    String message = String.format("%s made move: %s", username, move.toString());

                    if (g.isInCheck(BLACK) || g.isInCheck(WHITE)) {
                        message = message + " and check";

                    }
                    if (g.isInCheckmate(BLACK) || g.isInCheckmate(WHITE)) {
                        message = message + " and checkmate";

                    }
                    if (g.isInStalemate(BLACK) || g.isInStalemate(WHITE)) {
                        message = message + " and stalemate";

                    }


                    var notify = new ServerMessage(LOAD_GAME, g);
                    connections.broadcastToOne(gameID, notify, session);
                    connections.broadcastToAll(gameID, notify, session, gameID);


                    var notify2 = new ServerMessage(NOTIFICATION, null);
                    notify2.setMessage(message);
                    connections.broadcastToAll(gameID, notify2, session, gameID);


                    legalMove += 1;
                }
            }
        }



    }

    private void connect(Session session, int gameID, UserGameCommand commandType) throws IOException, ResponseException {
        connections.add(gameID,session);
        var game = "abc";


        String token = commandType.getAuthToken();

        AuthDAO dataAccess = new MySqlDataAuth();
        AuthData userhome = dataAccess.getAuth(token);

        String username = userhome.username();

        String title = "observer";



        GameDao dataGameAccess = new MySqlDataGame();
        GameData test = dataGameAccess.getGame(gameID);
        ChessGame g = new ChessGame();





        if(test!=null) {
            g = test.game();

            if(username.equals(test.whiteUsername())){
                title = "white";
            }
            if(username.equals(test.blackUsername())){
                title = "black";
            }



        }



        try {


            if(test==null){
                String error = "this is an error";
                var notify = new ServerMessage(ERROR, null);
                notify.setErrorMessage("errorMessage");
                connections.errorBroadcast(session, notify);

            }

            else {


                String fullmess = String.format("%s has joined the game as %s", username, title);
                var notify = new ServerMessage(LOAD_GAME, g);
                connections.broadcastToOne(gameID, notify, session);
                var notifyReal = new ServerMessage(NOTIFICATION, null);
                notifyReal.setMessage(fullmess);
                connections.broadcastToAll(gameID,notifyReal, session, gameID);

            }
        } catch (Exception e) {
            var notify = new ServerMessage(ERROR, null);
            connections.errorBroadcast(session, notify);

        }
    }

    private void saveSession(Integer gameID, Session session) {

        connections.add(gameID,session);

    }







}



