package server.resources;

import api.ApiInitializer;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("redirect")
public class RedirectHandlerSpotify {
    private ApiInitializer mySpotifyAPiInitializer;

    public RedirectHandlerSpotify(ApiInitializer spotifyAPiInitializer) {
        this.mySpotifyAPiInitializer = spotifyAPiInitializer;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(@QueryParam("code") String authCode,
                             @QueryParam("error") String error,
                             @QueryParam("state") String state) {
        if (authCode != null) {
            mySpotifyAPiInitializer.launchApi(authCode);
            return "Successfully authorized the user, you may close this page\n";
        }
        return "Could not authorize the user, error message: " + error;
    }
}
