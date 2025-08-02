package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    ChessGame game;
    String errorMessage;
    String message;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, ChessGame game) {

        this.serverMessageType = type;
        this.game=game;
    }


    public ServerMessage(String errorMessage) {

        //this.serverMessageType = type;
        this.errorMessage=errorMessage;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public void setServerMessageType(ServerMessageType t){
        this.serverMessageType=t;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage=errorMessage;
    }
    public void setMessage(String message){
        this.message=message;
    }


    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public ChessGame getGame(){return game;}

    public String getMessage(){
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
