package team.project.faceaccess.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import team.project.faceaccess.StartApplication;

import java.io.IOException;

public class Controller {


    void goToDashboard(){
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("views/dashboard-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),1024,729);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
