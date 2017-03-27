package ru.sk.test.web;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import ru.sk.test.mt.core.dto.ExecutionRequest;
import ru.sk.test.mt.core.dto.ExecutionResult;
import ru.sk.test.web.server.ApplicationConfig;
import ru.sk.test.web.controller.RestController;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class RestfulServiceTest extends JerseyTest {

    private static final Logger logger = Logger.getLogger(RestfulServiceTest.class);

    private Gson gson = new Gson();

    private static final String ZERO_USD_BALANCE = "USD 0.00";

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.register(RestController.class);
        return applicationConfig;
    }

    @Test
    public void testMoneyTransferParallel() {
        final List<Callable<ExecutionResult>> depositRequests = getDepositRequests();
        final List<Callable<ExecutionResult>> transferRequests = getTransferRequests();
        final List<Callable<ExecutionResult>> withdrawRequests = getWithdrawRequests();
        try {
            final ExecutorService executor = Executors.newFixedThreadPool(10);
            executor.invokeAll(depositRequests);
            executor.invokeAll(transferRequests);
            executor.invokeAll(withdrawRequests);
            ExecutionResult result1Dto = executor.submit(() -> executeGetRequest("accountBalance/40807810400134")).get();
            ExecutionResult result2Dto = executor.submit(() -> executeGetRequest("accountBalance/40807810402352")).get();
            ExecutionResult result3Dto = executor.submit(() -> executeGetRequest("accountBalance/40807810407547")).get();
            assertEquals((ZERO_USD_BALANCE), result1Dto.getMessage().toUpperCase());
            assertEquals((ZERO_USD_BALANCE), result2Dto.getMessage().toUpperCase());
            assertEquals((ZERO_USD_BALANCE), result3Dto.getMessage().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Callable<ExecutionResult>> getTransferRequests() {
        final List<Callable<ExecutionResult>> transferRequests = new ArrayList<>();
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810400134L, 40807810402352L, BigDecimal.valueOf(10.25))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810402352L, 40807810400134L, BigDecimal.valueOf(20))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810407547L, 40807810400134L, BigDecimal.valueOf(5.50))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810400134L, 40807810402352L, BigDecimal.valueOf(15))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810400134L, 40807810407547L, BigDecimal.valueOf(10.75))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810400134L, 40807810402352L, BigDecimal.valueOf(5.25))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810402352L, 40807810400134L, BigDecimal.valueOf(15.50))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810407547L, 40807810400134L, BigDecimal.valueOf(10))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810402352L, 40807810400134L, BigDecimal.valueOf(25))));
        transferRequests.add(() -> executePostRequest("transfer", transferExecutionRq(40807810402352L, 40807810407547L, BigDecimal.valueOf(25))));
        return transferRequests;
    }

    private List<Callable<ExecutionResult>> getDepositRequests() {
        final List<Callable<ExecutionResult>> depositRequests = new ArrayList<>();
        depositRequests.add(() -> executePostRequest("deposit", depositExecutionRq(40807810400134L, BigDecimal.valueOf(100.25))));
        depositRequests.add(() -> executePostRequest("deposit", depositExecutionRq(40807810402352L, BigDecimal.valueOf(500.75))));
        depositRequests.add(() -> executePostRequest("deposit", depositExecutionRq(40807810407547L, BigDecimal.valueOf(200.50))));
        return depositRequests;
    }

    private List<Callable<ExecutionResult>> getWithdrawRequests() {
        final List<Callable<ExecutionResult>> depositRequests = new ArrayList<>();
        depositRequests.add(() -> executePostRequest("withdraw", withdrawExecutionRq(40807810400134L, BigDecimal.valueOf(135.00))));
        depositRequests.add(() -> executePostRequest("withdraw", withdrawExecutionRq(40807810402352L, BigDecimal.valueOf(445.75))));
        depositRequests.add(() -> executePostRequest("withdraw", withdrawExecutionRq(40807810407547L, BigDecimal.valueOf(220.75))));
        return depositRequests;
    }

    private ExecutionResult executeGetRequest(String url) {
        logger.info("Request is being sent: " + url);
        Response output = target(url).request().get();
        if (output.getStatus() == 200) {
            String readEntity = output.readEntity(String.class);
            logger.info("Got response: " + readEntity);
            return gson.fromJson(readEntity, ExecutionResult.class);
        } else {
            throw new IllegalStateException("Code is different");
        }
    }

    private ExecutionResult executePostRequest(String url, ExecutionRequest executionRequest) {
        logger.info("Request is being sent: " + url +" | params: " + gson.toJson(executionRequest));
        Response output = target(url).request().post(Entity.json(executionRequest), Response.class);
        if (output.getStatus() == 200) {
            String readEntity = output.readEntity(String.class);
            logger.info("Got response: " + readEntity);
            return gson.fromJson(readEntity, ExecutionResult.class);
        } else {
            throw new IllegalStateException("Code is different");
        }
    }

    private ExecutionRequest transferExecutionRq(long fromAccNum, long toAccNum, BigDecimal amount) {
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setAmount(amount);
        executionRequest.setFromAccountNumber(fromAccNum);
        executionRequest.setToAccountNumber(toAccNum);
        return executionRequest;
    }

    private ExecutionRequest depositExecutionRq(long toAccNum, BigDecimal amount) {
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setAmount(amount);
        executionRequest.setToAccountNumber(toAccNum);
        return executionRequest;
    }

    private ExecutionRequest withdrawExecutionRq(long fromAccNum, BigDecimal amount) {
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setAmount(amount);
        executionRequest.setFromAccountNumber(fromAccNum);
        return executionRequest;
    }



}
