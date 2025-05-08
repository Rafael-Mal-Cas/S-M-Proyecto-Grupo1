package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Muy importante
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/testweb", "root", ""
        );
    }
}