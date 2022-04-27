package src;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Path("/authenticate")
    public Response login(User user) throws SQLException {
        User dbUser = getUserByUserNameAndPassword(user);
        if(dbUser != null) {
            return Response.ok(dbUser).build();
        }
        return Response.serverError().build();
    }

    @POST
    @Path("/register")
    public Response register(User user) throws SQLException {
        addUser(user);
        User dbUser = getUserByUserNameAndPassword(user);
        return login(dbUser);
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        // ...
        return null;
    }

    @POST
    public Response getUser(User userToStore) {
        // ...
        return null;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        // ...
        return null;
    }

    public void addUser(User user) throws SQLException {
        try (var con = DBConnection.get().createStatement()) {
            String sql = "INSERT INTO SCHUELER (firstname, lastname, username, password) VALUES ('%s', '%s', '%s', '%s')".formatted(user.getVorname(), user.getNachname(), user.getBenutzername(), user.getPasswort());
            con.execute(sql);
        }
    }

    public void updateUser(User user) throws SQLException {
        try (var con = DBConnection.get().createStatement()) {
            String sql = "UPDATE schueler SET firstname = '%s', lastname = '%s', username = '%s', password = '%s' WHERE id = %s".formatted(user.getVorname(), user.getNachname(), user.getBenutzername(), user.getPasswort(), user.getId());
            con.execute(sql);
        }
    }

    public void deleteUser(User user) throws SQLException {
        try (var con = DBConnection.get().createStatement()) {
            con.execute("DELETE FROM schueler WHERE id=%s".formatted(user.getId()));
        }
    }

    public User getUserByUserNameAndPassword(User user) throws SQLException {
        try (var con = DBConnection.get().createStatement()) {
            ResultSet rs = con.executeQuery("SELECT * FROM schueler WHERE(username='%s' AND password='%s')".formatted(user.getBenutzername(), user.getPasswort()));
            return map(rs);
        }
    }
    public User getUserById(User user) throws SQLException {
        try (var con = DBConnection.get().createStatement()) {
            ResultSet rs = con.executeQuery("SELECT * FROM schueler WHERE(id=%s)".formatted(user.getId()));
            return map(rs);
        }
    }
    
    private User map(ResultSet resourceResult) throws SQLException {
        if (resourceResult.next()) {
            User user = new User();
            user.setVorname(resourceResult.getString("firstname"));
            user.setNachname(resourceResult.getString("lastname"));
            user.setBenutzername(resourceResult.getString("username"));
            user.setPasswort(resourceResult.getString("password"));
            user.setId(resourceResult.getInt("id"));
            return user;
        }
        return null;
    }

}
