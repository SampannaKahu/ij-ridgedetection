package de.biomedical_imaging.ij.steger.run;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.biomedical_imaging.ij.steger.LineDetector;
import de.biomedical_imaging.ij.steger.Lines;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;


/**
 * Created by sampanna.kahu on 12/05/17.
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
//        ImageJ imageJ = new ImageJ();
        String filePath = "/Users/sampanna.kahu/Workspace/ij-ridgedetection/src/main/resources/venogram.jpg";
        File file = new File(filePath);
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

        Lines detectedLines = lineDetector.detectLines(imageProcessor, config);
        detectedLines = LineUtils.deduplicateLines(detectedLines);
//        BufferedImage bufferedImage = DisplayUtils.displayContours(detectedLines, imageProcessor, config.isDoEstimateWidth());
//        DisplayUtils.displayImage(bufferedImage, "Detected lines for minLength=" + config.getMinLength());

        String bwFilePath = "/Users/sampanna.kahu/Workspace/ij-ridgedetection/src/main/resources/venogram_bw.jpg";
        File bwImageFile = new File(bwFilePath);
        ImageProcessor bwImage = new ColorProcessor(ImageIO.read(bwImageFile));

        LineUtils.filterLinesUsingBWImage(detectedLines, bwImage, 0, 0);
//        final Lines[] lesserLines = {lineDetector.detectLines(imageProcessor, config)};

//        minLengthList.forEach(minLength -> {
//            System.out.println("De-duplicating lines.");
//            lesserLines[0] = LineUtils.deduplicateLines(lesserLines[0]);
//            System.out.println("Printing lines for minLenght=" + config.getMinLength());
//            BufferedImage bufferedImage = DisplayUtils.displayContours(lesserLines[0], imageProcessor, config.isDoEstimateWidth());
//            System.out.println("Displaying lines for minLenght=" + config.getMinLength());
//            DisplayUtils.displayImage(bufferedImage, "Detected lines for minLength=" + config.getMinLength());
//            config.setMinLength(minLength);
//            System.out.println("Starting detection for minLength=" + config.getMinLength());
//            Lines moreLines = lineDetector.detectLines(imageProcessor, config);
//            System.out.println("Accumulating lines for minLength=" + config.getMinLength());
//            lesserLines[0] = LineUtils.findJoinedLines(lesserLines[0], moreLines, 4f);
//        });

//        BufferedImage bufferedImage = DisplayUtils.displayContours(lesserLines[0], imageProcessor, config.isDoEstimateWidth());
//        DisplayUtils.displayImage(bufferedImage, "Detected lines for minLength=" + config.getMinLength());

//-----------------------------------------------------------
//        System.out.println("Getting overlay polygons");
//        Overlay overlay = getOverlayPolygon(lines, imageProcessor, config.isDoEstimateWidth());
//        displayOverlayOnImage(imageProcessor, overlay);
//        System.out.println("Done.");
    }

    //TODO: WIP
//    private static Lines centerToEdgeNearnessSearch(float xCoordinateOfEdgePoint, float yCoordinateOfEdgePoint, Lines searchableines, float searchTolerance) {
//        Lines matchedLines = new Lines(0);
//        searchableines.forEach(searchableLine -> {
//            if ()
//        });
//    }

    private static void detectLinesAndSaveFile(ImageProcessor imageProcessor, int fileNumber, LineDetectionConfig config) throws JsonProcessingException, FileNotFoundException {
        System.out.println("Starting line detection for iteration: " + fileNumber);

        // Detect lines
        LineDetector lineDetector = new LineDetector();
        Lines lines = lineDetector.detectLines(imageProcessor, config);

        //Save those lines as JSON
        String value = MapperUtil.getObjectMapper().writeValueAsString(new LineResult(lines, config));
        try (PrintWriter out = new PrintWriter("/tmp/filename" + fileNumber + ".txt")) {
            out.println(value);
        }
        System.out.println("Done");
    }

}
