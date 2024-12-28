package team.project.faceaccess.controllers;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import team.project.faceaccess.models.User;
import team.project.faceaccess.utils.TrainLBPH;
import team.project.faceaccess.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class CaptureController implements Initializable {
    @FXML
    private Button CloseButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button captureButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button disconnectButton;

    @FXML
    private Button saveButton;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView capturedImageView;

    private VideoCapture webSource;
    private final Mat cameraImage = new Mat();
    private Mat CapturedcameraImage = new Mat();
    private CascadeClassifier cascade;
    private int sample = 1;
    private final int maxNumSamples = 50;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Check if the classifier XML file exists
        String cascadePath = "photos//haarcascade_frontalface_alt.xml";
        File cascadeFile = new File(cascadePath);
        if (!cascadeFile.exists()) {
            throw new RuntimeException("Error: Classifier XML file not found at " + cascadePath);
        }

        cascade = new CascadeClassifier(cascadePath);
    }

    private CaptureController.DaemonThread myThread = null;

    private void startCamera() {

        new Thread(() -> {
            webSource = new VideoCapture(0);

            myThread = new CaptureController.DaemonThread();
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            myThread.runnable = true;
            t.start();
        }).start();
    }

    private void stopCamera() {
        if(myThread != null){
            myThread.setRunnable(false);
        }
        webSource.release();
        imageView.setImage(null);

    }

    private class DaemonThread implements Runnable {
        private volatile boolean runnable = false;

        @Override
        public void run() {
            while (runnable) {
                try {
                    if (webSource.grab()) {
                        webSource.retrieve(cameraImage);
                        CapturedcameraImage = new Mat(cameraImage);
                        Mat grayImage = new Mat(); //grayscale image
                        opencv_imgproc.cvtColor(cameraImage, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);

                        RectVector detectedFaces = new RectVector(); //detected face
                        cascade.detectMultiScale(grayImage, detectedFaces);

                        for (int i = 0; i < detectedFaces.size(); i++) { //loop to find faces
                            Rect faceData = detectedFaces.get(0);

                            // Draw rectangle around detected face
                            opencv_imgproc.rectangle(cameraImage, faceData, new org.bytedeco.opencv.opencv_core.Scalar(0, 255, 0, 0), 3, 0, 0);

                            // Crop and resize the face to 160x160
                            Mat croppedFace = new Mat(grayImage, faceData);
                            Mat resizedFace = new Mat();
                            opencv_imgproc.resize(croppedFace, resizedFace, new org.bytedeco.opencv.opencv_core.Size(160, 160));
                            if (saveButton.isPressed()) { //when the saveButton is pressed
                                if (sample <= maxNumSamples) {
                                    // Save cropped face as an image file
                                    String outputPath = "photos//person." + user.getId() + "." + sample + ".jpg";
                                    opencv_imgcodecs.imwrite(outputPath, resizedFace);
                                    System.out.println("Saved face at " + outputPath);

                                    Platform.runLater(() -> counterLabel.setText(sample * 2 + "/" + maxNumSamples * 2));
                                    progress.setProgress((double) (sample) / maxNumSamples);

                                    sample++;
                                }
                                if (sample > maxNumSamples) {
                                    new TrainLBPH().trainPhotos(); // Run the training after taking photos, generates the train file
                                    System.out.println("trainer file generated.");
                                    stopCamera(); // Stop the camera
                                    System.out.println("user id=" + user.getId());
                                    saveButtonOnClick();
                                    Platform.runLater(() ->closeWindows());
                                }
                            }
                        }

                        // Convert to JavaFX Image and display
                        Image fxImage = Utils.matToImage(cameraImage);
                        Platform.runLater(() -> imageView.setImage(fxImage));
                        synchronized (this) {
                            if (!runnable) {
                                System.out.println("Save the Photo");
                                this.wait();
                            }
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void setRunnable(boolean runnable) {
            this.runnable = runnable;
        }
    }

    private void closeWindows() {
        cancelButton.fire();
    }


    @FXML
    private ProgressBar progress;
    @Setter
    private User user;
    private UsersManagementController usersManagementController;


    @FXML
    private void capturePhoto() {
        Image fxImage = Utils.matToImage(CapturedcameraImage);
        Platform.runLater(() -> capturedImageView.setImage(fxImage));
        saveButton.setDisable(false);
    }


    @FXML
    void connectButtonOnClick(ActionEvent event) {
        startCamera();

    }

    @FXML
    void disconnectButtonOnClick(ActionEvent event) {
        stopCamera();
    }

    void saveButtonOnClick() {
        try {
            String filePath = String.format("photos/active/%s/1.png", user.getId());
            Path saveDir = Paths.get(filePath).getParent(); // Parent directory path

            // Ensure the directory exists, create if necessary
            if (saveDir != null && !Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
                System.out.println("Directory created: " + saveDir);
            }

            // Get the image from the ImageView
            Image fxImage = capturedImageView.getImage();
            if (fxImage != null) {
                System.out.println("Image found in ImageView.");

                // Convert the JavaFX Image to BufferedImage
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

                // Save the image to the specified file path
                File outputFile = new File(filePath);
                if (ImageIO.write(bufferedImage, "png", outputFile)) {
                    System.out.println("Image saved successfully to: " + filePath);
                    usersManagementController.updateBtn.fire();
                } else {
                    System.err.println("Failed to save the image. Unsupported format?");
                }
            } else {
                System.err.println("No image available in the ImageView to save.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeCapture(ActionEvent event) {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
        stopCamera();
    }

    public void setParentController(UsersManagementController controller) {
        this.usersManagementController = controller;
    }

    @FXML
    private Label counterLabel;

}

