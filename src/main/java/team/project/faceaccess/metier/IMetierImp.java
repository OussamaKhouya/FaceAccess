package team.project.faceaccess.metier;

import team.project.faceaccess.utils.Constants;
import team.project.faceaccess.utils.Helpers;
import team.project.faceaccess.models.AccessLog;
import team.project.faceaccess.models.Admin;
import team.project.faceaccess.models.User;
import team.project.faceaccess.singleton.SingletonConnexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMetierImp implements IMetier {
    // get all the users (action by the Admin)
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = SingletonConnexionDB.getConnexion();

        try {
            String query = "SELECT id, firstName, lastName, access, door, registredDate, sex FROM Users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setDoor(resultSet.getString("door"));
                user.setRegistredDate(resultSet.getInt("registredDate"));
                user.setSex(resultSet.getString("sex"));
                user.setAccess(resultSet.getBoolean("access"));
                users.add(user);
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public List<User> getUsersByKeyword(String keyword) {
        return List.of();
    }

    @Override
    public Void changePassword(String oldPassword, String newPassword) {
        return null;
    }

    @Override
    public void deleteUser(int id) {
        Connection connection = SingletonConnexionDB.getConnexion();

        try {
            String query = "DELETE FROM Users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Add a user ( action to be made by the Admin)
    @Override
    public void addUser(User user) {
        Connection connection = SingletonConnexionDB.getConnexion();

        try {
            String query = "INSERT INTO Users (id, firstName, lastName, access, door, registredDate, sex) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setBoolean(4, user.getAccess());
            preparedStatement.setString(5, user.getDoor());
            preparedStatement.setInt(6, user.getRegistredDate());
            preparedStatement.setString(7, user.getSex());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Add Success");

    }

    @Override
    public void addAdmin(Admin admin) {
        Connection connection = SingletonConnexionDB.getConnexion();
        try {
            String query = "INSERT INTO Admin (username,email,password) VALUES ( ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getEmail());
            preparedStatement.setString(3, admin.getPassword());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Admin getAdmin() {
        Connection connection = SingletonConnexionDB.getConnexion();
        Admin admin = new Admin();
        try {
            String query = "select * from Admin";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                admin.setId(resultSet.getInt("id"));
                admin.setUsername(resultSet.getString("username"));
                admin.setEmail(resultSet.getString("email"));
                admin.setPassword(resultSet.getString("password"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public void updateUser(User user) {
        Connection connection = SingletonConnexionDB.getConnexion();

        try {
            //String query = "UPDATE Users SET firstName = ?, lastName = ?, access = ? WHERE id = ?";
            String query = "UPDATE Users SET firstName = ?, lastName = ?, access = ?, door = ?, registredDate = ?, sex = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setBoolean(3, user.getAccess());
            preparedStatement.setString(4, user.getDoor());
            preparedStatement.setInt(5, user.getRegistredDate());
            preparedStatement.setString(6, user.getSex());
            preparedStatement.setInt(7, user.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void generateRaport(User user) {

    }

    @Override
    public void manageAccess(User user) {

    }

    @Override
    public void viewStatistics(User user) {

    }

    @Override
    public void viewGeneralStatistics() {

    }

    public int getTotalUsers(String duration, boolean hasAccess) {
        Connection connection = SingletonConnexionDB.getConnexion();
        int residents = 0;
        String accessGranted = hasAccess ? "1" : "0";
        String timestamp;
        if (duration.equals(Constants.DURATION_DAY)) {
            timestamp = Helpers.getDateTimeatStartOfDay();
        } else if (duration.equals(Constants.DURATION_WEEK)) {
            timestamp = Helpers.getDateTimeAtStartOfWeek();
        } else {
            timestamp = Helpers.getDateTimeatStartOfMonth();
        }
        String query = "";
        try {

            query = "select count(*) from AccessLog where accessGranted = ? and timestamp > ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, accessGranted);
            preparedStatement.setString(2, timestamp);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(query);
            System.out.println(timestamp);
            while (resultSet.next()) {
                residents = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // One record in Users tables is reserved for unknown users
        return residents ;
    }


    public Map<String, Integer> getTotalVisits(boolean hasAccess) {
        Connection connection = SingletonConnexionDB.getConnexion();
        String accessGranted = hasAccess ? "1" : "0";
        String query = "";
        Map<String, Integer> accessCounts = new HashMap<>();
        try {
            query = "SELECT\n" +
                    "    SUM(CASE WHEN timestamp >= DATE('now') THEN 1 ELSE 0 END) AS today,\n" +
                    "    SUM(CASE WHEN timestamp >= DATE('now', '-1 day') AND timestamp < DATE('now') THEN 1 ELSE 0 END) AS today1,\n" +
                    "    SUM(CASE WHEN timestamp >= DATE('now', '-2 day') AND timestamp < DATE('now', '-1 days') THEN 1 ELSE 0 END) AS today2,\n" +
                    "    SUM(CASE WHEN timestamp >= DATE('now', '-3 days') AND timestamp < DATE('now', '-2 days') THEN 1 ELSE 0 END) AS today3,\n" +
                    "    SUM(CASE WHEN timestamp >= DATE('now', '-4 days') AND timestamp < DATE('now', '-3 days') THEN 1 ELSE 0 END) AS today4,\n" +
                    "    SUM(CASE WHEN timestamp >= DATE('now', '-5 days') AND timestamp < DATE('now', '-4 days') THEN 1 ELSE 0 END) AS today5,\n" +
                    "    SUM(CASE WHEN timestamp >= DATE('now', '-6 days') AND timestamp < DATE('now', '-5 days') THEN 1 ELSE 0 END) AS today6\n" +
                    "FROM AccessLog\n" +
                    "WHERE accessGranted = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, accessGranted);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                accessCounts.put("today", rs.getInt("today"));
                accessCounts.put("today1", rs.getInt("today1"));
                accessCounts.put("today2", rs.getInt("today2"));
                accessCounts.put("today3", rs.getInt("today3"));
                accessCounts.put("today4", rs.getInt("today4"));
                accessCounts.put("today5", rs.getInt("today5"));
                accessCounts.put("today6", rs.getInt("today6"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // One record in Users tables is reserved for unknown users
        return accessCounts;
    }

    @Override
    public void addLog(AccessLog log) {
        Connection connection = SingletonConnexionDB.getConnexion();
        String query = "INSERT INTO AccessLog (userId, timestamp, accessGranted) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, log.getUser().getId()); // Set the user ID

            // Get current timestamp and format it
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_PATTERN);
            String formattedTimestamp = now.format(formatter);

            pstmt.setString(2, formattedTimestamp); // Use the formatted current timestamp
            pstmt.setBoolean(3, log.isAccessGranted()); // Set the accessGranted flag

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteLog(AccessLog log) {
        Connection connection = SingletonConnexionDB.getConnexion();
        String query = "DELETE FROM AccessLog WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, log.getId()); // Set the ID of the log to delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<AccessLog> getLogs() {
        Connection connection = SingletonConnexionDB.getConnexion();
        String query = "SELECT * FROM AccessLog";
        List<AccessLog> logs = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                AccessLog log = new AccessLog();
                log.setId(rs.getInt("id"));

                // Fetch the User by ID
                log.setUser(fetchUserById(rs.getInt("userId")));

                log.setTimestamp(LocalDateTime.parse(rs.getString("timestamp"), DateTimeFormatter.ofPattern(Constants.DATETIME_PATTERN)));
                log.setAccessGranted(rs.getBoolean("accessGranted"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public User fetchUserById(int userId) {
        Connection connection = SingletonConnexionDB.getConnexion();
        String query = "SELECT * FROM Users WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setAccess(rs.getBoolean("access"));
                    user.setDoor(rs.getString("door"));
                    user.setRegistredDate(rs.getInt("registredDate"));
                    user.setSex(rs.getString("sex"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user not found or in case of error
    }

    @Override
    public List<String> getAllDoors() throws SQLException {
        List<String> doors = new ArrayList<>();
        String query = "SELECT DISTINCT door FROM users";
        Connection connection = SingletonConnexionDB.getConnexion();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            doors.add(resultSet.getString("door"));
        }


        return doors;
    }

    @Override
    public List<User> getUsersByDoor(String door) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE door = ?";
        Connection connection = SingletonConnexionDB.getConnexion();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, door);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getBoolean("access"),
                    resultSet.getString("door"),
                    resultSet.getInt("registredDate"),
                    resultSet.getString("sex")
            );
            users.add(user);
        }

        return users;
    }


}
