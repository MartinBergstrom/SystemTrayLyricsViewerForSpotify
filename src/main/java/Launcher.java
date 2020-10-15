import http.MyHttpClient;
import server.SimpleServer;
import spotifyApi.SpotifyApiInitalizer;

public class Launcher {

    public static void main(String[] args) throws Exception {

        SpotifyApiInitalizer spotifyApiInitalizer = new SpotifyApiInitalizer((new MyHttpClient()));

        new Thread(() -> new SimpleServer(spotifyApiInitalizer::handleServerEvent)).start();
    }

}
