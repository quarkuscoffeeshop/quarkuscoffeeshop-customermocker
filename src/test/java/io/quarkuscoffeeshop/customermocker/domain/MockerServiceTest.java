package io.quarkuscoffeeshop.customermocker.domain;

import io.quarkuscoffeeshop.domain.OrderPlacedEvent;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkuscoffeeshop.domain.PlaceOrderCommand;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class MockerServiceTest {


    @Inject
    CustomerMocker customerMocker;

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testCustomerMocker() {

        List<PlaceOrderCommand> createOrderCommands = customerMocker.mockCustomerOrders(15);
        assertEquals(15, createOrderCommands.size());

        createOrderCommands.forEach(placeOrderCommand -> {
            System.out.println(jsonb.toJson(placeOrderCommand));
        });
    }
}
