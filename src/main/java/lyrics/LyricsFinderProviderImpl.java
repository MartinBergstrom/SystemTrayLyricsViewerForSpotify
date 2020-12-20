package lyrics;

import http.SimpleHttpClient;

import java.util.ArrayList;
import java.util.List;

public class LyricsFinderProviderImpl implements LyricsFinderProvider{
    private SimpleHttpClient simpleHttpClient;

    public LyricsFinderProviderImpl(SimpleHttpClient simpleHttpClient) {
        this.simpleHttpClient = simpleHttpClient;
    }

    @Override
    public List<LyricsFinder> getAllLyricsFinders() {
        List<LyricsFinder> finders = new ArrayList<>();

        finders.add(new GeniusLyricsFinder(simpleHttpClient));
        finders.add(new AZLyricsFinder(simpleHttpClient));
        finders.add(new FallbackLyricsFinder());

        return  finders;
    }
}
