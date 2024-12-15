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
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.Admin;

import java.io.IOException;
import java.sql.Connection;


public class LoginController {
    IMetier metier =new IMetierImp();
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
        String userName= adminUserNameId.getText().trim();
        String password= adminPasswordId.getText().trim();

        Admin admin=metier.getAdmin();

        if(userName.equals(admin.getUsername()) && password.equals(admin.getPassword())){
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
