package ru.sk.test.mt.data.h2;

import org.apache.log4j.Logger;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
public class DataBaseStarter {

    private static final Logger logger = Logger.getLogger(DataBaseStarter.class);

    private static Server server = null;
    private static final String URL = "jdbc:h2:mem:test;INIT=runscript from 'classpath:sql/create.sql'\\;runscript from 'classpath:sql/init.sql'";

    public static void startDB() throws ClassNotFoundException, SQLException {
        logger.debug("Starting DB server");
        server = Server.createTcpServer(
                "-tcpPort", "9123", "-tcpAllowOthers").start();
        Connection con = DriverManager.getConnection(URL, "test", "" );
        con.close();
        logger.info("DB server has been started");
    }

    public static void closeDB() {
        if (server != null) {
            server.stop();
        }
        logger.debug("DB server has been closed");
    }

}
