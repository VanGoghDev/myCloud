package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateClientCatalog {

    static Connection conn;
    static Statement st;

    public void ConnectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:dbjava");
            String sql = "CREATE TABLE IF NOT EXISTS CLIENT(\n" +
                    "   ID INT PRIMARY KEY     NOT NULL,\n" +
                    "   NAME           TEXT    NOT NULL,\n" +
                    "   AGE            INT     NOT NULL,\n" +
                    "   LOGIN         TEXT     NOT NULL, \n" +
                    "   PASSWORD      TEXT     NOT NULL) ";
            st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try{
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

