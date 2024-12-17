package team.project.faceaccess.controllers;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CaptureController {
    @FXML
    private Button CloseButton;
    @FXML
    private ComboBox<?> cameraComboBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button captureButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button disconnectButton;

    @FXML
    private Button saveButton;

    @FXML
    void captureButtonOnClick(ActionEvent event) {
        System.out.println("capture");
    }

    @FXML
    void connectButtonOnClick(ActionEvent event) {
        System.out.println("Connect");
    }

    @FXML
    void disconnectButtonOnClick(ActionEvent event) {
        System.out.println("Disconnect");
    }

    @FXML
    void saveButtonOnClick(ActionEvent event) {
        System.out.println("save");
    }

    @FXML
    private void closeCapture(ActionEvent event) {
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
}
