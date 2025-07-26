package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
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
import static websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        try{
            // playing fast a loose
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(),session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session,username,CONNECT);
                case MAKE_MOVE -> makeMove(session, username, MAKE_MOVE);
                case LEAVE -> leaveGame(session,username, LEAVE);
                case RESIGN -> resign(session, username, RESIGN);
            }
        }
    }

    private void resign(Session session, String username, UserGameCommand.CommandType commandType) {
    }

    private void leaveGame(Session session, String username, UserGameCommand.CommandType commandType) {

    }

    private void makeMove(Session session, String username, UserGameCommand.CommandType commandType) {

    }

    private void connect(Session session, String username, UserGameCommand.CommandType commandType) throws IOException{
        connections.add(username,session);
        var message = String.format("%s is in the game",username);
        var notify = new ServerMessage(NOTIFICATION);
        connections.broadcast(username,notify);
    }
    // This might have something to do with loadinig the games...
    private void saveSession(Integer gameID, Session session) {

    }

    private String getUsername(String authToken) {
        // this is a filler until I can understand
        AuthDAO b;
        AuthData c = b.getAuth(authToken);
        return c.username();


    }


}


//
//package server.websocket;
//
//import com.google.gson.Gson;
//import dataaccess.DataAccess;
//import exception.ResponseException;
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
//import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
//
//import java.io.IOException;
//import java.util.Timer;
//
//
//@WebSocket
//public class WebSocketHandler {
//
//    private final ConnectionManager connections = new ConnectionManager();
//
//    @OnWebSocketMessage
//    public void onMessage(Session session, String message) throws IOException {
//        Action action = new Gson().fromJson(message, Action.class);
//        switch (action.type()) {
//            case ENTER -> enter(action.visitorName(), session);
//            case EXIT -> exit(action.visitorName());
//        }
//    }
//
//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//}
