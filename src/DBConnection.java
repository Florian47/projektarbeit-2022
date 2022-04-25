package src;

import java.sql.*;

public class DBConnection {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/softwareprojekt2022", "postgres", "user");

            if (connection!=null) System.out.println("Connection OK");
            else System.out.println("Connection Failed");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
