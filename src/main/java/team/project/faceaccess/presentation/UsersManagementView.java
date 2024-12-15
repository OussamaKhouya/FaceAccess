package team.project.faceaccess.presentation;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.User;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UsersManagementView implements Initializable {

    @FXML
    private TextField employeeIdField;
    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField departmentField;



    @FXML
    private TableView<User> usersTable;

    @FXML
    private Button exportButton;

    @FXML
    private TextField firstNameField;

    @FXML
    private DatePicker registredDate;



    @FXML
    private Button importButton;

    @FXML
    private TextField lastNameField;


    @FXML
    private ImageView photoView;

    @FXML
    private ComboBox<?> positionComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private TextField searchField;

    @FXML
    private CheckBox statusCheckBox;

    @FXML
    private Button uploadPhotoBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private ComboBox<String> sexComboBox;


    User user;

    @FXML
    void onLoadBtnClicked(ActionEvent event) {
        if (!usersTable.getSelectionModel().getSelectedItems().isEmpty()) {
            user = usersTable.getSelectionModel().getSelectedItem();
            employeeIdField.setText(String.valueOf(user.getId()));
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            statusCheckBox.setSelected(user.getAccess());
            departmentField.setText(user.getDoor());
            registredDate.setValue(LocalDate.ofEpochDay(user.getRegistredDate()));
            sexComboBox.getSelectionModel().select(user.getSex());
            System.out.println(user);
        }
    }

    @FXML
    void onAddBtnClicked(ActionEvent event) {
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        user.setAccess(statusCheckBox.isSelected());
        user.setDoor(departmentField.getText());
        user.setId(Integer.parseInt(employeeIdField.getText()));
        user.setRegistredDate((int) registredDate.getValue().toEpochDay());
        user.setSex(sexComboBox.getSelectionModel().getSelectedItem());
        metier.addUser(user);
        reloadTable();
    }

    private void reloadTable() {
        System.out.println("Table Reloading ...");
        data = FXCollections.observableArrayList(
                metier.getAllUsers()
        );
        usersTable.setItems(data);
    }

    @FXML
    void onDeleteBtnClicked(ActionEvent event) {

    }

    @FXML
    void onSaveBtnClicked(ActionEvent event) {
        System.out.println(registredDate.getValue());
        System.out.println(registredDate.getValue().toEpochDay());
    }


    ObservableList<User> data;
    @FXML
    private Button uploadPhotoBtn1;
    IMetier metier = new IMetierImp();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName())
        );
        reloadTable();

        sexComboBox.getItems().removeAll(sexComboBox.getItems());
        sexComboBox.getItems().addAll("", "Male", "Female");
        sexComboBox.getSelectionModel().select("");

        System.out.println(LocalDate.now().toEpochDay());
        System.out.println(LocalDate.ofEpochDay(LocalDate.now().toEpochDay()));

    }


    void generateFakeData() {
        // Create a list of User objects
        List<User> users = new ArrayList<>();

        // Add User objects to the list
        users.add(new User(1, "John", "Doe", true, "A101", 20230101, "Male"));
        users.add(new User(2, "Jane", "Smith", false, "B202", 20230215, "Female"));
        users.add(new User(3, "Michael", "Johnson", true, "C303", 20220120, "Male"));
        users.add(new User(4, "Emily", "Davis", true, "D404", 20231010, "Female"));
        users.add(new User(5, "Chris", "Brown", false, "E505", 20221105, "Male"));
        users.add(new User(6, "Sophia", "Taylor", true, "F606", 20211212, "Female"));
        users.add(new User(7, "Daniel", "Wilson", true, "G707", 20230530, "Male"));
        users.add(new User(8, "Olivia", "Moore", false, "H808", 20230808, "Female"));
        users.add(new User(9, "Ethan", "Thomas", true, "I909", 20210621, "Male"));
        users.add(new User(10, "Isabella", "Harris", false, "J010", 20231115, "Female"));

        // Print the list of users
        for (User user : users) {
            metier.addUser(user);
        }
    }
}
