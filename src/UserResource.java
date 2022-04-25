package src;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Path("/authenticate")
    public Response login(String login) {

        System.out.println(login);
        return null;
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id,
                                @QueryParam("benutzername") @DefaultValue("") String title) {
        // ...
        return null;
    }

    @POST
    public Response getUserById(User userToStore, @Context UriInfo uriInfo) {
        // ...
        return null;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id,
                               @HeaderParam("User-Agent") String userAgent) {
        // ...
        return null;
    }

}
