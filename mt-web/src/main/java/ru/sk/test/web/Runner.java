package ru.sk.test.web;


import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.h2.jdbcx.JdbcDataSource;
import ru.sk.test.mt.data.dao.UserDAO;
import ru.sk.test.mt.data.entity.Person;
import ru.sk.test.mt.data.h2.DataBaseStarter;
import ru.sk.test.web.server.ServerStarter;
import ru.sk.test.web.servlet.RestfulServlet;

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
