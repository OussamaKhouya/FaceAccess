package team.project.faceaccess.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import team.project.faceaccess.HelloApplication;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.User;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class UsersManagementView implements Initializable {

    @FXML
    private TextField UserIdField;
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
    public Button updateBtn;
    @FXML
    private ComboBox<String> sexComboBox;


    User user;

    void clearUserForm() {
        UserIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        statusCheckBox.setSelected(false);
        departmentField.setText("");
        registredDate.setValue(LocalDate.now());
        sexComboBox.getSelectionModel().select("");
        photoView.setImage(null);
    }

    @FXML
    void onLoadBtnClicked(ActionEvent event) {
        if (!usersTable.getSelectionModel().getSelectedItems().isEmpty()) {
            user = usersTable.getSelectionModel().getSelectedItem();
            UserIdField.setText(String.valueOf(user.getId()));
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            statusCheckBox.setSelected(user.getAccess());
            departmentField.setText(user.getDoor());
            registredDate.setValue(LocalDate.ofEpochDay(user.getRegistredDate()));
            sexComboBox.getSelectionModel().select(user.getSex());
            System.out.println(user);
            File file = new File(String.format("photos/active/%s/1.png", user.getId()));
            Image image = new Image(file.toURI().toString());
            photoView.setImage(image);
        }
    }

    @FXML
    void onAddBtnClicked(ActionEvent event) {
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        user.setAccess(statusCheckBox.isSelected());
        user.setDoor(departmentField.getText());
        user.setId(Integer.parseInt(UserIdField.getText()));
        user.setRegistredDate((int) registredDate.getValue().toEpochDay());
        user.setSex(sexComboBox.getSelectionModel().getSelectedItem());
        metier.addUser(user);
        generatePhotoFromInternet(user.getId());
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
    void onDeleteBtnClicked(ActionEvent event) throws IOException {
        if (!usersTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention!");
            alert.setHeaderText("Suppression du User");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet Utilisateur ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    User p = usersTable.getSelectionModel().getSelectedItem();
                    metier.deleteUser(p.getId());
                    deleteUserPohtos(p.getId());
                    clearUserForm();
                    reloadTable();
                }
            });
        }


    }

    @FXML
    void onSaveBtnClicked(ActionEvent event) {
        if (!usersTable.getSelectionModel().getSelectedItems().isEmpty()) {
            user.setId(Integer.parseInt(UserIdField.getText()));
            user.setFirstName(firstNameField.getText());
            user.setLastName(lastNameField.getText());
            user.setAccess(statusCheckBox.isSelected());
            user.setDoor(departmentField.getText());
            user.setRegistredDate((int) registredDate.getValue().toEpochDay());
            user.setSex(sexComboBox.getSelectionModel().getSelectedItem());
            metier.updateUser(user);
            reloadTable();
        }
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

        List<User> u = metier.getAllUsers();
        for (User user : u) {
            generatePhotoFromInternet(user.getId());
        }
    }

    private void deleteUserPohtos(Integer userId) {
        Path sourceDir = Paths.get("photos/active/%s/1.png".formatted(userId.toString()));  // Source folder path
        String targetDir = "photos/deleted/%s/1.png".formatted(userId.toString());  // Target folder path

        try {
            // Create target directory if it does not exist
            Path saveDir = Paths.get(targetDir).getParent();
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }

            // Move the folder and its contents
            Files.move(sourceDir, saveDir.resolve(sourceDir.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            Path dir = Paths.get("photos/active/%s".formatted(userId.toString()));

            try {
                Files.delete(dir); // Deletes the directory if it's empty
                System.out.println("Directory deleted successfully.");
            } catch (IOException e) {
                System.err.println("Error deleting directory: " + e.getMessage());
            }
            System.out.println("Folder moved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePhotoFromInternet(Integer userId) {
        String imageUrl = "https://randomuser.me/api/portraits/men/%d.jpg".formatted(new Random().nextInt(100));
        String savePath = "photos/active/%s/1.png".formatted(userId.toString());

        try {
            // Create directories if they don't exist
            Path saveDir = Paths.get(savePath).getParent();
            if (saveDir != null && !Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }

            // Open connection to the URL and get input stream
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                // Save the file
                Path targetPath = Paths.get(savePath);
                Files.copy(inputStream, targetPath);
                System.out.println("Image downloaded successfully to " + savePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to download image: " + e.getMessage());
        }
    }

//    private OpenCVFrameGrabber grabber;
//    private volatile boolean isRunning = true;
//    private void startCamera() {
//        CaptureController controller=new CaptureController();
//        grabber = new OpenCVFrameGrabber(0); // Open default webcam
//        new Thread(() -> {
//            try {
//                grabber.start();
//                Java2DFrameConverter converter = new Java2DFrameConverter();
//
//                while (isRunning) {
//                    Frame frame = grabber.grab(); // Capture a frame
//                    if (frame != null) {
//                        BufferedImage bufferedImage = converter.convert(frame);
//                        if (bufferedImage != null) {
//                            Platform.runLater(() -> {
//                                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//                                controller.getImageView().setImage(image);
//                            });
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
    public void uploadPhotoBtnOnClick(ActionEvent actionEvent) throws IOException {

            if (!usersTable.getSelectionModel().getSelectedItems().isEmpty()){
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/capture-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                CaptureController controller =fxmlLoader.getController();
                controller.setUser(user);
                controller.setParentController(this);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            }else {
                Alert alert =new Alert(Alert.AlertType.ERROR,"select an user first",ButtonType.OK);
                alert.show();
            }

    }



    public void deleteUserPohtos(ActionEvent actionEvent) {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"are you sure uou want to delete the photo ?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        // Check which button the user clicked
        if (result.isPresent()) {
            if (result.get() == ButtonType.YES) {
                System.out.println("OK button clicked.");
                deleteUserPohtos(user.getId());
                updateBtn.fire();
            } else if (result.get() == ButtonType.NO) {
                System.out.println("Cancel button clicked.");
            }
        } else {
            System.out.println("No button was clicked (Alert was closed).");
        }
    }
}
