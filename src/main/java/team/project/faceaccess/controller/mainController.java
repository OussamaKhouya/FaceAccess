package team.project.faceaccess.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class mainController {

    @FXML
    private Button CloseButton;

    @FXML
    private Button attendanceButton;

    @FXML
    private AnchorPane attendancePane;

    @FXML
    private Button personnelButton;

    @FXML
    private AnchorPane personnelPane;

    @FXML
    private Button reportButton;

    @FXML
    private AnchorPane reportPane;

    public void initialize() {

        attendanceButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
        reportButton.setBackground(new Background(new BackgroundFill(Color.web("#f7f4f4"), CornerRadii.EMPTY, null)));
        personnelButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
    }

    @FXML
    void attendanceHandler(ActionEvent event) {
        attendancePane.setVisible(true);
        personnelPane.setVisible(false);
        reportPane.setVisible(false);
        reportButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
        personnelButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
    }

    @FXML
    void personnelHandler(ActionEvent event) {
        attendancePane.setVisible(false);
        personnelPane.setVisible(true);
        reportPane.setVisible(false);
        attendanceButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
        reportButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
    }

    @FXML
    void reportHandler(ActionEvent event) {
        attendancePane.setVisible(false);
        personnelPane.setVisible(false);
        reportPane.setVisible(true);
        attendanceButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
        personnelButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
    }
    @FXML
    private void closeLogin() {
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void reportMouseOn(MouseEvent mouseEvent) {
        reportButton.setBackground(new Background(new BackgroundFill(Color.web("#eee2e2"), CornerRadii.EMPTY, null)));

    }
    @FXML
    public void reportMouseOff(MouseEvent mouseEvent) {
        if(reportPane.isVisible()){
            reportButton.setBackground(new Background(new BackgroundFill(Color.web("#f7f4f4"), CornerRadii.EMPTY, null)));
        }else {
            reportButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
        }
    }
    @FXML
    public void personnelMouseOn(MouseEvent mouseEvent) {
        personnelButton.setBackground(new Background(new BackgroundFill(Color.web("#eee2e2"), CornerRadii.EMPTY, null)));
    }
    @FXML
    public void personnelMouseOff(MouseEvent mouseEvent) {
        if(personnelPane.isVisible()){
            personnelButton.setBackground(new Background(new BackgroundFill(Color.web("#f7f4f4"), CornerRadii.EMPTY, null)));
        }else {
            personnelButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
        }
    }
    @FXML
    public void attendanceMouseOn(MouseEvent mouseEvent) {
        attendanceButton.setBackground(new Background(new BackgroundFill(Color.web("#eee2e2"), CornerRadii.EMPTY, null)));
    }
    @FXML
    public void attendanceMouseOff(MouseEvent mouseEvent) {
        if(attendancePane.isVisible()){
            attendanceButton.setBackground(new Background(new BackgroundFill(Color.web("#f7f4f4"), CornerRadii.EMPTY, null)));
        }else {
            attendanceButton.setBackground(new Background(new BackgroundFill(Color.web("#40cd2f"), CornerRadii.EMPTY, null)));
        }
    }
}
