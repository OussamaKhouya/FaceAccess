package team.project.faceaccess.draft.faceRecognition;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import team.project.faceaccess.HelloApplication;

/**
 * The main class for a JavaFX application. It creates and handle the main
 * window with its resources (style, graphics, etc.).
 * 
 * This application handles a video stream and try to find any possible human
 * face in a frame. It can use the Haar or the LBP classifier.
 * 
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @version 1.0 (2014-01-10)
 * @since 1.0
 * 
 */
public class FaceRecognitionMain extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/team/project/faceaccess/presentation/JFXoverlay.fxml"));

			// create and style a scene
			Scene scene = new Scene(loader.load(), 800, 600);
			scene.getStylesheets().add(String.valueOf(HelloApplication.class.getResource("/team/project/faceaccess/presentation/application.css")));
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("Face Detection and Tracking");
			primaryStage.setScene(scene);
			// show the GUI
			primaryStage.show();
			
			// init the controller
			FXController controller = loader.getController();
			controller.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		launch(args);
	}
}