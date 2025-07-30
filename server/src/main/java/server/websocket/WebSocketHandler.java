package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.MySqlDataGame;
import dataaccess.UserDAO;
import dataaccess.GameDao;

import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Timer;


import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.management.Notification;

import static javax.management.remote.JMXConnectorFactory.connect;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        try{
            // playing fast a loose
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            //String username = getUsername(command.getAuthToken());
            int gameID = command.getGameID();

            saveSession(command.getGameID(),session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session,gameID,CONNECT);
                case MAKE_MOVE -> makeMove(session, gameID, MAKE_MOVE);
                case LEAVE -> leaveGame(session,gameID, LEAVE);
                case RESIGN -> resign(session, gameID, RESIGN);
            }
        } catch (IOException | ResponseException e) {
            throw new RuntimeException(e);
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

    private void makeMove(Session session, int username, UserGameCommand.CommandType commandType) {

    }

    private void connect(Session session, int gameID, UserGameCommand.CommandType commandType) throws IOException, ResponseException {
        connections.add(gameID,session);
        var game = "abc";

        //GameDao check = new GameDao;
        //check.getGame(gameID);
        try {
            GameDao dataGameAccess = new MySqlDataGame();
            dataGameAccess.getGame(gameID);

            var notify = new ServerMessage(LOAD_GAME, game);
            connections.broadcast(gameID, notify);
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



