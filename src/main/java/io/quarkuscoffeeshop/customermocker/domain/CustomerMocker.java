package io.quarkuscoffeeshop.customermocker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomerMocker {

    static final Logger logger = LoggerFactory.getLogger(CustomerMocker.class);

    static int counter;

    /*
        @Inject
        @RestClient
        RESTService restService;

        private boolean running;


        CustomerVolume customerVolume = CustomerVolume.SLOW;

        Runnable sendMockOrders = () -> {
            logger.debug("CustomerMocker now running");

            while (running) {
                try {
                    Thread.sleep(customerVolume.getDelay() * 1000);
                    int orders = new Random().nextInt(4);
                    List<PlaceOrderCommand> mockOrders = mockCustomerOrders(orders);
                    logger.debug("placing {} orders", mockOrders.size());
                    mockOrders.forEach(mockOrder -> {
                        logger.debug(mockOrder.toString());
                    });
                    placeOrders(mockOrders).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        CompletableFuture<Void> placeOrders(final List<PlaceOrderCommand> orders) {

            Collection<CompletableFuture<Void>> futures = new ArrayList<>(orders.size());
            orders.forEach(placeOrderCommand ->{
                futures.add(placeOrder(placeOrderCommand));
            });

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                    .exceptionally(e -> {
                        logger.error(e.getMessage());
                        return null;
                    });
        }

        CompletableFuture<Void> placeOrder(final PlaceOrderCommand placeOrderCommand) {
            return restService.placeOrders(placeOrderCommand)
                    .exceptionally(e -> {
                        logger.error(e.getMessage());
                        return null;
                    }).toCompletableFuture().thenApply(s -> {
                        logger.debug("sent {}", placeOrderCommand);
                        return null;
                    });
        }

        public CompletableFuture<Void> start() {
            this.running = true;
            return CompletableFuture.runAsync(sendMockOrders);
        }

        public void stop() {
            this.running = false;
            logger.debug("CustomerMocker now stopped");
        }

    */
    public static List<PlaceOrderCommand> mockCustomerOrders(final int desiredNumberOfOrders) {

        return Stream.generate(() -> {
            PlaceOrderCommand placeOrderCommand;
            if (counter == 100) {
                logger.debug("sending a remake");
                placeOrderCommand = new PlaceOrderCommand(
                        UUID.randomUUID().toString(),
                        "ATLANTA",
                        OrderSource.WEB,
                        null,
                        Arrays.asList(new OrderLineItem(Item.CAPPUCCINO, BigDecimal.valueOf(3.50), "Lemmy")),
                        null,
                        BigDecimal.valueOf(3.50)
                );
            } else {
                placeOrderCommand = new PlaceOrderCommand(
                        UUID.randomUUID().toString(),
                        "ATLANTA",
                        OrderSource.WEB,
                        null,
                        Arrays.asList(new OrderLineItem(Item.CAPPUCCINO, BigDecimal.valueOf(3.50), "Lemmy")),
                        null,
                        BigDecimal.valueOf(3.50)
                );
                // not all orders have kitchen items
                if (desiredNumberOfOrders % 2 == 0) {
                    placeOrderCommand.getKitchenItems().get().addAll(createKitchenItems());
                }
                if (placeOrderCommand.getBaristaItems().isPresent()) {
                    counter += placeOrderCommand.getBaristaItems().get().size();
                }
                if (placeOrderCommand.getKitchenItems().isPresent()) {
                    counter += placeOrderCommand.getKitchenItems().get().size();
                }
                logger.debug("current order count: {}", counter);
                return placeOrderCommand;
            }
            return placeOrderCommand;
        }).limit(desiredNumberOfOrders).collect(Collectors.toList());
    }

    private static Collection<OrderLineItem> createBeverages() {

        List<OrderLineItem> beverages = new ArrayList(2);
        beverages.add(new OrderLineItem(randomBaristaItem(), BigDecimal.valueOf(4.0), randomCustomerName()));
        beverages.add(new OrderLineItem(randomBaristaItem(), BigDecimal.valueOf(3.5), randomCustomerName()));
        return beverages;
    }

    private static Collection<OrderLineItem> createKitchenItems() {
        List<OrderLineItem> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new OrderLineItem(randomKitchenItem(), BigDecimal.valueOf(3.75), randomCustomerName()));
        kitchenOrders.add(new OrderLineItem(randomKitchenItem(), BigDecimal.valueOf(3.5), randomCustomerName()));
        return kitchenOrders;
    }

    static Item randomBaristaItem() {
        return Item.values()[new Random().nextInt(5)];
    }

    static Item randomKitchenItem() {
        return Item.values()[new Random().nextInt(3) + 5];
    }

    static String randomCustomerName() {
        return CustomerNames.randomName();
    }

}
