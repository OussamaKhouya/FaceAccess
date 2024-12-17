package team.project.faceaccess.metier;

import team.project.faceaccess.models.AccessLog;
import team.project.faceaccess.models.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Console {
    public static void main(String[] args) throws SQLException {
        IMetierImp metier = new IMetierImp();

        // Add a new log
        User testUser = metier.fetchUserById(18);
        User testUser2 = metier.fetchUserById(19);

        AccessLog newLog = new AccessLog();
        newLog.setUser(testUser);
        newLog.setTimestamp(LocalDateTime.now());
        newLog.setAccessGranted(false);

        System.out.println("Adding a new log...");
        metier.addLog(newLog);

        AccessLog newLog2 = new AccessLog();
        newLog2.setUser(testUser2);
        newLog2.setTimestamp(LocalDateTime.now());
        newLog2.setAccessGranted(true);

        System.out.println("Adding a new log...");
        metier.addLog(newLog2);



        // Get all logs
        System.out.println("Fetching all logs...");
        List<AccessLog> logs = metier.getLogs();
        for (AccessLog log : logs) {
            System.out.println(log);
        }

        // Delete a log
        if (!logs.isEmpty()) {
            AccessLog logToDelete = logs.get(1); // Taking the first log as an example
            System.out.println("Deleting log with ID: " + logToDelete.getId());
            metier.deleteLog(logToDelete);
        }

        // Fetch logs after deletion
        System.out.println("Fetching logs after deletion...");
        logs = metier.getLogs();
        for (AccessLog log : logs) {
            System.out.println(log);
        }

        // List of test doors
        String[] testDoors = {"D404", "C303", "E505", "F606", "G707", "H808", "I909", "J010"};
        List<User>users;
        for (String door : testDoors) {
            users = metier.getUsersByDoor(door);

            System.out.println("Testing Door: " + door);
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    System.out.println("User: " + user.getFirstName() + " " + user.getLastName());
                }
            } else {
                System.out.println("No users found for Door: " + door);
            }
        }

        List<String> doors= metier.getAllDoors();
        for (String door : doors) {
            System.out.println("Door: " + door);
        }

    }

}
