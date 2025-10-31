package prog2int.Config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public final class DatabaseConnection {

    private static final Properties PROPS = new Properties();
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static boolean AUTO_COMMIT;

    static {
        // 1) Cargar properties desde resources
        try (InputStream in = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (in == null) {
                throw new IllegalStateException("No se encontró database.properties en resources/");
            }
            PROPS.load(in);

            URL = Objects.requireNonNull(PROPS.getProperty("db.url"), "db.url es requerido");
            USER = PROPS.getProperty("db.user", "");
            PASSWORD = PROPS.getProperty("db.password", "");
            AUTO_COMMIT = Boolean.parseBoolean(PROPS.getProperty("db.autocommit", "true"));

            // 2) (Opcional en Java 9+) Forzar carga del driver
            // Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo database.properties", e);
        }
    }

    private DatabaseConnection() {}

    /** Retorna una Connection lista para usar. */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(AUTO_COMMIT);
        return conn;
    }
}
