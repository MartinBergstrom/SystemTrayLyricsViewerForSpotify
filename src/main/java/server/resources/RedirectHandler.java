package server.resources;

import customEvent.ServerEvent;
import customEvent.ServerEventType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.function.Consumer;

@Path("redirect")
public class RedirectHandler {
    private Consumer<ServerEvent> myEventCallback;

    public RedirectHandler(Consumer<ServerEvent> eventCallback){
        this.myEventCallback = eventCallback;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(@QueryParam("code") String authCode,
                             @QueryParam("error") String error,
                             @QueryParam("state") String state)
    {
        if (authCode != null) {
            myEventCallback.accept(new ServerEvent(ServerEventType.OBTAINED_AUTH_CODE_FROM_REDIRECT, authCode));
            return "Successfully authorized the user, you may close this page\n";
        }
        return "Could not authorize the user, error message: " + error;
    }
}
