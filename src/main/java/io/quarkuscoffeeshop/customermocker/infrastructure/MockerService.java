package io.quarkuscoffeeshop.customermocker.infrastructure;

import com.google.common.base.Joiner;
import io.quarkuscoffeeshop.customermocker.domain.CustomerMocker;
import io.quarkuscoffeeshop.customermocker.domain.CustomerVolume;
import io.quarkuscoffeeshop.domain.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Creates and sends CreateOrderCommand objects to the web application
 */
@ApplicationScoped
public class MockerService {

    final Logger logger = LoggerFactory.getLogger(MockerService.class);

    private boolean running;

    int counter;

    CustomerVolume customerVolume = CustomerVolume.SLOW;

    @Inject
    @RestClient
    RESTService RESTService;

    @Inject
    CustomerMocker customerMocker;

    Runnable sendMockOrders = () -> {
        logger.debug("CustomerMocker now running");

        while (running) {
            if(!running) logger.info("CustomerMocker now stopping");
            try {
                Thread.sleep(customerVolume.getDelay() * 1000);
                int orders = new Random().nextInt(4);
                if (orders >= 1) {
                    List<PlaceOrderCommand> placeOrderCommands = customerMocker.mockCustomerOrders(orders);
                    logger.debug("placing {} orders", placeOrderCommands.size());
                    placeOrderCommands.forEach(placeOrderCommand -> {
                        logger.debug(placeOrderCommand.toString());
                    });
                    placeOrders(placeOrderCommands);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private CompletionStage<Void> placeOrders(final List<PlaceOrderCommand> placeOrderCommands) {

        Collection<CompletionStage<Response>> results = new ArrayList<>(placeOrderCommands.size());
        placeOrderCommands.forEach(placeOrderCommand -> {
            results.add(RESTService.placeOrders(placeOrderCommand));
        });
        return null;
    }

    public CompletionStage<Void> start() {
        this.running = true;
        return CompletableFuture.runAsync(sendMockOrders);
    }

    public void stop() {
        this.running = false;
        logger.debug("CustomerMocker now stopped");
    }

    public void setToDev() {
        this.customerVolume = CustomerVolume.DEV;
    }

    public void setToSlow() {
        this.customerVolume = CustomerVolume.SLOW;
    }

    public void setToModerate() {
        this.customerVolume = CustomerVolume.MODERATE;
    }

    public void setToBusy() {
        this.customerVolume = CustomerVolume.BUSY;
    }

    public void setToWeeds() {
        this.customerVolume = CustomerVolume.WEEDS;
    }

    //--------------------------------------------------
    public boolean isRunning() {
        return running;
    }



}
