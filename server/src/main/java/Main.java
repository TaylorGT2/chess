import chess.*;

import dataaccess.*;
import exception.ResponseException;
import server.Server;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) throws ResponseException {
        try {
            var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            System.out.println("♕ 240 Chess Server: " + piece);


            UserDAO dataUser = new MemoryDataAccess();
            AuthDAO dataAuth = new MemoryAuthDataAccess();
            GameDao dataGame = new MemoryGameDataAccess();


            dataUser = new MySqlDataUser();
            dataAuth = new MySqlDataAuth();
            dataGame = new MySqlDataGame();

            if (args.length >= 2 && args[1].equals("sql")) {
                dataUser = new MySqlDataUser();
                dataAuth = new MySqlDataAuth();
                dataGame = new MySqlDataGame();
            }
            var userSer = new UserService(dataUser);

            var authSer = new AuthService(dataAuth);
            var gameSer = new GameService(dataGame);
            Server all = new Server(userSer, authSer, gameSer);



            Server s = new Server();
            all.run(8080);
        }catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }


    }
}