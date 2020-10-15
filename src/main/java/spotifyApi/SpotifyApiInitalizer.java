package spotifyApi;

import com.google.gson.GsonBuilder;
import customEvent.ServerEvent;
import customEvent.ServerEventType;
import http.MyHttpClient;
import lyrics.LyricsFinderProviderImpl;
import ui.MainSystemTray;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyApiInitalizer {
    private static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String SCOPE = "user-read-currently-playing";
    private static final String REDIRECT_URL = "http://127.0.0.1:8055/redirect";

    private CredentialsObtainer credentialsObtainer;
    private MyHttpClient client;

    public SpotifyApiInitalizer(MyHttpClient client) throws Exception {
        this.client = client;
        this.credentialsObtainer = new CredentialsObtainer();
    }

    public void handleServerEvent(ServerEvent serverEvent) {
        if (serverEvent.getType() == ServerEventType.STARTED) {
            openAuthorizeUserInBrowser();
        } else if (serverEvent.getType() == ServerEventType.OBTAINED_AUTH_CODE_FROM_REDIRECT) {
            String authCode = serverEvent.getMessage();
            handleAuthorizationCode(authCode);
        }
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

    private void handleAuthorizationCode(String code) {
        SpotifyApi spotifyApi = new SpotifyApi(client, credentialsObtainer, requestInitialToken(code));

        new MainSystemTray(spotifyApi, new LyricsFinderProviderImpl(client));
    }

    private SpotifyToken requestInitialToken(String code) {
        String body = "grant_type=authorization_code" + "&code=" + code + "&redirect_uri=" + REDIRECT_URL;

        String encoded = credentialsObtainer.getBase64encodedCredentials();
        String response = client.postRequest(API_TOKEN_URL, body, (postRequest) -> {
            postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.addHeader("Authorization", "Basic " + encoded);
        });

        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .fromJson(response, SpotifyToken.class);
    }


}
