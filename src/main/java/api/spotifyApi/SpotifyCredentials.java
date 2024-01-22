package api.spotifyApi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

class SpotifyCredentials {
    private static final String FILE_PATH_NAME = "D:\\Progg\\dev\\spotify-lyrics\\credentials_spotify.txt";
    private static final String CLIENT_ID_KEY = "clientID";
    private static final String SECRET_KEY = "secret";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String REFRESH_TOKEN_ACQUIRE_TIME = "refreshTokenAcquireTime";
    private String clientId;
    private String secret;
    private String refreshToken;
    private LocalDateTime myRefreshTokenAcquireTime;

    private final Properties prop;

    SpotifyCredentials() throws IOException {
        prop = new Properties();
        InputStream input = new FileInputStream(FILE_PATH_NAME);
        prop.load(input);
        input.close();
        clientId = prop.getProperty(CLIENT_ID_KEY);
        secret = prop.getProperty(SECRET_KEY);
        refreshToken = prop.getProperty(REFRESH_TOKEN);
        myRefreshTokenAcquireTime = parseRefreshTokenAcquireTime(prop.getProperty(REFRESH_TOKEN_ACQUIRE_TIME));
    }

    private LocalDateTime parseRefreshTokenAcquireTime(String refreshTokenAcquireTime) {
        if (refreshTokenAcquireTime != null && !refreshTokenAcquireTime.isEmpty()) {
            return LocalDateTime.parse(refreshTokenAcquireTime);
        }
        return null;
    }

    void saveRefreshToken(String refreshToken, LocalDateTime refreshTokenAcquireTime) {
        try {
            prop.setProperty(REFRESH_TOKEN, refreshToken);
            prop.setProperty(REFRESH_TOKEN_ACQUIRE_TIME, refreshTokenAcquireTime.toString());
            prop.store(new FileOutputStream(FILE_PATH_NAME), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Optional<String> getRefreshToken() {
        return Optional.ofNullable(refreshToken);
    }

    Optional<LocalDateTime> getRefreshTokenAcquireTime() {
        return Optional.ofNullable(myRefreshTokenAcquireTime);
    }

    String getBase64encodedCredentials() {
        String toEncode = clientId + ":" + secret;
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    String getClientId() {
        return clientId;
    }

}
