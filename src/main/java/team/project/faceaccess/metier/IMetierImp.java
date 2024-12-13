package team.project.faceaccess.metier;

import team.project.faceaccess.models.AccessLog;
import team.project.faceaccess.models.User;
import team.project.faceaccess.singleton.SingletonConnexionDB;

import java.sql.PreparedStatement;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class IMetierImp implements IMetier{
    // get all the users (action by the Admin)
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = SingletonConnexionDB.getConnexion();

        try {
            String query = "SELECT id, name, accessStatus, photosPath FROM Users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                boolean accessStatus = resultSet.getBoolean("accessStatus");
                String photosPath = resultSet.getString("photosPath");

                User user = new User(id, name, List.of(photosPath != null ? new java.io.File(photosPath) : null), accessStatus);
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
            String query = "INSERT INTO Users (name, accessStatus, photosPath) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setBoolean(2, user.isAccessStatus());

            // Handle photosPath
            String photosPath = user.getPhotos() != null && !user.getPhotos().isEmpty() ? user.getPhotos().get(0).getPath() : null;
            preparedStatement.setString(3, photosPath);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateUser(User user) {
        Connection connection = SingletonConnexionDB.getConnexion();

        try {
            String query = "UPDATE Users SET name = ?, accessStatus = ?, photosPath = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setBoolean(2, user.isAccessStatus());

            // Handle photosPath
            String photosPath = user.getPhotos() != null && !user.getPhotos().isEmpty() ? user.getPhotos().get(0).getPath() : null;
            preparedStatement.setString(3, photosPath);

            preparedStatement.setInt(4, user.getId());

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

    @Override
    public void addLog(AccessLog log) {

    }

    @Override
    public void deleteLog(AccessLog log) {

    }

    @Override
    public List<AccessLog> getLogs() {
        return List.of();
    }
}
