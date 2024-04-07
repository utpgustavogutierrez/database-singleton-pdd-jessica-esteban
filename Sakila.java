import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private Connection connection;

    private DatabaseSingleton() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:./sqlite-sakila.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // DatabaseSingleton es nuestro patrón Singleton
    // Es una creación de una única instancia de conexión de base de datos que se puede compartir en toda la aplicación.
    public static DatabaseSingleton getInstance() {
        if (instance == null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

public class Sakila {
    public static void main(String[] args) {
        try {
            // Obtener la instancia de la conexión de la base de datos
            DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstance();
            Connection connection = databaseSingleton.getConnection();

            // NOTE: Connection and Statement are AutoClosable.
            //       Don't forget to close them both in order to avoid leaks.
            try (Statement statement = connection.createStatement()) {
                statement.setQueryTimeout(30);  // set timeout to 30 sec.

                ResultSet rs = statement.executeQuery("select * from film");
                while (rs.next()) {
                    // read the result set
                    System.out.println("title = " + rs.getString("title"));
                }
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        }
    }
}
