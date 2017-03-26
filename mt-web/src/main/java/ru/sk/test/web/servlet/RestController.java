package ru.sk.test.web.servlet;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.sk.test.mt.core.MoneyTransferService;
import ru.sk.test.mt.core.dto.OperationResultDto;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
@Path("")
public class RestController {

    private static final Logger logger = Logger.getLogger(RestController.class);

    @Inject
    private MoneyTransferService moneyTransferService;

    private final Gson gson = new Gson();

    @GET
    @Path("transfer/from/{fromAccountId}/to/{toAccountId}/amount/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public String transfer(
            @PathParam("fromAccountId") Long fromAccountId,
            @PathParam("toAccountId") Long toAccountId,
            @PathParam("amount") BigDecimal amount
    ) {
        OperationResultDto result;
        try {
            result = moneyTransferService.transfer(fromAccountId, toAccountId, amount);
        } catch (Exception e) {
            String message = "Exception occurred while transferring money";
            logger.error(message, e);
            result = new OperationResultDto(false, message);
        }
        return gson.toJson(result);
    }

    @GET
    @Path("deposit/{toAccountId}/amount/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deposit(
            @PathParam("toAccountId") Long toAccountId,
            @PathParam("amount") BigDecimal amount
    ) {
        OperationResultDto result;
        try {
            result = moneyTransferService.deposit(toAccountId, amount);
        } catch (Exception e) {
            String message = "Exception occurred while depositing money";
            logger.error(message, e);
            result = new OperationResultDto(false, message);
        }
        return gson.toJson(result);
    }

    @GET
    @Path("withdraw/{fromAccountId}/amount/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public String withdraw(
            @PathParam("fromAccountId") Long fromAccountId,
            @PathParam("amount") BigDecimal amount
    ) {
        OperationResultDto result;
        try {
            result = moneyTransferService.withdraw(fromAccountId, amount);
        } catch (Exception e) {
            String message = "Exception occurred while withdrawing money";
            logger.error(message, e);
            result = new OperationResultDto(false, message);
        }
        return gson.toJson(result);
    }

    @GET
    @Path("accountBalance/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public String accountBalance(@PathParam("accountNumber") Long accountNumber) {
        logger.debug("Get account balance processing...");
        OperationResultDto result;
        try {
            result = moneyTransferService.getAccountBalance(accountNumber);
        } catch (Exception e) {
            String message = "Exception occurred while getting account balance";
            logger.error(message, e);
            result = new OperationResultDto(false, message);
        }
        return gson.toJson(result);
    }
}

