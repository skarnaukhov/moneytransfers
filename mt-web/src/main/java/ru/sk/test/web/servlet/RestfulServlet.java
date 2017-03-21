package ru.sk.test.web.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import ru.sk.test.mt.core.MoneyTransferService;
import ru.sk.test.mt.data.dao.UserDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.common.gson.AnnotationExcludeStrategy;
import ru.sk.test.web.Runner;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
@Path("home")
public class RestfulServlet {

    private static final Logger logger = Logger.getLogger(RestfulServlet.class);

    @Inject
    private MoneyTransferService moneyTransferService;

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        logger.debug("Get request processing...");
        return "Hello, world!";
    }

    @GET
    @Path("person/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPersonById(@PathParam("id") String idParam) {
        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException nfe) {
            return "ID is not valid number";
        }
        return moneyTransferService.getPersonById(id).toString();
    }
}

