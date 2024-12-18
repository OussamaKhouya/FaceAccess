package team.project.faceaccess.controllers;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import team.project.faceaccess.models.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.ResourceBundle;

public class CaptureController {
    private static int id=0;
    @FXML
    private Button CloseButton;
    @FXML
    private ComboBox<?> cameraComboBox;

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


    private OpenCVFrameGrabber grabber;
    private volatile boolean isRunning = true;


//    public void initialize() {
//        startCamera();
//    }

    @Setter
    private User user;
    private UsersManagementView usersManagementView;

    public void startCamera() {
        grabber = new OpenCVFrameGrabber(0); // Open default webcam
        new Thread(() -> {
            try {
                grabber.start();
                Java2DFrameConverter converter = new Java2DFrameConverter();

                while (isRunning) {
                    Frame frame = grabber.grab(); // Capture a frame
                    if (frame != null) {
                        BufferedImage bufferedImage = converter.convert(frame);
                        if (bufferedImage != null) {
                            Platform.runLater(() -> {
                                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                                imageView.setImage(image);
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void capturePhoto() {

        try {
             Frame frame = grabber.grab(); // Capture a single frame
            if (frame != null) {
                // Convert the frame to a BufferedImage
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.convert(frame);

                Platform.runLater(() -> {
                    Image capturedImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    capturedImageView.setImage(capturedImage);
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML
    void connectButtonOnClick(ActionEvent event) {
        System.out.println("Connect");
        startCamera();
    }

    @FXML
    void disconnectButtonOnClick(ActionEvent event) {
        System.out.println("Disconnect");
//        stopCamera();
    }

    @FXML
    void saveButtonOnClick(ActionEvent event) {
        System.out.println("save");
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
                    usersManagementView.updateBtn.fire();
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
        // Get the current stage using the button's scene
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
        stopCamera();
    }
    public void stopCamera() {
        isRunning = false;
        try {
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void setParentController(UsersManagementView usersManagementView) {
        this.usersManagementView=usersManagementView;
    }
}
