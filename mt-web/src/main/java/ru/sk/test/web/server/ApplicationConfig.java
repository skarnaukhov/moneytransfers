package ru.sk.test.web.server;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
@ApplicationPath("/*")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(new SingletonBinder());
        register(ImmediateFeature.class);
//        register(MOXyJsonContextResolver.class);
    }

}
