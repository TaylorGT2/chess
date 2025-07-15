import chess.*;

import exception.ResponseException;
import server.Server;

public class Main {
    public static void main(String[] args) throws ResponseException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        Server s = new Server();
        s.run(8080);

        //run(args[1]);

        // new Server testrun;
    }
}