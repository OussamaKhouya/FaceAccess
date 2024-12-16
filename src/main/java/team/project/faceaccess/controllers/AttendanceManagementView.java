package team.project.faceaccess.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.AccessLog;
import team.project.faceaccess.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AttendanceManagementView implements Initializable {


    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

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
        // Load Door Tree
        loadRoomTree();

        // Add listeners to the DatePickers
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterLogsByDateRange());
        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterLogsByDateRange());
    }

    private void loadLogs() {
        logs = FXCollections.observableArrayList(metier.getLogs());
        logTable.setItems(logs);
    }

    private void loadRoomTree() {
        // Root node for the TreeView with a checkbox
        CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>("Doors");
        root.setExpanded(true);
        root.setSelected(true);



        // Fetch all doors from the metier layer
        List<String> doors = null;
        try {
            doors = metier.getAllDoors();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (doors != null) {
            for (String door : doors) {
                // Create a CheckBoxTreeItem for each door
                CheckBoxTreeItem<String> doorItem = new CheckBoxTreeItem<>(door);
                doorItem.setExpanded(true);
                doorItem.setSelected(true);
                // Add listener to doorItem
                doorItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    filterLogsBasedOnSelection();
                });


                try {
                    // Fetch all users associated with the door
                    List<User> users = metier.getUsersByDoor(door);

                    if (users != null && !users.isEmpty()) {
                        for (User user : users) {
                            // Add each user as a child of the door with a checkbox
                            CheckBoxTreeItem<String> userItem = new CheckBoxTreeItem<>(
                                    user.getFirstName() + " " + user.getLastName()
                            );
                            userItem.setSelected(true);

                            // Add listener to userItem
                            userItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                filterLogsBasedOnSelection();
                            });

                            doorItem.getChildren().add(userItem);
                        }
                    } else {
                        System.out.println("No users found for Door: " + door);
                    }

                } catch (SQLException e) {
                    System.err.println("Failed to fetch users for door: " + door);
                    e.printStackTrace();
                }

                // Add the door CheckBoxTreeItem to the root
                root.getChildren().add(doorItem);
            }
        } else {
            System.err.println("No doors found.");
        }


        roomTree.setRoot(root);
        // Set the cell factory to display checkboxes
        roomTree.setCellFactory(CheckBoxTreeCell.forTreeView());
        // Set the root of the TreeView
        //roomTree.setRoot(root);
    }

    // Find the List of selected Items
    private List<String> getSelectedUsers(CheckBoxTreeItem<String> root) {
        List<String> selectedUsers = new ArrayList<>();

        // Traverse the tree and collect selected users
        collectSelectedUsers(root, selectedUsers);

        return selectedUsers;
    }

    private void collectSelectedUsers(CheckBoxTreeItem<String> item, List<String> selectedUsers) {
        if (item.isSelected()) {
            if (item.getChildren().isEmpty()) {
                // If it's a leaf node (user), add it to the list
                selectedUsers.add(item.getValue());
            } else {
                // If it's a door, add all child users
                for (TreeItem<String> child : item.getChildren()) {
                    collectSelectedUsers((CheckBoxTreeItem<String>) child, selectedUsers);
                }
            }
        } else {
            // Even if a door isn't selected, traverse its children
            for (TreeItem<String> child : item.getChildren()) {
                collectSelectedUsers((CheckBoxTreeItem<String>) child, selectedUsers);
            }
        }
    }


    // Method to Filter and Update Logs
    private void filterLogsBasedOnSelection() {
        // Get the root node of the TreeView
        CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>) roomTree.getRoot();

        // Collect all selected users
        List<String> selectedUsers = getSelectedUsers(root);

        // Filter logs based on selected users
        ObservableList<AccessLog> filteredLogs = FXCollections.observableArrayList();

        for (AccessLog log : logs) {
            String userFullName = log.getUser().getFirstName() + " " + log.getUser().getLastName();

            if (selectedUsers.contains(userFullName)) {
                filteredLogs.add(log);
            }
        }

        // Update the TableView with the filtered logs
        logTable.setItems(filteredLogs);
    }


    private void filterLogsByDateRange() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate == null && toDate == null) {
            // If no dates are selected, show all logs
            logTable.setItems(logs);
            return;
        }

        ObservableList<AccessLog> filteredLogs = logs.filtered(log -> {
            LocalDate logDate = log.getTimestamp().toLocalDate();
            if (fromDate != null && toDate != null) {
                return (logDate.isEqual(fromDate) || logDate.isAfter(fromDate))
                        && (logDate.isEqual(toDate) || logDate.isBefore(toDate));
            } else if (fromDate != null) {
                return logDate.isEqual(fromDate) || logDate.isAfter(fromDate);
            } else { // toDate != null
                return logDate.isEqual(toDate) || logDate.isBefore(toDate);
            }
        });

        logTable.setItems(filteredLogs);
    }


}
