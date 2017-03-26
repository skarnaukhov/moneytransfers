package ru.sk.test.web;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import ru.sk.test.mt.core.dto.ExecutionResult;
import ru.sk.test.web.server.ApplicationConfig;
import ru.sk.test.web.controller.RestController;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
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
        try {
            final ExecutorService executor = Executors.newFixedThreadPool(10);
            executor.invokeAll(depositRequests);
            executor.invokeAll(transferRequests);
            ExecutionResult result1Dto = executor.submit(() -> executeGetRequest("accountBalance/40807810400134")).get();
            ExecutionResult result2Dto = executor.submit(() -> executeGetRequest("accountBalance/40807810402352")).get();
            ExecutionResult result3Dto = executor.submit(() -> executeGetRequest("accountBalance/40807810407547")).get();
            assertEquals(result1Dto.getMessage().toUpperCase(),("USD 135.00"));
            assertEquals(result2Dto.getMessage().toUpperCase(),("USD 445.75"));
            assertEquals(result3Dto.getMessage().toUpperCase(),("USD 220.75"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Callable<ExecutionResult>> getTransferRequests() {
        final List<Callable<ExecutionResult>> transferRequests = new ArrayList<>();
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810400134/to/40807810402352/amount/10.25"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810402352/to/40807810400134/amount/20"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810407547/to/40807810400134/amount/5.50"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810400134/to/40807810402352/amount/15"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810400134/to/40807810407547/amount/10.75"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810400134/to/40807810402352/amount/5.25"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810402352/to/40807810400134/amount/15.50"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810407547/to/40807810400134/amount/10"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810402352/to/40807810400134/amount/25"));
        transferRequests.add(() -> executeGetRequest("transfer/from/40807810402352/to/40807810407547/amount/25"));
        return transferRequests;
    }

    private List<Callable<ExecutionResult>> getDepositRequests() {
        final List<Callable<ExecutionResult>> depositRequests = new ArrayList<>();
        depositRequests.add(() -> executeGetRequest("deposit/40807810400134/amount/100.25"));
        depositRequests.add(() -> executeGetRequest("deposit/40807810402352/amount/500.75"));
        depositRequests.add(() -> executeGetRequest("deposit/40807810407547/amount/200.50"));
        return depositRequests;
    }

    private ExecutionResult executeGetRequest(String url) {
        logger.info("Request is being sent: " + url);
        Response output = target(url).request().get();
        if (output.getStatus() == 200) {
            String readEntity = output.readEntity(String.class);
            return gson.fromJson(readEntity, ExecutionResult.class);
        } else {
            throw new IllegalStateException("Code is different");
        }
    }



}
