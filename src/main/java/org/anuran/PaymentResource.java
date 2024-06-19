package org.anuran;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.anuran.model.RzpCreateOrderRequest;
import org.anuran.model.RzpCreateOrderResponse;
import org.anuran.service.restclient.RazorpayRestClientService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParams;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;

@Path("/api/payments")
public class PaymentResource {

//    @Inject
//    @RestClient
    private final RazorpayRestClientService razorpayRestClientService;


//    @ConfigProperty(name = "RZP_URL", defaultValue = "https://api.razorpay.com/v1")
    private String rzpUrl = "https://api.razorpay.com/v1";
    public PaymentResource() {
        razorpayRestClientService = QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(rzpUrl))
                .clientHeadersFactory(ClientHeaderParam.)
                .build(RazorpayRestClientService.class);
    }

//    @GET
//    @Path("/orders")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Order> findAll(@QueryParam("user") String user) {
//        if (!StringUtil.isNullOrEmpty(user)) {
//            return orderService.findByUserEmail(user);
//        }
//        return orderService.findAll();
//    }
    


    @POST
    @Path("/create-order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RzpCreateOrderResponse createOrder(RzpCreateOrderRequest request) {
        return razorpayRestClientService.createOrder(request);
    }

}
