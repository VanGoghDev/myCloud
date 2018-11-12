package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ClientRepository implements IRepository<User> {

    Connection conn;
    PreparedStatement st;

    /*public boolean isLogged(User entity) {
        Cursor cursor = null;
        String sql = "SELECT LOGIN FROM CLIENT WHERE LOGIN=" + entity.login;
        cursor = db.rawQuery(sql, null);
        try {
            conn = MyConnection.getConnection();
            cursor = conn.
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/
    /*public boolean isLogged(User entity) {
        boolean found = true;
        String sql = "SELECT LOGIN, PASSWORD FROM CLIENT WHERE LOGIN = ? AND PASSWORD = ?";
        try {
            conn = MyConnection.getConnection();
            st = conn.prepareStatement(sql);
            st.setString(1, entity.login);
            st.setString(2, entity.password);

            ResultSet rs = st.executeQuery();
            found = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return found;
    }*/

    @Override
    public void insert(User entity) {
        String sql = "INSERT INTO CLIENT (ID, NAME, AGE, LOGIN, PASSWORD) VALUES(?, ?, ?, ?, ?)";
        try {
            conn = MyConnection.getConnection();
            st = conn.prepareStatement(sql);
            st.setInt(1, entity.id);
            st.setString(2, entity.name);
            st.setInt(3, entity.age);
            st.setString(4, entity.login);
            st.setString(5, entity.password);
            st.addBatch();
            st.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public List<User> get() {
        return null;
    }
}
