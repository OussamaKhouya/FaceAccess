package team.project.faceaccess.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import team.project.faceaccess.HelloApplication;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.Admin;

import java.io.IOException;

public class RegisterController  {
    IMetier metier =new IMetierImp();
    @FXML
    private TextField adminEmail;

    @FXML
    private PasswordField adminPassword;

    @FXML
    private PasswordField adminPasswordConfirme;

    @FXML
    private TextField adminUserName;

    @FXML
    public Button CloseButton;
    @FXML
    public Button RegisterButton;

    @FXML
    private void closeRegister() {
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void RegisterButtonOnClick(ActionEvent actionEvent) throws IOException {
        Admin admin = new Admin();
        String userName = adminUserName.getText();
        String email = adminEmail.getText();
        String password = adminPassword.getText();
        if(userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.show();
        }else{
            admin.setUsername(userName);
            admin.setEmail(email);
            if (password.equals(adminPasswordConfirme.getText())) {
                admin.setPassword(password);
                metier.addAdmin(admin);
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                closeRegister();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.show();
            }

        }

    }
}
