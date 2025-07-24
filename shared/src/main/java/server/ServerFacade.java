package server;

import com.google.gson.Gson;
import exception.ErrorResponse;
import exception.ResponseException;
import model.UserData;
import model.AuthData;
import model.GameData;
import reqres.LoginRequest;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public UserData addUser(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path,null, user, UserData.class);
    }

    public AuthData login(UserData check) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path,null, check, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException{
        var path = String.format("/session");
        this.makeRequest("DELETE", path, authToken, null, null);

    }

    public GameData createGame(GameData gg, String authToken) throws ResponseException{
        var path = "/game";
        return this.makeRequest("POST",path,authToken,gg,GameData.class);
    }



    public void deleteGame(int id) throws ResponseException {
        var path = String.format("/game/%s", id);
        this.makeRequest("DELETE", path, null, null,null);
    }

    public void deleteAllUsers() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null,null);
    }


    public GameData[] listGames(String authToken) throws ResponseException {
        var path = "/game";
        record ListGameResponse(GameData[] games) {
        }
        var response = this.makeRequest("GET", path, authToken,null, ListGameResponse.class);
        return response.games();
    }

    public void playGame(int gameID, String playerColor, String authToken) throws ResponseException{
        var path = "/game";


//        public record LoginRequest(
//                int gameID,
//                String playerColor){
//        }
//        record playGameReq()

        var play = new LoginRequest(gameID, playerColor);
        this.makeRequest("PUT", path, authToken,play, null);
    }



    public UserData[] listUsers() throws ResponseException {
        var path = "/session";
        record Listuser(UserData[] user) {
        }
        var response = this.makeRequest("GET", path, null,null, Listuser.class);
        return response.user();
    }






    private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            //writeBody(request, http);
            if(authToken!=null) {
                http.addRequestProperty("authorization", authToken);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
