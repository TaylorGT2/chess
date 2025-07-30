package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import websocketmessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    // this might need to be an array list of  connections
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        var connection = new Connection(gameID, session);
        ArrayList<Connection> sessionMesh = new ArrayList<>();
        sessionMesh.add(connection);
        connections.put(gameID, sessionMesh);
    }

    public void remove(int visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(int excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var all : connections.values()) {
            for(var c : all) {
                if (c.session.isOpen()) {
                    c.send(notification.toString());
                    if (c.gameID != excludeVisitorName) {
                        c.send(notification.toString());
                    }
                } else {
                    removeList.add(c);
                }
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.gameID);
        }
    }
}
