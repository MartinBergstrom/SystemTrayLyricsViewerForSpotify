import http.MyHttpClient;
import server.SimpleServer;
import spotifyApi.SpotifyApiHandler;

public class Launcher {

    public static void main(String[] args) throws Exception {
        SpotifyApiHandler spotifyApiHandler = new SpotifyApiHandler(new MyHttpClient());
        new Thread(() -> new SimpleServer(spotifyApiHandler::handleServerEvent)).start();
    }

}
