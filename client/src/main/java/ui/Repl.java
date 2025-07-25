package ui;



import websocket.NotificationHandler;
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

    public void notify(Notification notification) {
        System.out.println("\u001b[" + "31m" + notification.message());

        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + "\u001b[" + ">>> " + "\u001b[" + "32m");
    }

}