package lyrics;

import spotifyApi.CurrentlyPlaying;

import java.net.URL;
import java.util.Optional;

public interface LyricsFinder {

    /**
     * @return null if no lyrics find
     */
    Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying);
}
