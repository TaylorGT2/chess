package ui;



import chess.ChessBoard;
import chess.ChessGame;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;
import websocketmessages.Notification;



import java.util.Scanner;

public class Repl implements NotificationHandler {
    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print("\u001b[" + "34m" + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage notification) {
        System.out.println("\u001b[" + "31m" + "testing testing");

        printPrompt();
    }

    public void loadGame(ServerMessage notification){

        ChessGame load = notification.getGame();

        ChessBoard b = load.getBoard();

        client.board = b;

        if(client.color.equals("black")){
            client.makeBlack();
        }
        else{
            client.makeBoard();
        }

        System.out.println("new board");

        printPrompt();


    }


    private void printPrompt() {
        System.out.print("\n" + "\u001b[" + ">>> " + "\u001b[" + "32m");
    }

}