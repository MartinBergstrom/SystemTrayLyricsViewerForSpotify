package server;

import customEvent.ServerEvent;
import customEvent.ServerEventType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import server.resources.RedirectHandler;

import java.util.function.Consumer;

public class SimpleServer {
    private static final String PORT = "8055";
    private Consumer<ServerEvent> myEventCallback;

    public SimpleServer(Consumer<ServerEvent> eventCallback) {
        this.myEventCallback = eventCallback;
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");

        Server jettyServer = new Server(Integer.valueOf(PORT));

        ServletContainer container = new ServletContainer(createResourceConfig());
        ServletHolder servletHolder = new ServletHolder(container);

        handler.addServlet(servletHolder, "/*");
        jettyServer.setHandler(handler);

        try {
            jettyServer.start();
            myEventCallback.accept(new ServerEvent(ServerEventType.STARTED));
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ResourceConfig createResourceConfig(){
        ResourceConfig resourceConfig = new ResourceConfig();
        RedirectHandler handler = new RedirectHandler(myEventCallback);
        resourceConfig.register(handler);
        return resourceConfig;
    }

}
