package api.spotifyApi;

import api.ApiInitializer;
import com.google.gson.GsonBuilder;
import http.MyHttpClient;
import lyrics.LyricsFinderProviderImpl;
import ui.MainSystemTray;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class SpotifyApiInitalizer implements ApiInitializer {
    private static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String SCOPE = "user-read-currently-playing";
    private static final String REDIRECT_URL = "http://127.0.0.1:8055/redirect";

    private SpotifyCredentials spotifyCredentials;
    private MyHttpClient client;

    public SpotifyApiInitalizer(MyHttpClient client) throws Exception {
        this.client = client;
        this.spotifyCredentials = new SpotifyCredentials();
    }

    @Override
    public boolean isInitialized() {
        return spotifyCredentials.getAccessToken().isPresent();
    }

    @Override
    public void authorizeUser() {
        String url = AUTH_URL + "?response_type=code&client_id=" + spotifyCredentials.getClientId()
                + "&scope=" + SCOPE + "&redirect_uri=" + REDIRECT_URL;

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void launchApi() {
        String accessToken = spotifyCredentials.getAccessToken()
                .orElseThrow(() -> new IllegalStateException("No accessToken found, unable to launch api"));

        lauchApiAndSystemTray(accessToken);
    }

    @Override
    public void launchApi(String accessToken) {
        spotifyCredentials.saveAccessToken(accessToken);
        lauchApiAndSystemTray(accessToken);
    }

    private void lauchApiAndSystemTray(String accessToken) {
        SpotifyApi spotifyApi = new SpotifyApi(client, spotifyCredentials, requestInitialToken(accessToken));

        new MainSystemTray(spotifyApi, new LyricsFinderProviderImpl(client));
    }

    private SpotifyToken requestInitialToken(String code) {
        String body = "grant_type=authorization_code" + "&code=" + code + "&redirect_uri=" + REDIRECT_URL;

        String encoded = spotifyCredentials.getBase64encodedCredentials();
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
