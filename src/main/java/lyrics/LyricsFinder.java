package lyrics;

import spotifyApi.CurrentlyPlaying;

import java.net.URL;

public interface LyricsFinder {

    /**
     * @return null if no lyrics find
     */
    URL findLyricsFor(CurrentlyPlaying currentlyPlaying);
}
