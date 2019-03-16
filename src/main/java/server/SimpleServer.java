package server;

import com.google.common.eventbus.EventBus;
import customEvent.ServerEvent;
import customEvent.ServerEventType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import server.resources.RedirectHandler;

public class SimpleServer {
    public static final String PORT = "8055";
    private EventBus eventBus;

    public SimpleServer(EventBus eventBus) {
        this.eventBus = eventBus;
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");

        Server jettyServer = new Server(Integer.valueOf(PORT));

        ServletContainer container = new ServletContainer(createResourceConfig());
        ServletHolder servletHolder = new ServletHolder(container);

        handler.addServlet(servletHolder, "/*");
        jettyServer.setHandler(handler);

        try {
            jettyServer.start();
            eventBus.post(new ServerEvent(ServerEventType.STARTED));
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ResourceConfig createResourceConfig(){
        ResourceConfig resourceConfig = new ResourceConfig();
        RedirectHandler handler = new RedirectHandler(eventBus);
        resourceConfig.register(handler);
        return resourceConfig;
    }

}
