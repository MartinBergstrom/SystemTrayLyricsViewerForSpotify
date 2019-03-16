import spotifyApi.SpotifyApiHandler;
import com.google.common.eventbus.EventBus;
import http.MyHttpClient;
import server.SimpleServer;

public class Laucher {
    public static void main(String[] args) throws Exception {
        EventBus eventBus = new EventBus();
        new Thread(() -> new SimpleServer(eventBus)).start();
        new SpotifyApiHandler(new MyHttpClient(), eventBus);
    }
}
