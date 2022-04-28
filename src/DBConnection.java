package src;


import org.apache.derby.iapi.jdbc.AutoloadedDriver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    private DBConnection(){}

    public static Connection get() {
        if(connection == null) {
            Driver derbyEmbeddedDriver = new AutoloadedDriver();
            try {
                DriverManager.registerDriver(derbyEmbeddedDriver);
                connection = DriverManager.getConnection
                        ("jdbc:derby:testdb1;create=true");
                connection.setSchema("APP");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return connection;
    }

    public static void dropDatabaseContent() throws SQLException {
        try(var con = get().createStatement()){
            con.execute("TRUNCATE TABLE schueler");
        }
    }

    public static void fillWithTestData() throws SQLException {
        User user = new User();
        user.setBenutzername("admin");
        user.setPasswort("admin1");
        user.setNachname("admin");
        user.setVorname("admin");
        new UserResource().addUser(user);
    }
}
