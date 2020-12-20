package lyrics;

import http.SimpleHttpClient;
import org.apache.http.HttpResponse;
import api.spotifyApi.CurrentlyPlaying;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

class GeniusLyricsFinder implements LyricsFinder {
    private static final String GENIUS_URL = "https://genius.com/";
    private SimpleHttpClient httpClient;
    private final ArtistAndSongStringFormatter artistAndSongStringFormatter;

    GeniusLyricsFinder(SimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        this.artistAndSongStringFormatter = new ArtistAndSongStringFormatter().addDelimiterReplaceWhiteSpace("-");
    }

    @Override
    public Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying) {
        String convertedArtist =  artistAndSongStringFormatter.format(currentlyPlaying.getMyArtist());
        String convertedSong = artistAndSongStringFormatter.format(currentlyPlaying.getSong());

        System.out.println("CONVERTED ARTIST: " + convertedArtist);
        System.out.println("CONVERTED SONG: " + convertedSong);

        String url = GENIUS_URL + convertedArtist + "-" + convertedSong + "-lyrics";
        HttpResponse response = httpClient.getRequestHttpResponse(url);

        if (response.getStatusLine().getStatusCode() != 404) {
            try {
                return Optional.of(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public LyricsWebPage getLyricsWebPage() {
        return LyricsWebPage.GENIUS;
    }
}
