package io.quarkuscoffeeshop.customermocker.domain;

import io.quarkuscoffeeshop.domain.*;
import io.quarkuscoffeeshop.customermocker.infrastructure.RESTService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
                List<OrderPlacedEvent> mockOrders = mockCustomerOrders(orders);
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

    public List<OrderPlacedEvent> mockCustomerOrders(int desiredNumberOfOrders) {

        return Stream.generate(() -> {
            if (counter == 100) {
                logger.debug("sending a remake");
                OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
                orderPlacedEvent.id = UUID.randomUUID().toString();
                orderPlacedEvent.setOrderSource(OrderSource.REMAKE);
                orderPlacedEvent.setBeverages(Arrays.asList(new LineItem(Item.CAPPUCCINO, "Lemmy")));
                return orderPlacedEvent;
            }else{
                OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
                orderPlacedEvent.id = UUID.randomUUID().toString();
                orderPlacedEvent.setOrderSource(OrderSource.WEB);
                orderPlacedEvent.beverages = createBeverages();
                // not all orders have kitchen items
                if (desiredNumberOfOrders % 2 == 0) {
                    orderPlacedEvent.kitchenOrders = createKitchenItems();
                }
                counter += orderPlacedEvent.getBeverages().size() + orderPlacedEvent.getKitchenOrders().size();
                logger.debug("current order count: {}", counter);
                return orderPlacedEvent;
            }
        }).limit(desiredNumberOfOrders).collect(Collectors.toList());
    }

    private List<LineItem> createBeverages() {

        List<LineItem> beverages = new ArrayList(2);
        beverages.add(new LineItem(randomBaristaItem(), randomCustomerName()));
        beverages.add(new LineItem(randomBaristaItem(), randomCustomerName()));
        return beverages;
    }

    private List<LineItem> createKitchenItems() {
        List<LineItem> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new LineItem(randomKitchenItem(), randomCustomerName()));
        kitchenOrders.add(new LineItem(randomKitchenItem(), randomCustomerName()));
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
