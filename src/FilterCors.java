package src;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
//@PreMatching
public class FilterCors implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request) {
        if (isPreflightRequest(request)) {
            request.abortWith(Response.ok().build());
        } else {
            request.getHeaders().add("Access-Control-Allow-Credentials", "true");
            request.getHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            request.getHeaders().add("Access-Control-Allow-Headers",
                    // put it in this list. And remove the ones you don't want.
                    "X-Requested-With, Authorization, " +
                            "Accept-Version, Content-MD5, CSRF-Token, Content-Type");


            request.getHeaders().add("Access-Control-Allow-Origin", "*");
        }
    }

    private static boolean isPreflightRequest(ContainerRequestContext request) {
        return request.getHeaderString("Origin") != null
                && request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {

        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.getHeaders().add("Access-Control-Allow-Headers",
                // put it in this list. And remove the ones you don't want.
                "X-Requested-With, Authorization, " +
                        "Accept-Version, Content-MD5, CSRF-Token, Content-Type");


        response.getHeaders().add("Access-Control-Allow-Origin", "*");
    }
}