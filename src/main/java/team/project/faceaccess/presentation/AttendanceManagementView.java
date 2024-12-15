package team.project.faceaccess.presentation;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.AccessLog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import team.project.faceaccess.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AttendanceManagementView implements Initializable {

    @FXML
    private TableView<AccessLog> logTable;

    @FXML
    private TableColumn<AccessLog, Integer> logIdColumn;

    @FXML
    private TableColumn<AccessLog, String> firstNameColumn;

    @FXML
    private TableColumn<AccessLog, String> lastNameColumn;

    @FXML
    private TableColumn<AccessLog, String> dateColumn;

    @FXML
    private TableColumn<AccessLog, Boolean> accessGrantedColumn;

    @FXML
    private ObservableList<AccessLog> logs;

    @FXML
    private final IMetier metier = new IMetierImp();

    @FXML
    private TreeView<String> roomTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns
        logIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId()));
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getLastName()));
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        accessGrantedColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isAccessGranted()).asObject());

        // Load logs
        loadLogs();
        loadRoomTree();
    }

    private void loadLogs() {
        logs = FXCollections.observableArrayList(metier.getLogs());
        logTable.setItems(logs);
    }

    private void loadRoomTree() {
        // Root node for the TreeView
        TreeItem<String> root = new TreeItem<>("Doors");
        root.setExpanded(false);

        // Fetch all doors from the metier layer
        List<String> doors = null;
        try {
            doors = metier.getAllDoors();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (doors != null) {
            for (String door : doors) {
                // Create a TreeItem for each door
                TreeItem<String> doorItem = new TreeItem<>("Door " + door);

                try {
                    // Fetch all users associated with the door
                    List<User> users = metier.getUsersByDoor(door);

                    if (users != null && !users.isEmpty()) {
                        for (User user : users) {
                            // Add each user as a child of the door
                            TreeItem<String> userItem = new TreeItem<>("-- " + user.getFirstName() + " " + user.getLastName());
                            doorItem.getChildren().add(userItem);
                        }
                    } else {
                        System.out.println("No users found for Door: " + door);
                    }

                } catch (SQLException e) {
                    System.err.println("Failed to fetch users for door: " + door);
                    e.printStackTrace();
                }

                // Add the door TreeItem to the root
                root.getChildren().add(doorItem);
            }
        } else {
            System.err.println("No doors found.");
        }

        // Set the root of the TreeView
        roomTree.setRoot(root);
    }


}
