package team.project.faceaccess.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team.project.faceaccess.StartApplication;

import java.io.IOException;

public class AttendanceApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the attendance FXML views
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("views/attendance-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // Set the stage properties
        stage.setTitle("Attendance Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
