package spotifyApi;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import customEvent.ServerEvent;
import customEvent.ServerEventType;
import http.MyHttpClient;
import lyrics.LyricsFinderProviderImpl;
import ui.MainSystemTray;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SpotifyApiHandler {
    private static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String CURRENTLY_PLAYING_URL = "https://api.spotify.com/v1/me/player/currently-playing";
    private static final String SCOPE = "user-read-currently-playing";
    private static final String REDIRECT_URL = "http://127.0.0.1:8055/redirect";

    private SpotifyToken spotifyToken;
    private MyHttpClient client;
    private CredentialsObtainer credentialsObtainer;

    private LocalTime tokenExpiryStartTime;

    public SpotifyApiHandler(MyHttpClient client) throws Exception {
        this.client = client;
        credentialsObtainer = new CredentialsObtainer();
    }

    public void handleServerEvent(ServerEvent serverEvent) {
        if (serverEvent.getType() == ServerEventType.STARTED) {
            openAuthorizeUserInBrowser();
        } else if (serverEvent.getType() == ServerEventType.OBTAINED_AUTH_CODE_FROM_REDIRECT) {
            String authCode = serverEvent.getMessage();
            handleAuthorizationCode(authCode);
        }
    }

    private void handleAuthorizationCode(String code) {
        String body = "grant_type=authorization_code" + "&code=" + code + "&redirect_uri=" + REDIRECT_URL;
        requestTokenApi(body);
        new MainSystemTray(this, new LyricsFinderProviderImpl(client));
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

    private void requestTokenApi(String body) {
        String encoded = credentialsObtainer.getBase64encodedCredentials();
        String response = client.postRequest(API_TOKEN_URL, body, (postRequest) -> {
            postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.addHeader("Authorization", "Basic " + encoded);
        });
        spotifyToken = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .fromJson(response, SpotifyToken.class);
        tokenExpiryStartTime = LocalTime.now();
    }

    private void refreshToken() {
        String body = "grant_type=refresh_token" + "&refresh_token=" + spotifyToken.getRefresh_token();
        requestTokenApi(body);
    }

    public CurrentlyPlaying requestCurrentlyPlaying() {
        LocalTime startTimePlusExpirySeconds = (tokenExpiryStartTime.plusSeconds((long) spotifyToken.getExpires_in()));
        if (LocalTime.now().isAfter(startTimePlusExpirySeconds)) {
            refreshToken();
        }

        String response = client.getRequest(CURRENTLY_PLAYING_URL,
                (getRequest) -> getRequest.addHeader("Authorization", "Bearer " + spotifyToken.getAccess_token()));

        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

        if (jsonObject.get("item").getAsJsonObject() == null) {
            throw new RuntimeException("Could not get currently playing");
        }

        return CurrentlyPlaying.newBuilder()
                .withArtist(getArtist(jsonObject))
                .withSong(getSong(jsonObject))
                .withSongProgress(getSongProgress(jsonObject))
                .withSongLength(getSongLength(jsonObject))
                .withIsCurrentlyPlaying(getCurrentlyPlaying(jsonObject))
                .build();
    }

    private boolean getCurrentlyPlaying(JsonObject object) {
        return object.get("is_playing").getAsBoolean();
    }

    private Duration getSongLength(JsonObject object) {
        return Duration.of(object.get("duration_ms").getAsLong(), ChronoUnit.MILLIS);
    }

    private Duration getSongProgress(JsonObject object) {
        return Duration.of(object.get("progress_ms").getAsLong(), ChronoUnit.MILLIS);
    }

    private static String getArtist(JsonObject object) {
        return object.get("item").getAsJsonObject()
                .get("artists").getAsJsonArray()
                .get(0).getAsJsonObject().get("name").getAsString();
    }

    private static String getSong(JsonObject object) {
        return object.get("item").getAsJsonObject()
                .get("name").getAsString();
    }

}
