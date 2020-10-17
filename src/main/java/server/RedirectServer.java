package server;

import api.API;
import api.ApiInitializer;
import api.ApiInitializers;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import server.resources.RedirectHandlerGenius;
import server.resources.RedirectHandlerSpotify;

public class RedirectServer {
    private static final String PORT = "8055";
    private ApiInitializers myApiInitalizers;

    public RedirectServer(ApiInitializers apiInitializers) {
        this.myApiInitalizers = apiInitializers;
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");

        Server jettyServer = new Server(Integer.valueOf(PORT));

        ServletContainer container = new ServletContainer(createResourceConfig());
        ServletHolder servletHolder = new ServletHolder(container);

        handler.addServlet(servletHolder, "/*");
        jettyServer.setHandler(handler);

        try {
            jettyServer.start();
            myApiInitalizers.getAll().forEach(ApiInitializer::authorizeUser);
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ResourceConfig createResourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        RedirectHandlerSpotify handlerSpotify = new RedirectHandlerSpotify(myApiInitalizers.get(API.SPOTIFY));
        RedirectHandlerGenius handlerGenius = new RedirectHandlerGenius(myApiInitalizers.get(API.GENIUS));
        resourceConfig.register(handlerSpotify);
        resourceConfig.register(handlerGenius);
        return resourceConfig;
    }

}
