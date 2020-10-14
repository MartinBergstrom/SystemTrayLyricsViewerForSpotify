package lyrics;

import http.MyHttpClient;
import org.apache.http.HttpResponse;
import spotifyApi.CurrentlyPlaying;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

class AZLyricsFinder implements LyricsFinder {
    private static final String AZ_LYRICS = "https://www.azlyrics.com/lyrics/";
    private MyHttpClient httpClient;
    private final ArtistAndSongStringFormatter artistAndSongStringFormatter;

    AZLyricsFinder(MyHttpClient httpClient) {
        this.httpClient = httpClient;
        this.artistAndSongStringFormatter = new ArtistAndSongStringFormatter()
                .addRemoveAllWhiteSpace();
    }

    @Override
    public Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying) {
        String convertedSongName = artistAndSongStringFormatter.format(currentlyPlaying.getSong());

        String convertedArtistName = artistAndSongStringFormatter.format(currentlyPlaying.getMyArtist());

        String url = AZ_LYRICS + convertedArtistName + "/" + convertedSongName + ".html";
        HttpResponse response = httpClient.getRequest(url);

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
