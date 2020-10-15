package spotifyApi;

import java.time.LocalTime;

public class SpotifyToken {

    private String access_token;
    private String token_type;
    private String scope;
    private transient LocalTime tokenExpiryStartTime;

    public SpotifyToken() {
        this.tokenExpiryStartTime = LocalTime.now();
    }

    // Number of seconds before it expires
    private int expires_in;

    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public LocalTime getTokenExpiryStartTime() {
        return tokenExpiryStartTime;
    }

    public String getScope() {
        return scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}
