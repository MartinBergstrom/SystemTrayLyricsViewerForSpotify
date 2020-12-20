package api.spotifyApi;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import http.SimpleHttpClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SpotifyApi {
    private static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String CURRENTLY_PLAYING_URL = "https://api.spotify.com/v1/me/player/currently-playing";

    private SpotifyToken mySpotifyToken;
    private SimpleHttpClient myClient;
    private SpotifyCredentials mySpotifyCredentials;

    SpotifyApi(SimpleHttpClient client, SpotifyCredentials spotifyCredentials, SpotifyToken initialToken) {
        myClient = client;
        mySpotifyCredentials = spotifyCredentials;
        mySpotifyToken = initialToken;
    }

    private void refreshToken() {
        String refreshToken = mySpotifyToken.getRefresh_token();
        LocalDateTime expiryStartTime = mySpotifyToken.getTokenExpiryStartTime();

        String body = "grant_type=refresh_token" + "&refresh_token=" + refreshToken;
        String encoded = mySpotifyCredentials.getBase64encodedCredentials();
        String response = myClient.postRequest(API_TOKEN_URL, body, (postRequest) -> {
            postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.addHeader("Authorization", "Basic " + encoded);
        });
        mySpotifyToken = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .fromJson(response, SpotifyToken.class);
        if (mySpotifyToken.getRefresh_token() == null || mySpotifyToken.getRefresh_token().isEmpty()) {
            mySpotifyToken = new SpotifyToken(mySpotifyToken.getAccess_token(),
                    mySpotifyToken.getScope(),
                    mySpotifyToken.getExpires_in(),
                    refreshToken,
                    expiryStartTime);
        }
        mySpotifyCredentials.saveRefreshToken(mySpotifyToken.getRefresh_token(), mySpotifyToken.getTokenExpiryStartTime());
    }

    public CurrentlyPlaying requestCurrentlyPlaying() {
        LocalDateTime startTimePlusExpirySeconds = (mySpotifyToken.getTokenExpiryStartTime().plusSeconds((long) mySpotifyToken.getExpires_in()));
        if (LocalDateTime.now().isAfter(startTimePlusExpirySeconds)) {
            refreshToken();
        }

        String response = myClient.getRequest(CURRENTLY_PLAYING_URL,
                (getRequest) -> getRequest.addHeader("Authorization", "Bearer " + mySpotifyToken.getAccess_token()));
        if (response == null) {
            System.out.println("No response from: " + CURRENTLY_PLAYING_URL + ". Spotify paused?");
            return null;
        }

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
