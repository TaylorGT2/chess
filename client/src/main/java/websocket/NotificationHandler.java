package websocket;

import websocket.messages.ServerMessage;
import websocketmessages.Notification;

public interface NotificationHandler {
    void notify(ServerMessage notification);

    //void loadgame(ServerMessage notification);

    void loadGame(ServerMessage notification);
}


