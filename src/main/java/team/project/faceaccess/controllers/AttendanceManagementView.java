package team.project.faceaccess.controllers;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.stage.FileChooser;
import javafx.scene.control.Alert;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        /*
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterLogsByDateRange());
        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterLogsByDateRange());
         */
        // Add listeners to the DatePickers
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterLogs());
        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterLogs());

        // Add a listener to the root of the checkbox tree
        CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>) roomTree.getRoot();
        root.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), event -> filterLogs());
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
                    filterLogs();
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
                                filterLogs();
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
    /*
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

     */

    /*
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


     */

    private void filterLogs() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        // Get selected users from the tree
        CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>) roomTree.getRoot();
        List<String> selectedUsers = getSelectedUsers(root);

        ObservableList<AccessLog> filteredLogs = logs.filtered(log -> {
            // Filter by selected users
            String userFullName = log.getUser().getFirstName() + " " + log.getUser().getLastName();
            boolean matchesUserFilter = selectedUsers.isEmpty() || selectedUsers.contains(userFullName);

            // Filter by date range
            boolean matchesDateFilter = true;
            if (fromDate != null || toDate != null) {
                LocalDate logDate = log.getTimestamp().toLocalDate();
                if (fromDate != null && logDate.isBefore(fromDate)) {
                    matchesDateFilter = false;
                }
                if (toDate != null && logDate.isAfter(toDate)) {
                    matchesDateFilter = false;
                }
            }

            return matchesUserFilter && matchesDateFilter;
        });

        // Update the TableView
        logTable.setItems(filteredLogs);
    }

    @FXML
    private void exportReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Let the user choose a file to save
        java.io.File file = fileChooser.showSaveDialog(logTable.getScene().getWindow());
        if (file != null) {
            Document document = new Document();
            try {
                // Initialize PdfWriter with the chosen file path
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Add a title to the document
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("Access Logs Report", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n")); // Add spacing

                // Create a table for the logs
                PdfPTable table = new PdfPTable(5); // Five columns: ID, First Name, Last Name, Date, Access Granted
                table.setWidthPercentage(100); // Full width of the page
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Add table headers
                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                table.addCell(new PdfPCell(new Phrase("ID", headerFont)));
                table.addCell(new PdfPCell(new Phrase("First Name", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Last Name", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Date", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Access Granted", headerFont)));

                // Add logs to the table
                for (AccessLog log : logTable.getItems()) {
                    table.addCell(String.valueOf(log.getId()));
                    table.addCell(log.getUser().getFirstName());
                    table.addCell(log.getUser().getLastName());
                    table.addCell(log.getTimestamp().toString());
                    table.addCell(log.isAccessGranted() ? "Yes" : "No");
                }

                document.add(table);

                // Add a footer or additional information if needed
                document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));

                // Show success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Report Exported");
                successAlert.setHeaderText(null);
                successAlert.setContentText("The report has been successfully exported.");
                successAlert.showAndWait();

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                // Show error alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Export Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while exporting the report.");
                errorAlert.showAndWait();
            } finally {
                document.close();
            }
        }
    }










}
