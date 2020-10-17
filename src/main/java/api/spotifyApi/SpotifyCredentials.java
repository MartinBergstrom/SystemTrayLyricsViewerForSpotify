package api.spotifyApi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

class SpotifyCredentials {
    private static final String FILE_PATH_NAME = "D:\\Progg\\Workarea\\spotifylyricssystemtray\\credentials_spotify.txt";
    private static final String CLIENT_ID_KEY = "clientID";
    private static final String SECRET_KEY = "secret";
    private static final String ACCESS_TOKEN = "accessToken";
    private String clientId;
    private String secret;
    private String accessToken;

    private final Properties prop;

    SpotifyCredentials() throws IOException {
        prop = new Properties();
        InputStream input = new FileInputStream(FILE_PATH_NAME);
        prop.load(input);
        input.close();
        clientId = prop.getProperty(CLIENT_ID_KEY);
        secret = prop.getProperty(SECRET_KEY);
        accessToken = prop.getProperty(ACCESS_TOKEN);
    }

    void saveAccessToken(String accessToken) {
        try {
            prop.setProperty(ACCESS_TOKEN, accessToken);
            prop.store(new FileOutputStream(FILE_PATH_NAME), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Optional<String> getAccessToken() {
        return Optional.ofNullable(accessToken);
    }

    String getBase64encodedCredentials() {
        String toEncode = clientId + ":" + secret;
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    String getClientId() {
        return clientId;
    }

}
