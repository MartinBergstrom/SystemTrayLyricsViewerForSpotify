package api;

import api.spotifyApi.SpotifyApiInitalizer;
import http.MyHttpClient;

import java.util.*;

public class ApiInitializers {
    private final Map<API, ApiInitializer> myInitializerMapping;

    public ApiInitializers(MyHttpClient httpClient) throws Exception {
        myInitializerMapping = new HashMap<>();

        myInitializerMapping.put(API.SPOTIFY, new SpotifyApiInitalizer(httpClient));
    }

    public ApiInitializer get(API api) {
        ApiInitializer initializer = myInitializerMapping.get(api);
        if (initializer == null) {
            throw new IllegalArgumentException("Unknown initalizer type: " + api);
        }
        return initializer;
    }

    public List<ApiInitializer> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(myInitializerMapping.values()));
    }


}
