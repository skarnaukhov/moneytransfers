package ru.sk.test.web;


import org.apache.log4j.Logger;
import ru.sk.test.web.server.ServerStarter;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
public class Runner {

    private static final Logger logger = Logger.getLogger(Runner.class);

    public static void main(String[] args) {
        try {
            ServerStarter.startServer();
        } catch (Exception e) {
            logger.error("Error while starting the DB", e);
            ServerStarter.stopServer();
        }
    }

}
