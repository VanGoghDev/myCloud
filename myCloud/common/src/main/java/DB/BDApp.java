package DB;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class BDApp {

    String DB_PATH = "DB.db";
    private static final Logger LOGGER = Logger.getLogger(BDApp.class.getSimpleName());
    public static Connection connection;
    public static PreparedStatement preparedStatement;
    public static ResultSet resultSet;

    public void connectionDB() {
        connection = null;
        try {
            connection = DriverManager.getConnection(JDBC.PREFIX + DB_PATH);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDB() {
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement
                    ("CREATE TABLE  if not exists clients (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName STRING, age INTEGER, login STRING, password STRING)");
            preparedStatement.execute();
            System.out.println("Table created");
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(User entity) {
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("INSERT INTO clients (firstName, age, login, password) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, entity.name);
            preparedStatement.setInt(2, entity.age);
            preparedStatement.setString(3, entity.login);
            preparedStatement.setString(4, entity.password);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("User added");
    }

    public void update(User entity) {
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("UPDATE PERSON set firstName = ?, age = ?, salary = ?, password = ? WHERE id = ?");
            preparedStatement.setString(1, entity.name);
            preparedStatement.setInt(2, entity.age);
            preparedStatement.setString(3, entity.login);
            preparedStatement.setString(4, entity.password);
            preparedStatement.setInt(5, entity.id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(int id, User entity) {
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("UPDATE PERSON set firstName = ?, age = ?, salary = ?, password = ? WHERE id = ?");
            preparedStatement.setString(1, entity.name);
            preparedStatement.setInt(2, entity.age);
            preparedStatement.setString(3, entity.login);
            preparedStatement.setString(4, entity.password);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRegistered(String login) {
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("SELECT login FROM clients WHERE login=?");
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isLogged(String login, String password) {
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("SELECT login, password FROM clients WHERE login=? and password=?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public User get(int id) {
        User user = null;
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("SELECT id, name, age, login, password FROM clients WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user = new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5));
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public List<User> get() {
        List<User> list = new LinkedList<>();
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("SELECT id, name, age, login, password FROM clients");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5)));
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void delete(int id) {
        String sql = "DELETE FROM PERSON WHERE id=?";

        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("DELETE FROM PERSON WHERE id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearTable() {
        try {
            connectionDB();
            preparedStatement = connection.prepareStatement("DELETE FROM clients");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
