package team.project.faceaccess.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import team.project.faceaccess.HelloApplication;

import java.io.IOException;


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
    void loginButtonOnClick(ActionEvent event) throws IOException {
        String userName= adminUserNameId.getText();
        String password= adminPasswordId.getText();
        if(userName.equals("admin") && password.equals("admin")){
            System.out.println("Login Successful");
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

            ((Stage) CloseButton.getScene().getWindow()).close();

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
