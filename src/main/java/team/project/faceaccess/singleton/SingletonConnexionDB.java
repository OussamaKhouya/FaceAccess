package team.project.faceaccess.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnexionDB {
    private static Connection connection;

    private SingletonConnexionDB() {}


    public static Connection getConnexion() {
        if (connection == null) {
            try {
                String dbUrl = "jdbc:sqlite:faceaccess.db";  // For local SQLite file
                connection = DriverManager.getConnection(dbUrl);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
