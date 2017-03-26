package ru.sk.test.web.server;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import ru.sk.test.web.Runner;
import ru.sk.test.web.controller.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
public class ServerStarter {

    private static final Logger logger = Logger.getLogger(Runner.class);

    private static Server server;

    public static void startServer() {
        try {
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            server = new Server(7070);
            server.setHandler(context);
            addRestfulServlet(context);

            server.start();
            logger.info("Jetty Server has been started");
            server.join();
        } catch (Exception ex) {
            logger.error("Error occurred while starting Jetty Server", ex);
        }
    }

    private static void addRestfulServlet(ServletContextHandler context) {
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);
        Map<String ,String> initParams = new HashMap<>();
        initParams.put("jersey.config.server.provider.classnames",
                RestController.class.getCanonicalName());
        initParams.put("javax.ws.rs.Application",
                ApplicationConfig.class.getCanonicalName());
        jerseyServlet.setInitParameters(initParams);
    }

    public static void stopServer() {
        try {
            if (server != null) {
                server.stop();
                server = null;
            }
        } catch (Exception e) {
            logger.error("Error while stopping jetty server", e);
        }
    }
}
