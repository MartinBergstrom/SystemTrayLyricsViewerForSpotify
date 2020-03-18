package lyrics;

import http.MyHttpClient;
import org.apache.http.HttpResponse;
import spotifyApi.CurrentlyPlaying;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class AZLyricsFinder implements LyricsFinder{
    private static final String AZ_LYRICS = "https://www.azlyrics.com/lyrics";
    private MyHttpClient httpClient;

    public AZLyricsFinder(MyHttpClient httpClient) {
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
                // run and schedule robot with listener to break if mouse movement
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
