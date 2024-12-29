package team.project.faceaccess.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private Label time;
    @FXML
    private Label date;
    @FXML
    private Label recognitionResultlbl;
    @FXML
    private Tab homeTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab recognitionTab;
    List<User> users;

    @FXML
    private Button CloseButton;

    @FXML
    private TextField UserIdField;


    @FXML
    private TextField departmentField;

    @FXML
    private TextField firstNameField;


    @FXML
    private CheckBox isAdmin;

    @FXML
    private TextField lastNameField;


    @FXML
    private DatePicker registredDate;

    @FXML
    private ComboBox<String> sexComboBox;

    @FXML
    private CheckBox statusCheckBox;


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
        String cascadePath = "photos//haarcascade_frontalface_alt.xml";
        File cascadeFile = new File(cascadePath);
        if (!cascadeFile.exists()) {
            throw new RuntimeException("Error: Classifier XML file not found at " + cascadePath);
        }
        users = metier.getAllUsers();
        cascade = new CascadeClassifier(cascadePath);
        reloadClassifier();
        tabPane.setTabMaxHeight(0);
        setupDateTime();

    }

    private void setupDateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Get current date and time
            LocalDateTime now = LocalDateTime.now();

            // Format for time (e.g., 13:45)
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = now.format(timeFormatter);

            // Format for date (e.g., Saturday, 28 December 2024)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
            String formattedDate = now.format(dateFormatter);
            time.setText(formattedTime);
            date.setText(formattedDate);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

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
    private final int counter = 0;
    private final Controller dashboardController = new Controller();
    private User user = null;

    private void startCamera() {
        reloadClassifier();

        webSource = new VideoCapture(0);
        if (!webSource.isOpened()) {
            System.out.println("Error: Camera not available.");
            return;
        }
//        Thread cameraThread = new Thread(() -> {
//            long startTime = System.currentTimeMillis();
//            List<FacePrediction> predictions = new ArrayList<>();
//            while (runnable) {
//                if (webSource.read(cameraImage)) {
//                    Mat grayImage = new Mat();
//                    opencv_imgproc.cvtColor(cameraImage, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);
//
//                    RectVector detectedFaces = new RectVector();
//                    cascade.detectMultiScale(grayImage, detectedFaces);
//                    long facesNumber = detectedFaces.size();
//                    Map<Integer, Integer> faceCount = new HashMap<>();
//                    for (int i = 0; i < facesNumber && facesNumber == 1; i++) {
//                        Rect faceData = detectedFaces.get(i);
//
//                        Mat croppedFace = new Mat(grayImage, faceData);
//                        opencv_imgproc.resize(croppedFace, croppedFace, new org.bytedeco.opencv.opencv_core.Size(160, 160));
//
//                        int[] label = new int[1];
//                        double[] confidence = new double[1];
//                        recognizer.predict(croppedFace, label, confidence);
//                        System.out.println("confidence: " + confidence[0]);
//                        predictions.add(new FacePrediction(label[0], confidence[0], faceData));
//                        if (label[0] == -1 || confidence[0] > 90) {
//
//                            idPerson = -1;
//                            updateLabels("Unknown", "", "");
//                            opencv_imgproc.rectangle(cameraImage, faceData, new org.bytedeco.opencv.opencv_core.Scalar(0, 0, 255, 3), 3, 0, 0);
//
//                        } else {
//                            idPerson = label[0];
//                            opencv_imgproc.rectangle(cameraImage, faceData, new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 3, 0, 0);
//                            if(user != null){
//                                int textX = faceData.x();
//                                int textY = Math.max(faceData.y() - 10, 0);
//                                opencv_imgproc.putText(cameraImage, user.getFirstName() + " " + user.getLastName(),
//                                        new org.bytedeco.opencv.opencv_core.Point(textX, textY),
//                                        opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.8,
//                                        new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 2, 0, false);
//                            }
//                            faceCount.put(idPerson, faceCount.getOrDefault(idPerson, 0) + 1);
//                            // Check elapsed time
//                            long elapsedTime = System.currentTimeMillis() - startTime;
//                            if (elapsedTime >= 300) { // 1 second window
//                                if (!faceCount.isEmpty()) {
//                                    // Get the most recognized face
//                                    int mostRecognizedFace = faceCount.entrySet().stream()
//                                            .max(Comparator.comparingInt(Map.Entry::getValue))
//                                            .map(Map.Entry::getKey)
//                                            .orElse(-1);
//
//                                    System.out.println("Most recognized face: " + mostRecognizedFace);
//
//                                // Reset for the next window
//                                faceCount.clear();
//                                startTime = System.currentTimeMillis();
//                             user = fetchPersonData(mostRecognizedFace);
//                            // Calculate position to draw the text (above the rectangle)
//                            int textX = faceData.x();
//                            int textY = Math.max(faceData.y() - 10, 0); // Ensure text doesn't go out of bounds
//                            if (user != null) {
//                                // Draw the name above the rectangle
//                                opencv_imgproc.putText(cameraImage, user.getFirstName() + " " + user.getLastName(),
//                                        new org.bytedeco.opencv.opencv_core.Point(textX, textY),
//                                        opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.8,
//                                        new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 2, 0, false);
//                                if (user.isAdmin()) {
//                                    //Platform.runLater(dashboardController::goToDashboard );
//                                    //webSource.release();
//                                    //imageView.setImage(null);
//                                    System.out.println("You are an admin");
//                                }
//                            }
//                        }
//                        }
//                        }
//                    }
//
//
//                    Image fxImage = Utils.matToImage(cameraImage);
//                    Platform.runLater(() -> {
//                        imageView.setImage(fxImage);
//                        Circle clip = new Circle(220, 165, 165);
//                        imageView.setClip(clip);
//                    });
//                }
//            }
//        });

        Thread cameraThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            List<FacePrediction> predictions = new ArrayList<>();

            while (runnable) {
                if (webSource.read(cameraImage)) {
                    Mat grayImage = new Mat();
                    opencv_imgproc.cvtColor(cameraImage, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);

                    RectVector detectedFaces = new RectVector();
                    cascade.detectMultiScale(grayImage, detectedFaces);
                    long facesNumber = detectedFaces.size();
                    Map<Integer, Integer> faceCount = new HashMap<>();

                    for (int i = 0; i < facesNumber && facesNumber == 1; i++) {
                        Rect faceData = detectedFaces.get(i);

                        Mat croppedFace = new Mat(grayImage, faceData);
                        opencv_imgproc.resize(croppedFace, croppedFace, new org.bytedeco.opencv.opencv_core.Size(160, 160));

                        int[] label = new int[1];
                        double[] confidence = new double[1];
                        recognizer.predict(croppedFace, label, confidence);
                        System.out.println("Confidence: " + confidence[0]);

                        // Store the prediction for tracking
                        predictions.add(new FacePrediction(label[0], confidence[0], faceData));

                        // If face is unknown or confidence is too high, mark as unknown
                        if (label[0] == -1 || confidence[0] > 90) {
                            idPerson = -1;
                            updateLabels("Unknown", "", "");
                            opencv_imgproc.rectangle(cameraImage, faceData, new org.bytedeco.opencv.opencv_core.Scalar(0, 0, 255, 3), 3, 0, 0);
                        } else {
                            idPerson = label[0];
                            opencv_imgproc.rectangle(cameraImage, faceData, new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 3, 0, 0);

                            // Display the name above the rectangle
                            if (user != null) {
                                int textX = faceData.x();
                                int textY = Math.max(faceData.y() - 10, 0);
                                opencv_imgproc.putText(cameraImage, user.getFirstName() + " " + user.getLastName(),
                                        new org.bytedeco.opencv.opencv_core.Point(textX, textY),
                                        opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.8,
                                        new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 2, 0, false);
                            }

                            faceCount.put(idPerson, faceCount.getOrDefault(idPerson, 0) + 1);
                        }
                    }

                    // Check elapsed time and process predictions
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    if (elapsedTime >= 1000) { // 1 second window
                        // Find the most recognized face (lowest confidence)
                        if (!predictions.isEmpty()) {
                            FacePrediction bestPrediction = predictions.stream()
                                    .min(Comparator.comparingDouble(FacePrediction::getConfidence))
                                    .orElse(null);

                            if (bestPrediction != null) {
                                // Update user information with the most recognized face
                                int mostRecognizedFace = bestPrediction.getLabel();
                                user = fetchPersonData(mostRecognizedFace);
                                if ((!UserIdField.getText().isEmpty() && user.getId() != Integer.parseInt(UserIdField.getText()))) {
                                    clearUserForm();
                                }

                                // Draw the name above the face with the lowest confidence
                                if (user != null) {
                                    int textX = bestPrediction.getFace().x();
                                    int textY = Math.max(bestPrediction.getFace().y() - 10, 0);
                                    opencv_imgproc.putText(cameraImage, user.getFirstName() + " " + user.getLastName(),
                                            new org.bytedeco.opencv.opencv_core.Point(textX, textY),
                                            opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.8,
                                            new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 3), 2, 0, false);

                                    if (user.isAdmin()) {
                                        // Handle admin user logic here
                                        System.out.println("You are an admin");
                                    }
                                }
                            }
                        }

                        // Reset for the next window
                        predictions.clear();
                        startTime = System.currentTimeMillis();
                    }

                    // Display the frame
                    Image fxImage = Utils.matToImage(cameraImage);
                    Platform.runLater(() -> {
                        imageView.setImage(fxImage);
                        Circle clip = new Circle(220, 165, 165);
                        imageView.setClip(clip);
                    });
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
    private void goToHome() {
        tabPane.getSelectionModel().select(homeTab);
        webSource.release();
        clearUserForm();
    }

    @FXML
    private void showDetails() {
        UserIdField.setText(String.valueOf(user.getId()));
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        statusCheckBox.setSelected(user.getAccess());
        departmentField.setText(user.getDoor());
        registredDate.setValue(LocalDate.ofEpochDay(user.getRegistredDate()));
        sexComboBox.getItems().removeAll(sexComboBox.getItems());
        sexComboBox.getItems().addAll("", "Male", "Female");
        sexComboBox.getSelectionModel().select("");
        sexComboBox.getSelectionModel().select(user.getSex());
        isAdmin.setSelected(user.isAdmin());
    }

    void clearUserForm() {
        UserIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        statusCheckBox.setSelected(false);
        departmentField.setText("");
        registredDate.setValue(LocalDate.now());
        sexComboBox.getSelectionModel().select("");
    }

    @FXML
    private void closeLogin() {
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

}