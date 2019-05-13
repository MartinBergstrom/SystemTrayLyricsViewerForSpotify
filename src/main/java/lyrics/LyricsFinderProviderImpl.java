package lyrics;

import http.MyHttpClient;

import java.util.ArrayList;
import java.util.List;

public class LyricsFinderProviderImpl implements LyricsFinderProvider{
    private MyHttpClient myHttpClient;

    public LyricsFinderProviderImpl(MyHttpClient myHttpClient) {
        this.myHttpClient = myHttpClient;
    }

    @Override
    public List<LyricsFinder> getAllLyricsFinders() {
        List<LyricsFinder> finders = new ArrayList<>();
        finders.add(new LyricsFinderBaseImpl(myHttpClient));
        return  finders;
    }
}