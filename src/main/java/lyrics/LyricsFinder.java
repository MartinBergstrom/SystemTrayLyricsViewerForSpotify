package lyrics;

import java.net.URL;

public interface LyricsFinder {

    /**
     * @return null if no lyrics find
     */
    URL findLyricsFor(String artist, String songname);
}
