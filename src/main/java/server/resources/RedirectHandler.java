package server.resources;

import com.google.common.eventbus.EventBus;
import customEvent.ServerEvent;
import customEvent.ServerEventType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("redirect")
public class RedirectHandler {
    private EventBus eventBus;

    public RedirectHandler(EventBus eventBus){
        this.eventBus = eventBus;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(@QueryParam("code") String authCode,
                             @QueryParam("error") String error,
                             @QueryParam("state") String state)
    {
        if (authCode != null) {
            eventBus.post(new ServerEvent<>(ServerEventType.OBTAINED_AUTH_CODE_FROM_REDIRECT, authCode));
            return "Successfully authorized the user, you may close this page\n";
        }
        return "Could not authorize the user, error message: " + error;
    }
}
