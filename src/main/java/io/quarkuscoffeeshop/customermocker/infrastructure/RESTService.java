package io.quarkuscoffeeshop.customermocker.infrastructure;

import io.quarkuscoffeeshop.domain.OrderPlacedEvent;
import io.quarkuscoffeeshop.domain.PlaceOrderCommand;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("/order")
@RegisterRestClient(configKey="orkey")
public interface RESTService {

    @POST
    CompletionStage<Response> placeOrders(PlaceOrderCommand placeOrderCommand);
}
