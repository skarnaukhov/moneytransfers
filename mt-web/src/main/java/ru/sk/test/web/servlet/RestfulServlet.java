package ru.sk.test.web.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import ru.sk.test.mt.core.MoneyTransferService;
import ru.sk.test.mt.core.dto.OperationResultDto;
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
import java.math.BigDecimal;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
@Path("get")
public class RestfulServlet {

    private static final Logger logger = Logger.getLogger(RestfulServlet.class);

    @Inject
    private MoneyTransferService moneyTransferService;

    private final Gson gson = new Gson();

    @GET
    @Path("transfer/from/{fromUserId}/account/{fromAccountId}/to/{toUserId}/account/{toAccountId}/money/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public String transfer(
            @PathParam("fromUserId") Long fromUserId, @PathParam("fromAccountId") Long fromAccountId,
            @PathParam("toUserId") Long toUserId, @PathParam("toAccountId") Long toAccountId,
            @PathParam("amount") BigDecimal amount
    ) {
        OperationResultDto result;
        try {
            result = moneyTransferService.transfer(fromUserId, fromAccountId, toUserId, toAccountId, amount);
        } catch (Exception e) {
            String message = "Exception occurred while transfering money";
            logger.error(message, e);
            result = new OperationResultDto(false, message);
        }
        return gson.toJson(result);
    }

    @GET
    @Path("person/{id}/accountBalance/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public String accountBalance(@PathParam("id") Long personId, @PathParam("accountNumber") Long accountNumber) {
        logger.debug("Get account balance processing...");
        OperationResultDto result;
        try {
            result = moneyTransferService.getAccountBalance(personId, accountNumber);
        } catch (Exception e) {
            String message = "Exception occurred while getting account balance";
            logger.error(message, e);
            result = new OperationResultDto(false, message);
        }
        return gson.toJson(result);
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

