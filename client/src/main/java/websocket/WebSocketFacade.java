package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocketmessages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException{
        try{
            url = url.replace("http","ws");
            URI socketURI = new URI(url+"/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message){
                    ServerMessage notification = new Gson().fromJson(message,ServerMessage.class);
                    notificationHandler.notify(notification);

                    if(notification.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME){
                        notificationHandler.loadGame(notification);
                    }




                }


            });

        } catch (URISyntaxException e) {
            throw new ResponseException(500,e.getMessage());
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){

    }

    public void connect(String authToken, int gameID) throws ResponseException{
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException{
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void leave(String authToken, int gameID) throws ResponseException{
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException{
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
