import chess.*;
import ui.Repl;

public class Main {
    public static void main(String[] args) {


        var serviceUrl = "http://localhost:8080";

        if (args.length == 1){
            serviceUrl = args[0];
        }

        Repl thing = new Repl(serviceUrl);
        thing.run();
        thing.notify(null);



        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}