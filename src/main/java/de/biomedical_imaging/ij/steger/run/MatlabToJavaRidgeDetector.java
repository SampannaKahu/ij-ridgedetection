package de.biomedical_imaging.ij.steger.run;

import de.biomedical_imaging.ij.steger.LineDetector;
import de.biomedical_imaging.ij.steger.Lines;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * @author sampanna.kahu.
 */
public class MatlabToJavaRidgeDetector {
    public MatlabToJavaRidgeDetector() {
    }

    public Lines detectLines(String absoluteFilePath, double sigma, double upperThreshold, double lowerThreshold, double minLength, double maxLength) throws IOException {
        File file = new File(absoluteFilePath);
        if (file.exists()) {
            System.out.println("File exists");
        } else {
            System.out.println("File not found.");
        }
        ImageProcessor imageProcessor = new ColorProcessor(ImageIO.read(file));
        LineDetector lineDetector = new LineDetector();

        LineDetectionConfig config = new LineDetectionConfig();
        //set the default configs values.
        config.setSigma(9.9);
        config.setUpperThreshold(0.2);
        config.setLowerThreshold(0.00);
        config.setMinLength(30);
        config.setMaxLength(2000);
        config.setDarkLine(true);
        config.setDoCorrectPosition(true);
        config.setDoEstimateWidth(true);
        config.setDoExtendLine(true);

        return lineDetector.detectLines(imageProcessor, config);
    }
}
