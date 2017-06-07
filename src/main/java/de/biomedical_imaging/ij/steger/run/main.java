package de.biomedical_imaging.ij.steger.run;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.biomedical_imaging.ij.steger.LineDetector;
import de.biomedical_imaging.ij.steger.Lines;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;


/**
 * Created by sampanna.kahu on 12/05/17.
 */
public class Main {
    private static final Logger       LOGGER       = Logger.getLogger(Main.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
//        ImageJ imageJ = new ImageJ();
        File file = new File("/Users/sampanna.kahu/Workspace/ij-ridgedetection/src/main/resources/venogram.jpg");
        if (file.exists()) {
            System.out.println("File exists");
        } else {
            System.out.println("File not found.");
        }
        ImageProcessor imageProcessor = new ColorProcessor(ImageIO.read(file));

        LineDetectionConfig config = new LineDetectionConfig();
        //set the default configs values.
        config.setSigma(9.9);
        config.setUpperThreshold(0.2);
        config.setLowerThreshold(0.00);
        config.setMinLength(10);
        config.setMaxLength(1410);
        config.setDarkLine(true);
        config.setDoCorrectPosition(true);
        config.setDoEstimateWidth(true);
        config.setDoExtendLine(true);

//        detectLinesAndSaveFile(imageProcessor, 222, config);

        double clow = 0;
        double chigh = 0;
        double lineWidth = 0;


        int fileNumber = 0;
        for (double sigma = 0.0; sigma <= 20.0; sigma = sigma + 0.1) {

//            double estimatedSigma = lineWidth / (2 * Math.sqrt(3)) + 0.5;
//
//            double estimatedLowerThresh = Math.floor(Math.abs(-2
//                                                                      * clow
//                                                                      * (lineWidth / 2.0)
//                                                                      / (Math.sqrt(2 * Math.PI) * estimatedSigma * estimatedSigma * estimatedSigma)
//                                                                      * Math.exp(-((lineWidth / 2.0) * (lineWidth / 2.0))
//                                                                                         / (2 * estimatedSigma * estimatedSigma))));
//
//
//            double estimatedUpperThresh = Math.floor(Math.abs(-2
//                                                                      * chigh
//                                                                      * (lineWidth / 2.0)
//                                                                      / (Math.sqrt(2 * Math.PI) * estimatedSigma * estimatedSigma * estimatedSigma)
//                                                                      * Math.exp(-((lineWidth / 2.0) * (lineWidth / 2.0))
//                                                                                         / (2 * estimatedSigma * estimatedSigma))));




            fileNumber = fileNumber + 1;
            config.setSigma(sigma);
            try {
                spawnExecutionThread(imageProcessor, fileNumber, config);
            } catch (Exception e) {
                System.out.println("Failed to detect lines for config: " + config.toString() + ". Stack trace: " + e.toString());
            }
        }
        System.out.println("All done!");
    }

    private static void spawnExecutionThread(ImageProcessor imageProcessor, int fileNumber, LineDetectionConfig config) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(() -> {
            try {
                detectLinesAndSaveFile(imageProcessor, fileNumber, config);
            } catch (JsonProcessingException | FileNotFoundException e) {
                System.out.println("Failed to detect lines for config: " + config.toString() + ". Stack trace: " + e.toString());
            }
        });
        future.get(10, TimeUnit.SECONDS);
    }

    private static void detectLinesAndSaveFile(ImageProcessor imageProcessor, int fileNumber, LineDetectionConfig config) throws JsonProcessingException, FileNotFoundException {
        System.out.println("Starting line detection for iteration: " + fileNumber);

        // Detect lines
        LineDetector lineDetector = new LineDetector();
        Lines lines = lineDetector.detectLines(imageProcessor, config);

        //Save those lines as JSON
        String value = objectMapper.writeValueAsString(new LineResult(lines, config));
        try (PrintWriter out = new PrintWriter("/tmp/filename" + fileNumber + ".txt")) {
            out.println(value);
        }
        System.out.println("Done");
    }
}
