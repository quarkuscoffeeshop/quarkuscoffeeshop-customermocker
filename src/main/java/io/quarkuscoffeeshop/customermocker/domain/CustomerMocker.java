package io.quarkuscoffeeshop.customermocker.domain;

import io.quarkuscoffeeshop.domain.*;
import io.quarkuscoffeeshop.customermocker.infrastructure.RESTService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.quarkuscoffeeshop.customermocker.domain.JsonUtil.toJson;

@ApplicationScoped
public class CustomerMocker {

    final Logger logger = LoggerFactory.getLogger(CustomerMocker.class);

    @Inject
    @RestClient
    RESTService restService;

    private boolean running;

    int counter;

    CustomerVolume customerVolume = CustomerVolume.SLOW;

    Runnable sendMockOrders = () -> {
        logger.debug("CustomerMocker now running");

        while (running) {
            try {
                Thread.sleep(customerVolume.getDelay() * 1000);
                int orders = new Random().nextInt(4);
                List<PlaceOrderCommand> mockOrders = mockCustomerOrders(orders);
                logger.debug("placing orders");
                mockOrders.forEach(mockOrder -> {
                    restService.placeOrders(mockOrder);
                    logger.debug("placed order: {}", toJson(mockOrder));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public CompletableFuture<Void> start() {
        this.running = true;
        return CompletableFuture.runAsync(sendMockOrders);
    }

    public void stop() {
        this.running = false;
        logger.debug("CustomerMocker now stopped");
    }

    public List<PlaceOrderCommand> mockCustomerOrders(int desiredNumberOfOrders) {

        return Stream.generate(() -> {
            PlaceOrderCommand placeOrderCommand;
            if (counter == 100) {
                logger.debug("sending a remake");
                placeOrderCommand = new PlaceOrderCommand(
                    OrderSource.WEB,
                        Arrays.asList(new OrderLineItem(Item.CAPPUCCINO, BigDecimal.valueOf(3.50), "Lemmy")),
                    null,
                    BigDecimal.valueOf(3.50)
                );
            }else{
                placeOrderCommand = new PlaceOrderCommand(
                        OrderSource.WEB,
                        Arrays.asList(new OrderLineItem(Item.CAPPUCCINO, BigDecimal.valueOf(3.50), "Lemmy")),
                        null,
                        BigDecimal.valueOf(3.50)
                );
                // not all orders have kitchen items
                if (desiredNumberOfOrders % 2 == 0) {
                    placeOrderCommand.getKitchenItems().get().addAll(createKitchenItems());
                }
                if(placeOrderCommand.getBaristaItems().isPresent()){
                    counter += placeOrderCommand.getBaristaItems().get().size();
                }
                if(placeOrderCommand.getKitchenItems().isPresent()){
                    counter += placeOrderCommand.getKitchenItems().get().size();
                }
                logger.debug("current order count: {}", counter);
                return placeOrderCommand;
            }
            return placeOrderCommand;
        }).limit(desiredNumberOfOrders).collect(Collectors.toList());
    }

    private Collection<OrderLineItem> createBeverages() {

        List<OrderLineItem> beverages = new ArrayList(2);
        beverages.add(new OrderLineItem(randomBaristaItem(), BigDecimal.valueOf(4.0), randomCustomerName()));
        beverages.add(new OrderLineItem(randomBaristaItem(), BigDecimal.valueOf(3.5),randomCustomerName()));
        return beverages;
    }

    private Collection<OrderLineItem> createKitchenItems() {
        List<OrderLineItem> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new OrderLineItem(randomKitchenItem(), BigDecimal.valueOf(3.75), randomCustomerName()));
        kitchenOrders.add(new OrderLineItem(randomKitchenItem(), BigDecimal.valueOf(3.5), randomCustomerName()));
        return kitchenOrders;
    }

    Item randomBaristaItem() {
        return Item.values()[new Random().nextInt(5)];
    }

    Item randomKitchenItem() {
        return Item.values()[new Random().nextInt(3) + 5];
    }

    String randomCustomerName() {
        return CustomerNames.randomName();
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
