package lyrics;

import spotifyApi.CurrentlyPlaying;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

class FallbackLyricsFinder implements LyricsFinder {
    private String artist;
    private String song;

    private String getGoogleUrl() {
        return "https://www.google.com/search?q=" +  artist + "+"+ song +"+lyrics";
    }

    @Override
    public Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying) {
        artist = currentlyPlaying.getArtist();
        song = currentlyPlaying.getSong();
        try {
            return Optional.of(new URL(transform(getGoogleUrl())));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static String transform(String input) {
        return input.replaceAll(" ", "+");
    }
}
