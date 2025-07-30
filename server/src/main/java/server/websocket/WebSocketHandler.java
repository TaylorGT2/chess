package server.websocket;

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


import static javax.management.remote.JMXConnectorFactory.connect;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {
    public final ConnectionManager connections = new ConnectionManager();// = ConnectionManager.connections;

    //private final ConnectionManager connectionsALl = connections.getConnections();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        UserGameCommand commandCheck = new Gson().fromJson(msg, UserGameCommand.class);
        if(commandCheck.getCommandType()==MAKE_MOVE){
            makeMove(session, "testUserBob", commandCheck.getGameID(), MAKE_MOVE);
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
                        String error = "this is an error";
                        var notify = new ServerMessage(ERROR, null);
                        notify.setErrorMessage("errorMessage");
                        connections.broadcast(gameID, notify);
                        shutdown = true;
                    }
                } catch (ResponseException e) {

                    String error = "this is an error";
                    var notify = new ServerMessage(ERROR, null);
                    notify.setErrorMessage("errorMessage");
                    connections.broadcast(gameID, notify);

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
                        case MAKE_MOVE -> makeMove(session, playerDidSomething, gameID, MAKE_MOVE);
                        case LEAVE -> leaveGame(session, gameID, LEAVE);
                        case RESIGN -> resign(session, gameID, RESIGN);
                    }
                }
            } catch (IOException | ResponseException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void resign(Session session, int username, UserGameCommand.CommandType commandType) {
    }

    private void leaveGame(Session session, int gameID, UserGameCommand.CommandType commandType) throws IOException {
        connections.remove(gameID);
        var message = String.format("is gone");
        var notify = new ServerMessage(NOTIFICATION, "a user left");
        connections.broadcast(gameID,notify);
    }

    private void makeMove(Session session, String username, int gameID, UserGameCommand.CommandType commandType) throws IOException {

        connections.add(gameID,session);
        String error = String.format("user %s made a move", username);
        var notify = new ServerMessage(NOTIFICATION, null);
        notify.setErrorMessage("errorMessage");
        connections.broadcast(gameID, notify);



    }

    private void connect(Session session, int gameID, UserGameCommand.CommandType commandType) throws IOException, ResponseException {
        connections.add(gameID,session);
        var game = "abc";

        //GameDao check = new GameDao;
        //check.getGame(gameID);

        //if()



        try {
            GameDao dataGameAccess = new MySqlDataGame();
            GameData test = dataGameAccess.getGame(gameID);

            if(test==null){
                String error = "this is an error";
                var notify = new ServerMessage(ERROR, null);
                notify.setErrorMessage("errorMessage");
                connections.broadcast(gameID, notify);
            }

            else {

                var notify = new ServerMessage(LOAD_GAME, game);
                connections.broadcastToOne(gameID, notify, session);
                var notifyReal = new ServerMessage(NOTIFICATION, null);
                notifyReal.setMessage("this is a test message");
                connections.broadcastToAll(gameID,notifyReal, session);

            }
        } catch (ResponseException e) {
            var notify = new ServerMessage(ERROR, game);
            connections.broadcast(gameID, notify);
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
            var load = new ServerMessage(LOAD_GAME, game);
            //connections.add(gameID,session);
            //connections.add
            connections.broadcast(gameID, load);
            //connections.broadcast(gameID, notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }




}



