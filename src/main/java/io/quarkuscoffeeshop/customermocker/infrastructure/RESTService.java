package io.quarkuscoffeeshop.customermocker.infrastructure;

import io.quarkuscoffeeshop.customermocker.domain.PlaceOrderCommand;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("/order")
@RegisterRestClient(configKey="orkey")
@Produces(MediaType.APPLICATION_JSON)
public interface RESTService {

    @POST
    CompletionStage<Response> placeOrders(final PlaceOrderCommand placeOrderCommand);
}
