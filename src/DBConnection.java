package src;


import org.apache.derby.iapi.jdbc.AutoloadedDriver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static void main(String[] args) throws SQLException {
        Driver derbyEmbeddedDriver = new AutoloadedDriver();
        DriverManager.registerDriver(derbyEmbeddedDriver);
        Connection conn = DriverManager.getConnection
                ("jdbc:derby:testdb1;create=true");
        conn.setAutoCommit(false);
    }

}
