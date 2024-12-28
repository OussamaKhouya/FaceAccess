package team.project.faceaccess.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.Admin;

import java.io.IOException;


public class LoginController {
    IMetier metier = new IMetierImp();
    @FXML
    private PasswordField adminPasswordId;

    @FXML
    private TextField adminUserNameId;

    @FXML
    private Button loginButtonOn;
    @FXML
    private Button CloseButton;

    @FXML
    void loginButtonOnClick(ActionEvent event) throws IOException {
        String userName = adminUserNameId.getText().trim();
        String password = adminPasswordId.getText().trim();

        Admin admin = metier.getAdmin();

        if (userName.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
            new Controller().goToDashboard();
            ((Stage) CloseButton.getScene().getWindow()).close();
        } else {
            System.out.println("Login Failed");
        }

    }

    @FXML
    private void closeLogin() {
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
}
