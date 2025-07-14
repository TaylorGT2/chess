package server;

import RequestResult.LoginRequest;
import com.google.gson.Gson;
import dataaccess.*;
import model.UserData;
import model.AuthData;
import model.GameData;
import service.GameService;
import spark.*;
import exception.ResponseException;
import service.UserService;
import service.AuthService;

import java.util.Map;

public class Server {
    //public Server(){}
    public UserDAO dataAccess = new MemoryDataAccess();
    public AuthDAO dataAuthAccess = new MemoryAuthDataAccess();
    public GameDao dataGameAccess = new MemoryGameDataAccess();

    // this might need to be private and final
    public UserService service = new UserService(dataAccess);

    public AuthService serviceAuth = new AuthService(dataAuthAccess);

    public GameService serviceGame = new GameService(dataGameAccess);

    public Server(UserService service) {
        this.service = service;
        //webSocketHandler = new WebSocketHandler();
    }

   public Server(){}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.delete("/db", this::deleteAllUsers);

        Spark.post("/game",this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game",this::listGames);
//
        Spark.delete("/session",this::logout);
        Spark.post("/session",this::login);



//        Spark.post("/user", new Route(){
//            public Object handle(RegisterRequest rr, RegisterResult rru) {
//
//            }
//        }

        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint



        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//    private void exceptionHandler(ResponseException ex, Request req, Response res) {
//        res.status(ex.StatusCode());
//        res.body(ex.toJson());
//    }


    private String register(Request req, Response res) throws ResponseException{
        var user = new Gson().fromJson(req.body(), UserData.class);
        user = service.adduser(user);

        var authToken = new Gson().fromJson(req.body(), AuthData.class);
        authToken = serviceAuth.createAuth(authToken);

        //return authtoken;
        res.body(new Gson().toJson(authToken));
        //return res;
        return new Gson().toJson(authToken);
        //return "hi, this is a test";
    }

    private String createGame(Request req, Response res) throws ResponseException{

        //var authToken = new Gson().fromJson(req.body(), AuthData.class);
        //var authToken = req.params("authorization:");
        var authToken = req.headers("Authorization");
        AuthData g = serviceAuth.getAuth(authToken);
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
        //var authToken = req.params("authorization:");
        var authToken = req.headers("Authorization");
        serviceAuth.getAuth(authToken);
        if(authToken!=null){


//            var listgames = serviceGame.listGames();
//            res.body(new Gson().toJson(listgames));
//            return new Gson().toJson(listgames);
            res.type("application/json");
            var list = serviceGame.listGames().toArray();
            res.body(new Gson().toJson(Map.of("games", list)));
            return new Gson().toJson(Map.of("games", list));
        }
//
//        res.type("application/json");
//        var list = serviceGame.listGames().toArray();
//        return new Gson().toJson(Map.of("game", list));
        return null;
    }



    private String login(Request req, Response res) throws ResponseException{

        var user = new Gson().fromJson(req.body(), UserData.class);

        //var id = String.parseString(req.params(":id"));
        String username = req.params(":username");
        String password = req.params(":password");

        var authToken = new Gson().fromJson(req.body(), AuthData.class);
        authToken = serviceAuth.createAuth(authToken);

         boolean legal = service.login(user);


        if(legal==true){
            res.body(new Gson().toJson(authToken));
            return new Gson().toJson(authToken);


        }

        //var pet = service.getPet(id);
        //var user = new Gson().fromJson(req.body(), UserData.class);
        return null;


    }

    private String joinGame(Request req, Response res) throws ResponseException{
        var authToken = req.headers("Authorization");
        AuthData auth = serviceAuth.getAuth(authToken);

        var gameTicket = req.body();
        var game2 = new Gson().fromJson(req.body(), LoginRequest.class);
        int gameID = game2.gameID();
        String playerColor = game2.playerColor();
        //var game3 = new Gson().toJson(req.body());

        //String playerColor = game3.getAsJsonObject().get("PlayerColor").getAsString();
        //String playerColor = game2.get("PlayerColor").getAsString();


        //var gameID = gameTicket.gameID();



        //String gameID = req.params(":gameID");
        //String playerColor = req.body("playerColor");



        //var gameID = Integer.parseInt(req.params("gameID"));
        if(auth!=null){
            String username = auth.username();
            serviceGame.joinGame(gameID,playerColor,username);

            res.body(new Gson().toJson(username));
            return "{}";
            //var body = new Gson().fromJson(req.body());

        }
        return null;
    }

    private String logout(Request req, Response res) throws ResponseException {

       // var authToken = req.params("authorization:");
        var authToken = req.headers("Authorization");

        serviceAuth.deleteAuth(authToken);
        return "{}";



    }


//    private Object clear(Request req, Response res) throws ResponseException{
//        var clear = new Gson().fromJson(req.body(), UserData.class);
//        user = service.adduser(user);
//        return new Gson().toJson(user);
//    }

    private Object deleteAllUsers(Request req, Response res) throws ResponseException {
        service.deleteAllUsers();
        serviceAuth.deleteAllUsers();
        serviceGame.clear();
        res.status(200);
        return "";
    }


    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }


//    private Object echoBody(Request req, Response res){
//        //var bodyObj = getBody(req, Map.class);
//
//        res.type("application/json");
//        return new Gson().toJson(bodyObj);
//    }


//    private static void createRoutes() {
//        Spark.get("/hello", (req, res) -> "Hello BYU!");
//    }

//    Spark.notFound((req, res) -> {
//        res.type("text/html");
//        return readFromClasspathDirectory("/public/404.html");
//    });
//
//    private static String readFromClasspathDirectory(String file) {
//        try {
//            InputStream inputStream =
//                    CustomNotFoundStaticFileServer2.class.getResourceAsStream(file);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            StringBuilder content = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append("\n");
//            }
//
//            return content.toString();
//        } catch (IOException e) {
//            return file + " file not found.";
//        }
//    }
}
