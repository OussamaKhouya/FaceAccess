package team.project.faceaccess.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import team.project.faceaccess.StartApplication;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;
import team.project.faceaccess.models.User;
import team.project.faceaccess.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private Label recognitionResultlbl;
    @FXML
    private Tab homeTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab recognitionTab;
    List<User> users;

    void reloadClassifier() {
        File file = new File("photos//classifierLBPH.yml");
        if (file.exists()) {
            if (recognizer != null)
                recognizer = null;
            recognizer = LBPHFaceRecognizer.create();
            recognizer.read("photos//classifierLBPH.yml");
            recognizer.setThreshold(80);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");
        String cascadePath = "photos//haarcascade_frontalface_alt.xml";
        File cascadeFile = new File(cascadePath);
        if (!cascadeFile.exists()) {
            throw new RuntimeException("Error: Classifier XML file not found at " + cascadePath);
        }
        users = metier.getAllUsers();
        cascade = new CascadeClassifier(cascadePath);
        reloadClassifier();
        tabPane.setTabMaxHeight(0);
    }

    @FXML
    void onCameraBtnClicked(ActionEvent event) {
        startCamera();
        tabPane.getSelectionModel().select(recognitionTab);
        System.out.println("Camera selected");
    }

    @FXML
    void onLockBtnClicked(ActionEvent event) throws IOException {
        Stage loginOrRegisterStage = new Stage();
        FXMLLoader loginLoader;
        loginLoader = new FXMLLoader(StartApplication.class.getResource("views/login-view.fxml"));
        loginOrRegisterStage.setTitle("Login");
        Scene scene1 = new Scene(loginLoader.load());
        loginOrRegisterStage.initStyle(StageStyle.UNDECORATED);
        loginOrRegisterStage.setScene(scene1);
        loginOrRegisterStage.show();
    }

    private VideoCapture webSource;
    private final Mat cameraImage = new Mat();
    private CascadeClassifier cascade;
    private LBPHFaceRecognizer recognizer;

    private final boolean runnable = true;
    private int idPerson = -1;
    private int counter = 0;


    private void startCamera() {
        reloadClassifier();

        webSource = new VideoCapture(0);
        if (!webSource.isOpened()) {
            System.out.println("Error: Camera not available.");
            return;
        }
        Thread cameraThread = new Thread(() -> {
            while (runnable) {
                if (webSource.read(cameraImage)) {
                    Mat grayImage = new Mat();
                    opencv_imgproc.cvtColor(cameraImage, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);

                    RectVector detectedFaces = new RectVector();
                    cascade.detectMultiScale(grayImage, detectedFaces);
                    System.out.println("faces N " + detectedFaces.size());
                    long facesNumber = detectedFaces.size();
                    for (int i = 0; i < facesNumber && facesNumber == 1; i++) {
                        Rect faceData = detectedFaces.get(i);


                        Mat croppedFace = new Mat(grayImage, faceData);
                        opencv_imgproc.resize(croppedFace, croppedFace, new org.bytedeco.opencv.opencv_core.Size(160, 160));

                        int[] label = new int[1];
                        double[] confidence = new double[1];
                        recognizer.predict(croppedFace, label, confidence);

                        if (label[0] == -1 || confidence[0] > 90) {
                            idPerson = -1;
                            updateLabels("Unknown", "", "");
                            opencv_imgproc.rectangle(cameraImage, faceData, new org.bytedeco.opencv.opencv_core.Scalar(0, 0, 255, 3), 3, 0, 0);

                        } else {
                            counter++;
                            idPerson = label[0];
                            System.out.println(idPerson);
                            User user = fetchPersonData(idPerson);
                            opencv_imgproc.rectangle(cameraImage, faceData, new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 3, 0, 0);
                            // Calculate position to draw the text (above the rectangle)
                            int textX = faceData.x();
                            int textY = Math.max(faceData.y() - 10, 0); // Ensure text doesn't go out of bounds
                            if (user != null) {
                                // Draw the name above the rectangle
                                opencv_imgproc.putText(cameraImage, user.getFirstName() + " " + user.getLastName(),
                                        new org.bytedeco.opencv.opencv_core.Point(textX, textY),
                                        opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.8,
                                        new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 2, 0, false);
                                if (user.isAdmin()) {
                                    Platform.runLater(() -> new Controller().goToDashboard());
                                    webSource.release();
                                    imageView.setImage(null);
                                }
                            }

                        }
                    }
                    Image fxImage = Utils.matToImage(cameraImage);
                    Platform.runLater(() -> imageView.setImage(fxImage));
                }
            }
        });

        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    IMetier metier = new IMetierImp();

    private void updateLabels(String id, String firstName, String lastName) {
        Platform.runLater(() -> {
            recognitionResultlbl.setText(id + ":  " + firstName + " " + lastName);
        });
    }

    private User fetchPersonData(int personId) {
        // Simulate database fetch for person details based on ID
        User user = users.stream()
                .filter(u -> u.getId() == personId)
                .findFirst()
                .orElse(null);
        if (user != null)
            updateLabels(String.valueOf(user.getId()), user.getFirstName(), user.getLastName());
        return user;
    }


    @FXML
    Button CloseButton;

    @FXML
    private void closeLogin() {
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

}