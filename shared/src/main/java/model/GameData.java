package model;
//
//import chess.ChessGame;
//
//public class GameData {
//    int gameID;
//    String whiteUsername;
//    String blackUsername;
//    String gameName;
//    ChessGame game;
//
//    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
//        this.gameID = gameID;
//        this.whiteUsername = whiteUsername;
//        this.blackUsername = blackUsername;
//        this.gameName = gameName;
//        this.game = game;
//    }
//
//}


import com.google.gson.Gson;
import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {



    public String toString() {
        return new Gson().toJson(this);
    }


}
