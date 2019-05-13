package lyrics;

import http.MyHttpClient;
import org.apache.http.HttpResponse;
import spotifyApi.CurrentlyPlaying;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public class LyricsFinderBaseImpl extends AbstractLyricsFinder implements LyricsFinder {
    private static final String AZ_LYRICS = "https://www.azlyrics.com/lyrics";
    // https://www.azlyrics.com/lyrics/inflames/ithemask.html

    private MyHttpClient httpClient;

    public LyricsFinderBaseImpl(MyHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<URL> findLyricsFor(CurrentlyPlaying currentlyPlaying) {
        String url = AZ_LYRICS + "/" + convertString(currentlyPlaying.getArtist()) + "/" +
                convertString(currentlyPlaying.getSong()) + ".html";
        HttpResponse response = httpClient.getRequest(url);

        if (response.getStatusLine().getStatusCode() != 404){
            try {
                return Optional.of(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private static String convertString(String string) {
        return string.toLowerCase().replace(" ","");
    }
}
