package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import websocketmessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    // this might need to be an array list of  connections
    public ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {

        if(connections.get(gameID)!=null){
            // add to inner list
            connections.get(gameID).add(new Connection(gameID, session));
        }
        else {
            var connection = new Connection(gameID, session);
            ArrayList<Connection> sessionMesh = new ArrayList<>();


            sessionMesh.add(connection);
            connections.put(gameID, sessionMesh);
        }
    }

    public ConcurrentHashMap<Integer, ArrayList<Connection>> getConnections(){
        if(connections == null){
            return new ConcurrentHashMap<>();
        }
        else {
            return connections;
        }
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


    public void broadcastToOne(int excludeVisitorName, ServerMessage notification, Session session) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var all : connections.values()) {
            for(var c : all) {
                if (c.session.isOpen()) {
                    //c.send(notification.toString());
                    if (c.session == session) {
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










    public void broadcastToAll(int excludeVisitorName, ServerMessage notification, Session session) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var all : connections.values()) {
            for(var c : all) {
                if (c.session.isOpen()) {
                    //c.send(notification.toString());
                    if (c.session != session) {
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
