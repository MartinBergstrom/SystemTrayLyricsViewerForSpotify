package spotifyApi;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import customEvent.ServerEvent;
import customEvent.ServerEventType;
import http.MyHttpClient;
import ui.MainSystemTray;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyApiHandler  {
    private static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String CURRENTLY_PLAYING_URL = "https://api.spotify.com/v1/me/player/currently-playing";
    private static final String SCOPE = "user-read-currently-playing";
    private static final String REDIRECT_URL = "http://127.0.0.1:8055/redirect";

    private SpotifyToken spotifyToken;
    private MyHttpClient client;
    private CredentialsObtainer credentialsObtainer;

    private EventBus eventBus;

    private MainSystemTray mainSystemTray;

    public SpotifyApiHandler(MyHttpClient client, EventBus eventBus) throws Exception {
        this.client = client;
        credentialsObtainer = new CredentialsObtainer();
        eventBus.register(this);
    }

    @Subscribe
    public void handleServerEvent(ServerEvent<String> serverEvent) {
        if (serverEvent.getType() == ServerEventType.STARTED) {
            openAuthorizeUserInBrowser();
        } else if (serverEvent.getType() == ServerEventType.OBTAINED_AUTH_CODE_FROM_REDIRECT) {
            String authCode = serverEvent.getPayload();
            handleAuthorizationCode(authCode);
        }
    }

    private void handleAuthorizationCode(String code) {
        String body = "grant_type=authorization_code" + "&code=" + code + "&redirect_uri=" + REDIRECT_URL;
        String encoded = credentialsObtainer.getBase64encodedCredentials();
        String response = client.postRequest(API_TOKEN_URL, body, (postRequest) -> {
            postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.addHeader("Authorization", "Basic " + encoded);
        });
        spotifyToken = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .fromJson(response, SpotifyToken.class);
        mainSystemTray = new MainSystemTray(this);
    }

    private void openAuthorizeUserInBrowser() {
        String url = AUTH_URL + "?response_type=code&client_id=" + credentialsObtainer.getClientId()
                + "&scope=" + SCOPE + "&redirect_uri=" + REDIRECT_URL;

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public CurrentlyPlaying getMyCurrentlyPlaying() {
        String respone = client.getRequest( CURRENTLY_PLAYING_URL, (getRequest) -> {
            getRequest.addHeader("Authorization", "Bearer " + spotifyToken.getAccess_token());
        });
        JsonElement element =  new JsonParser().parse(respone);
        String artist = element.getAsJsonObject()
                .get("item").getAsJsonObject()
                .get("artists").getAsJsonArray()
                .get(0).getAsJsonObject().get("name").getAsString();
        String song =  element.getAsJsonObject()
                .get("item").getAsJsonObject()
                .get("name").getAsString();

        return new CurrentlyPlaying(artist, song);

    }

}
