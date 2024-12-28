package team.project.faceaccess.metier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnexionDB {

    private static Connection connection;

    private SingletonConnexionDB() {}

    public static Connection getConnexion()  {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/team/project/faceaccess/database/faceaccess.db");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
