package lyrics;

import http.MyHttpClient;
import org.apache.http.HttpResponse;
import spotifyApi.CurrentlyPlaying;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

class GeniusLyricsFinder implements LyricsFinder{
    private static final String GENIUS_URL = "https://www.genius.com/";
    private MyHttpClient httpClient;

    GeniusLyricsFinder(MyHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying) {
        String convertedArtist = new ArtistAndSongStringFormatter(currentlyPlaying.getArtist()).format();
        String convertedSong = new ArtistAndSongStringFormatter(currentlyPlaying.getSong())
                .addDelimiter("-")
                .format();

        String url = GENIUS_URL + convertedArtist + "-" + convertedSong + "-lyrics";
        HttpResponse response = httpClient.getRequest(url);

        if (response.getStatusLine().getStatusCode() != 404) {
            try {
                return Optional.of(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }


}
