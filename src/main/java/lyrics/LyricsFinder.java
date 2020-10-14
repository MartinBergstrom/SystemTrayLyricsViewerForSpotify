package lyrics;

import spotifyApi.CurrentlyPlaying;

import java.net.URL;
import java.util.Optional;

public interface LyricsFinder {

    Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying);

    LyricsWebPage getLyricsWebPage();

}
