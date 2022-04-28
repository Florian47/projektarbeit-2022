package src;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public Response getAll() throws SQLException {
        return Response.ok(getUsers()).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) throws SQLException {
        User user = new User();
        user.setId(id);
        User dbUser = getUserById(user);
        return Response.ok(dbUser).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User userToStore) throws SQLException {
        userToStore.setId(id);
        updateUser(userToStore);
        User dbUser = getUserById(userToStore);
        return Response.ok(dbUser).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) throws SQLException {
        User user = new User();
        user.setId(id);
        deleteUser(user);
        return Response.ok().build();
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
            return mapSingle(rs);
        }
    }
    public List<User> getUsers() throws SQLException {
        try (var con = DBConnection.get().createStatement()) {
            ResultSet rs = con.executeQuery("SELECT * FROM schueler");
            return map(rs);
        }
    }

    public User getUserById(User user) throws SQLException {
        try (var con = DBConnection.get().createStatement()) {
            ResultSet rs = con.executeQuery("SELECT * FROM schueler WHERE(id=%s)".formatted(user.getId()));
            return mapSingle(rs);
        }
    }

    private User mapSingle(ResultSet resourceResult) throws SQLException {
        List<User> list = map(resourceResult);
        if (list.size()==1) {
            return list.get(0);
        }
        return null;
    }
    private List<User> map(ResultSet resourceResult) throws SQLException {
        List<User> list = new ArrayList<>();
        while (resourceResult.next()) {
            User user = new User();
            user.setVorname(resourceResult.getString("firstname"));
            user.setNachname(resourceResult.getString("lastname"));
            user.setBenutzername(resourceResult.getString("username"));
            user.setPasswort(resourceResult.getString("password"));
            user.setId(resourceResult.getInt("id"));
            list.add(user);
        }
        return list;
    }

}
