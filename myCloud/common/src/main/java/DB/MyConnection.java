package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    static Connection conn;

    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:dbjava");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
