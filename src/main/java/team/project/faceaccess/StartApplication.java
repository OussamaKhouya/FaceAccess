package team.project.faceaccess;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import team.project.faceaccess.metier.IMetierImp;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage chargingStage) throws IOException {
        //Show charging view
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("views/charging-view.fxml"));
        chargingStage.setScene(new Scene(fxmlLoader.load(), 517 , 341));
        chargingStage.initStyle(StageStyle.UNDECORATED);
        chargingStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(e -> {

            Stage registerOrHomeView = new Stage();
            boolean isThereAnAdmin = new IMetierImp().isThereAnAdmin();
            FXMLLoader loginLoader;
            if (isThereAnAdmin) {
                loginLoader = new FXMLLoader(StartApplication.class.getResource("views/home-view.fxml"));
                registerOrHomeView.setTitle("Home");
                registerOrHomeView.initStyle(StageStyle.UNDECORATED);
            } else {
                loginLoader = new FXMLLoader(StartApplication.class.getResource("views/register-view.fxml"));
                registerOrHomeView.setTitle("Register");
                registerOrHomeView.initStyle(StageStyle.UNDECORATED);
            }

            try {
                registerOrHomeView.setScene(new Scene(loginLoader.load()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            chargingStage.close();
            registerOrHomeView.show();
        });
        delay.play();

    }

    public static void main(String[] args) {
        launch();
    }
}