package io.quarkuscoffeeshop.customermocker.domain;

import io.quarkuscoffeeshop.domain.Item;
import io.quarkuscoffeeshop.domain.OrderLineItem;
import io.quarkuscoffeeshop.domain.OrderSource;
import io.quarkuscoffeeshop.domain.PlaceOrderCommand;
import io.quarkuscoffeeshop.customermocker.domain.JsonUtil;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.math.BigDecimal;
import java.util.Arrays;

public class JsonSerializationTest {

    Jsonb localJsonb = JsonbBuilder.create();


    @Test
    public void testOptionalSerialization() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                OrderSource.WEB,
                null,
                Arrays.asList(new OrderLineItem(Item.CAPPUCCINO, BigDecimal.valueOf(3.50), "Lemmy")),
                null,
                BigDecimal.valueOf(3.50)
        );

        System.out.println(localJsonb.toJson(placeOrderCommand));
        System.out.println(JsonUtil.toJson(placeOrderCommand));

        PlaceOrderCommand resultingPlaceOrderCommand = localJsonb.fromJson("{\"baristaItems\":[{\"item\":\"CAPPUCCINO\",\"name\":\"Lemmy\",\"price\":3.5}],\"commandType\":\"PLACE_ORDER\",\"id\":\"420df3f2-3ba1-4d3d-9373-c7ee19fe3dff\",\"orderSource\":\"WEB\",\"total\":3.5}", PlaceOrderCommand.class);
        System.out.println(resultingPlaceOrderCommand.toString());
    }
}
