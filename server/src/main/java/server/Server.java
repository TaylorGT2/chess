package server;

import reqres.LoginRequest;
import com.google.gson.Gson;
import dataaccess.*;
import model.UserData;
import model.AuthData;
import model.GameData;
import server.websocket.Connection;
import server.websocket.ConnectionManager;
import service.GameService;
import spark.*;
import exception.ResponseException;
import service.UserService;
import service.AuthService;

import server.websocket.WebSocketHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public WebSocketHandler webSocketHandler;
    public UserService service;// = new UserService(dataAccess);

    public AuthService serviceAuth;//= new AuthService(dataAuthAccess);

    public GameService serviceGame;//= new GameService(dataGameAccess);

    public ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    //ConnectionManager m;

    public Server(UserService service, AuthService serviceAuth, GameService serviceGame) {


            this.service = service;
            this.serviceAuth = serviceAuth;
            this.serviceGame = serviceGame;
            webSocketHandler = new WebSocketHandler(new ConnectionManager(connections));

    }

   public Server() {


       try {


           UserDAO dataAccess = new MySqlDataUser();

           AuthDAO dataAuthAccess = new MySqlDataAuth();
           GameDao dataGameAccess = new MySqlDataGame();



           this.service = new UserService(dataAccess);

           this.serviceAuth = new AuthService(dataAuthAccess);

           this.serviceGame = new GameService(dataGameAccess);

           webSocketHandler = new WebSocketHandler(new ConnectionManager(connections));

       }catch(Exception ex){

       }


   }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.delete("/db", this::deleteAllUsers);

        Spark.post("/game",this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game",this::listGames);

        Spark.delete("/session",this::logout);
        Spark.post("/session",this::login);


        //Spark.webSocket("/ws", WebSocketHandler.class);





        Spark.exception(ResponseException.class, this::exceptionHandler);





        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int port() {
        return Spark.port();
    }




    private String register(Request req, Response res) throws ResponseException{
        var user = new Gson().fromJson(req.body(), UserData.class);
        if(user.email()==null||user.password()==null||user.username()==null){
            throw new ResponseException(400, "Error: bad request");
        }
        user = service.adduser(user);

        var authToken = new Gson().fromJson(req.body(), AuthData.class);
        authToken = serviceAuth.createAuth(authToken);


        res.body(new Gson().toJson(authToken));

        return new Gson().toJson(authToken);

    }

    private String createGame(Request req, Response res) throws ResponseException{


        var authToken = req.headers("Authorization");
        AuthData g = serviceAuth.getAuth(authToken);

        if(g==null){
            throw new ResponseException(401,"Error: unauthorized");
        }

        if(g!=null || g==null){
            var game = new Gson().fromJson(req.body(), GameData.class);
            String realgame = game.gameName();

            GameData test = serviceGame.createGame(realgame);

            res.body(new Gson().toJson(test));
            return new Gson().toJson(test);

        }

        return null;


    }


    private String listGames(Request req, Response res) throws ResponseException{

        var authToken = req.headers("Authorization");
        AuthData a = serviceAuth.getAuth(authToken);
        if(a!=null){



            res.type("application/json");
            var list = serviceGame.listGames().toArray();
            res.body(new Gson().toJson(Map.of("games", list)));
            return new Gson().toJson(Map.of("games", list));
        }
        else{
            throw new ResponseException(401, "Error: Unauthorized");
        }

    }



    private String login(Request req, Response res) throws ResponseException{

        var user = new Gson().fromJson(req.body(), UserData.class);


        String username = req.params(":username");
        String password = req.params(":password");

        var authToken = new Gson().fromJson(req.body(), AuthData.class);
        authToken = serviceAuth.createAuth(authToken);

        if(user.password()==null||user.username()==null){
            throw new ResponseException(400, "Error: bad request");
        }

        boolean legal = service.login(user);


        if(legal==true){
            res.body(new Gson().toJson(authToken));
            return new Gson().toJson(authToken);


        }


        else{
            throw new ResponseException(401, "Error: unauthorized");
        }


    }

    private String joinGame(Request req, Response res) throws ResponseException{
        var authToken = req.headers("Authorization");
        AuthData auth = serviceAuth.getAuth(authToken);


        var gameTicket = req.body();
        var game2 = new Gson().fromJson(req.body(), LoginRequest.class);
        int gameID = game2.gameID();

        //Session session;

        //URI socketURI = new URI(url+"/ws");
//        this.notificationHandler = notificationHandler;
//
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//
//        this.session = container.connectToServer(this,socketURI);





        Integer idCheck = game2.gameID();
        if(idCheck==null){
            throw new ResponseException(400,"Error: bad request");
        }
        String playerColor = game2.playerColor();



        if(auth!=null){
            if(playerColor==null){
                throw new ResponseException(400,"Error: bad request");
            }
            if(playerColor.equalsIgnoreCase("WHITE")||playerColor.equalsIgnoreCase("BLACK")) {
                String username = auth.username();
                playerColor = playerColor.toUpperCase();
                serviceGame.joinGame(gameID, playerColor, username);

                res.body(new Gson().toJson(username));
                return "{}";
            }
            else{
                throw new ResponseException(400,"Error: bad request");
            }


        }
        else{
            throw new ResponseException(401,"Error:Unauthorized");
        }
    }

    private String logout(Request req, Response res) throws ResponseException {

       // var authToken = req.params("authorization:");
        var authToken = req.headers("Authorization");

        serviceAuth.deleteAuth(authToken);
        return "{}";



    }




    private Object deleteAllUsers(Request req, Response res) throws ResponseException {
        service.deleteAllUsers();
        serviceAuth.deleteAllUsers();
        serviceGame.clear();
        res.status(200);
        return "";
    }


    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }


}
