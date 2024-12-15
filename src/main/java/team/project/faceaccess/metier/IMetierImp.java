package team.project.faceaccess.metier;

import team.project.faceaccess.models.AccessLog;
import team.project.faceaccess.models.Admin;
import team.project.faceaccess.models.User;
import team.project.faceaccess.singleton.SingletonConnexionDB;

import java.io.File;
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

        }catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public void updateUser(User user) {
        Connection connection = SingletonConnexionDB.getConnexion();

        try {
            String query = "UPDATE Users SET firstName = ?, lastName = ?, access = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setBoolean(2, user.getAccess());

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
