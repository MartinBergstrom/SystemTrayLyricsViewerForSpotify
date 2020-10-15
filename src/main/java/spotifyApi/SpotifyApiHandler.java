package spotifyApi;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import http.MyHttpClient;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SpotifyApiHandler {
    private static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String CURRENTLY_PLAYING_URL = "https://api.spotify.com/v1/me/player/currently-playing";

    private SpotifyToken mySpotifyToken;
    private MyHttpClient myClient;
    private CredentialsObtainer myCredentialsObtainer;

    SpotifyApiHandler(MyHttpClient client, CredentialsObtainer credentialsObtainer, SpotifyToken initialToken) {
        myClient = client;
        myCredentialsObtainer = credentialsObtainer;
        mySpotifyToken = initialToken;
    }

    private void requestTokenApi(String body) {
        String encoded = myCredentialsObtainer.getBase64encodedCredentials();
        String response = myClient.postRequest(API_TOKEN_URL, body, (postRequest) -> {
            postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.addHeader("Authorization", "Basic " + encoded);
        });
        mySpotifyToken = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .fromJson(response, SpotifyToken.class);
    }

    private void refreshToken() {
        String body = "grant_type=refresh_token" + "&refresh_token=" + mySpotifyToken.getRefresh_token();
        requestTokenApi(body);
    }

    public CurrentlyPlaying requestCurrentlyPlaying() {
        LocalTime startTimePlusExpirySeconds = (mySpotifyToken.getTokenExpiryStartTime().plusSeconds((long) mySpotifyToken.getExpires_in()));
        if (LocalTime.now().isAfter(startTimePlusExpirySeconds)) {
            refreshToken();
        }

        String response = myClient.getRequest(CURRENTLY_PLAYING_URL,
                (getRequest) -> getRequest.addHeader("Authorization", "Bearer " + mySpotifyToken.getAccess_token()));

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
        return Duration.of(object.get("item").getAsJsonObject().get("duration_ms").getAsLong(), ChronoUnit.MILLIS);
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
