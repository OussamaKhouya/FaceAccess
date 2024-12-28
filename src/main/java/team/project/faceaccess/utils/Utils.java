package team.project.faceaccess.utils;

import javafx.scene.image.Image;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import java.awt.image.BufferedImage;

public class Utils {
    public static Image matToImage(Mat mat) {
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        BufferedImage bufferedImage = java2dConverter.convert(converter.convert(mat));
        return javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
    }
}