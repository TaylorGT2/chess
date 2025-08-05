package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.Client;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocketmessages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class WebSocketFacade extends Endpoint{
    public Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler, Client client) throws ResponseException{
        try{
            url = url.replace("http","ws");
            URI socketURI = new URI(url+"/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();



            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message){

                    //System.out.println(message);
                    System.out.println("A message was received");
                    ServerMessage notification = new Gson().fromJson(message,ServerMessage.class);


                    if(notification.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME){
                        //notificationHandler.loadGame(notification);

                        ChessGame load = notification.getGame();

                        ChessBoard b = load.getBoard();

                        client.board = b;

                        if(client.color.equals("black")){
                            client.makeBlack();
                        }
                        else{
                            client.makeBoard();
                        }

                        System.out.print(SET_TEXT_COLOR_GREEN);

                        System.out.println("new board");

                        printPrompt();



                    }
                    if(notification.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION){

                        System.out.print(SET_TEXT_COLOR_GREEN);

                        System.out.println("Incoming super important message!!!!");
                        System.out.println(notification.getMessage());
                        System.out.println("Message was sent!!");

                    }
                    if(notification.getServerMessageType()== ServerMessage.ServerMessageType.ERROR){

                        System.out.print(SET_TEXT_COLOR_GREEN);

                        System.out.println("Incoming super important ERROR ERROR ERROR message!!!!");
                        System.out.println(notification.getErrorMessage());
                        System.out.println("Message was sent!!");

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

    private void printPrompt() {
        System.out.print("\n" + "\u001b[" + ">>> " + "\u001b[" + "32m");
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
