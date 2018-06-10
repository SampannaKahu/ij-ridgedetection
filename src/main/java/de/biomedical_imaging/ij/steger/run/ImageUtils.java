package de.biomedical_imaging.ij.steger.run;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author sampanna.kahu.
 */
public class ImageUtils {
    public static boolean[][] rgb2bw(BufferedImage rgbImage, double threshold) {
        if (threshold > 1 || threshold < 0) {
            throw new IllegalArgumentException("Threshold should be 0, 1 or between 0 and 1.");
        }
        int width = rgbImage.getWidth();
        int height = rgbImage.getHeight();

        boolean[][] binaryImage = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color colorPixel = new Color(rgbImage.getRGB(i, j));
                double grayValue = colorPixel.getRed() * 0.299 + colorPixel.getGreen() * 0.587 + colorPixel.getBlue() * 0.114;
                if (grayValue > threshold * 255) {
                    binaryImage[i][j] = true;
                }
            }
        }
        return binaryImage;
    }
}
