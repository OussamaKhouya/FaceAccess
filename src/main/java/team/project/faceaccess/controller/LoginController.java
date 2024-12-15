package team.project.faceaccess.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController {
    @FXML
    private PasswordField adminPasswordId;

    @FXML
    private TextField adminUserNameId;

    @FXML
    private Button loginButtonOn;
    @FXML
    private Button CloseButton;

    @FXML
    void loginButtonOnClick(ActionEvent event) {
        String userName= adminUserNameId.getText();
        String password= adminPasswordId.getText();
        if(userName.equals("admin") && password.equals("admin")){
            System.out.println("Login Successful");
        }
        else{
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
