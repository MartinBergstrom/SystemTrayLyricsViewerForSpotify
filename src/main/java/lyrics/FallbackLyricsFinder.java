package lyrics;

import http.MyHttpClient;
import spotifyApi.CurrentlyPlaying;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class FallbackLyricsFinder implements LyricsFinder {
    private String artist;
    private String song;

    private MyHttpClient httpClient;

    public FallbackLyricsFinder(MyHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private String getGoogleUrl() {
        return "https://www.google.com/search?q=" +  artist + "+"+ song +"+lyrics";
    }

    @Override
    public Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying) {
        artist = currentlyPlaying.getArtist();
        song = currentlyPlaying.getSong();
        try {
            return Optional.of(new URL(getGoogleUrl()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
