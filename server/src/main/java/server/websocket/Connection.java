package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public int gameID;
    public Session session;

    public Connection(int visitorName, Session session) {
        this.gameID = visitorName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
