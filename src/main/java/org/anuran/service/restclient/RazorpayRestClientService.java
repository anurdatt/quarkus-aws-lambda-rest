package org.anuran.service.restclient;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.anuran.model.RzpCreateOrderRequest;
import org.anuran.model.RzpCreateOrderResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import io.quarkus.rest.client.reactive.ClientBasicAuth;


@Path("/orders")
@RegisterRestClient(configKey = "razorpay-api")
@RegisterClientHeaders
@ClientBasicAuth(username = "${razorpay.apikey}", password = "${razorpay.apisecret}")
public interface RazorpayRestClientService {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    JsonNode createOrder(RzpCreateOrderRequest request);
}
