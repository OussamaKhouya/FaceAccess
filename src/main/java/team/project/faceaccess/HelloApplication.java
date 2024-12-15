package team.project.faceaccess;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/welcom-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 399, 315);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Stage loginStage =new Stage();
        FXMLLoader loginLoader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));
        Scene scene1 = new Scene(loginLoader.load());
        loginStage.setTitle("Login");
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setScene(scene1);

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            stage.close();
            loginStage.show();
        });
        delay.play();

    }

    public static void main(String[] args) {
        launch();
    }
}