package ru.sk.test.mt;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Sergey_Karnaukhov on 26.03.2017
 */
public class H2Test {

    @Test
    public void testLaunch() throws Exception {
        Class.forName(org.h2.Driver.class.getName());
        final Connection conn = DriverManager.getConnection(
                "jdbc:h2:~/test;INIT=runscript from 'classpath:sql/create.sql'\\;runscript from 'classpath:sql/init.sql'", "test", "");
    }

}
