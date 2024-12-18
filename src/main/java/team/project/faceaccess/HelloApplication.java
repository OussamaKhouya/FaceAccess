package team.project.faceaccess;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.singleton.SingletonConnexionDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        IMetier metier = new IMetierImp();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/welcom-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 399, 315);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        Stage loginOrRegisterStage = new Stage();
        Connection connection = SingletonConnexionDB.getConnexion();
        int nAdmin = 0;
        try {
            String query = "select count(*) from Admin";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                nAdmin = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FXMLLoader loginLoader;
        if (nAdmin == 0) {
            loginLoader = new FXMLLoader(HelloApplication.class.getResource("views/register-view.fxml"));
            loginOrRegisterStage.setTitle("Register");
        } else {
            loginLoader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));
            loginOrRegisterStage.setTitle("Login");
        }

        Scene scene1 = new Scene(loginLoader.load());
        loginOrRegisterStage.initStyle(StageStyle.UNDECORATED);
        loginOrRegisterStage.setScene(scene1);

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            stage.close();
            loginOrRegisterStage.show();
        });
        delay.play();

    }

    public static void main(String[] args) {
        launch();
    }
}