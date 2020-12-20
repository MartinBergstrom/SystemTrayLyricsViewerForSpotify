package lyrics;

import http.SimpleHttpClient;
import org.apache.http.HttpResponse;
import api.spotifyApi.CurrentlyPlaying;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

class AZLyricsFinder implements LyricsFinder {
    private static final String AZ_LYRICS = "https://www.azlyrics.com/lyrics/";
    private SimpleHttpClient httpClient;
    private final ArtistAndSongStringFormatter artistAndSongStringFormatter;

    AZLyricsFinder(SimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        this.artistAndSongStringFormatter = new ArtistAndSongStringFormatter()
                .addRemoveAllWhiteSpace();
    }

    @Override
    public Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying) {
        String convertedSongName = artistAndSongStringFormatter.format(currentlyPlaying.getSong());

        String convertedArtistName = artistAndSongStringFormatter.format(currentlyPlaying.getMyArtist());

        String url = AZ_LYRICS + convertedArtistName + "/" + convertedSongName + ".html";
        HttpResponse response = httpClient.getRequestHttpResponse(url);

        if (response.getStatusLine().getStatusCode() != 404) {
            try {
                return Optional.of(new URL(url));
                // run and schedule robot with listener to break if mouse movement
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public LyricsWebPage getLyricsWebPage() {
        return LyricsWebPage.AZ;
    }

}
