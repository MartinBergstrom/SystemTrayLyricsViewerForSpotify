package lyrics;

import http.MyHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class LyricsFinderBaseImpl implements  LyricsFinder {
    private static final String AZ_LYRICS = "https://www.azlyrics.com/lyrics";
    // https://www.azlyrics.com/lyrics/inflames/ithemask.html

    private MyHttpClient httpClient;

    public LyricsFinderBaseImpl(MyHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public URL findLyricsFor(String artist, String songname) {
        String url = AZ_LYRICS + "/" + convertString(artist) + "/" + convertString(songname) + ".html";
        HttpResponse response = httpClient.getRequest(url);

        if (response.getStatusLine().getStatusCode() != 404){
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String convertString(String string) {
        return string.toLowerCase().replace(" ","");
    }
}
