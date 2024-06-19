package org.anuran.service.restclient;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.anuran.model.RzpCreateOrderRequest;
import org.anuran.model.RzpCreateOrderResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/orders")
@RegisterRestClient(configKey = "razorpay-api")
public interface RazorpayRestClientService {
    @POST
    RzpCreateOrderResponse createOrder(RzpCreateOrderRequest request);
}
