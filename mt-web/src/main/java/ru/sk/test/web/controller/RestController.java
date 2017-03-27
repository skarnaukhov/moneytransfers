package ru.sk.test.web.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.sk.test.mt.core.AbstractExecutionResponse;
import ru.sk.test.mt.core.MoneyTransferService;
import ru.sk.test.mt.core.dto.ExecutionRequest;
import ru.sk.test.mt.core.dto.ExecutionResult;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
@Path("")
public class RestController extends AbstractExecutionResponse {

    private static final Logger logger = Logger.getLogger(RestController.class);

    @Inject
    private MoneyTransferService moneyTransferService;

    private final Gson gson = new Gson();

    @POST
    @Path("transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String transfer(ExecutionRequest executionRequest) {
        ExecutionResult result;
        try {
            result = moneyTransferService.transfer(executionRequest);
        } catch (Exception e) {
            String message = "Exception occurred while transferring money";
            logger.error(message, e);
            result = failure(message);
        }
        return gson.toJson(result);
    }

    @POST
    @Path("deposit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deposit(
            ExecutionRequest executionRequest
    ) {
        ExecutionResult result;
        try {
            result = moneyTransferService.deposit(executionRequest);
        } catch (Exception e) {
            String message = "Exception occurred while depositing money";
            logger.error(message, e);
            result = failure(message);
        }
        return gson.toJson(result);
    }

    @POST
    @Path("withdraw")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String withdraw(ExecutionRequest executionRequest) {
        ExecutionResult result;
        try {
            result = moneyTransferService.withdraw(executionRequest);
        } catch (Exception e) {
            String message = "Exception occurred while withdrawing money";
            logger.error(message, e);
            result = failure(message);
        }
        return gson.toJson(result);
    }

    @GET
    @Path("accountBalance/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public String accountBalance(@PathParam("accountNumber") Long accountNumber) {
        logger.debug("Get account balance processing...");
        ExecutionResult result;
        try {
            result = moneyTransferService.getAccountBalance(accountNumber);
        } catch (Exception e) {
            String message = "Exception occurred while getting account balance";
            logger.error(message, e);
            result = failure(message);
        }
        return gson.toJson(result);
    }
}

