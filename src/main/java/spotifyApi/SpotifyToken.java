package spotifyApi;

public class SpotifyToken {

    private String access_token;
    private String token_type;
    private String scope;

    // Number of seconds before it expires
    private int expires_in;

    private String refresh_token;

    public String getAccess_token() {
        return access_token;
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
