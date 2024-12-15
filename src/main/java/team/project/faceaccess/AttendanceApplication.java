package team.project.faceaccess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AttendanceApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the attendance FXML view
        FXMLLoader fxmlLoader = new FXMLLoader(AttendanceApplication.class.getResource("views/attendance.fxml"));
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
