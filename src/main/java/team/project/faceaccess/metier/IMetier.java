package team.project.faceaccess.metier;

import lombok.extern.java.Log;
import team.project.faceaccess.models.AccessLog;
import team.project.faceaccess.models.Admin;
import team.project.faceaccess.models.User;

import java.sql.SQLException;
import java.util.List;


public interface IMetier {
    List<User> getAllUsers();
    List<User> getUsersByKeyword(String keyword);
    Void changePassword(String oldPassword, String newPassword);
    void deleteUser(int id);
    void addUser(User user);
    void addAdmin(Admin admin);
    Admin getAdmin();
    void updateUser(User user);
    void generateRaport(User user);
    void manageAccess(User user);
    void viewStatistics(User user);
    void viewGeneralStatistics();
    void addLog(AccessLog log);
    void deleteLog(AccessLog log);
    List<AccessLog> getLogs();
    User fetchUserById(int userId);
    List<String> getAllDoors() throws SQLException; // Fetches a list of all unique doors (rooms)
    List<User> getUsersByDoor(String door) throws SQLException; // Fetches users associated with a specific door

}
