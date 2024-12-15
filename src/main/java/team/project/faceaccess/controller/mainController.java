package team.project.faceaccess.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class mainController {

    @FXML
    private Button CloseButton;

    @FXML
    private AnchorPane attendancePane;

    @FXML
    private AnchorPane personnelPane;

    @FXML
    private AnchorPane reportPane;

    @FXML
    void attendanceHandler(ActionEvent event) {
        attendancePane.setVisible(true);
        personnelPane.setVisible(false);
        reportPane.setVisible(false);
    }

    @FXML
    void personnelHandler(ActionEvent event) {
        attendancePane.setVisible(false);
        personnelPane.setVisible(true);
        reportPane.setVisible(false);
    }

    @FXML
    void reportHandler(ActionEvent event) {
        attendancePane.setVisible(false);
        personnelPane.setVisible(false);
        reportPane.setVisible(true);
    }
    @FXML
    private void closeLogin() {
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
}
