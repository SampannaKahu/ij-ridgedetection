package de.biomedical_imaging.ij.steger.run;

import de.biomedical_imaging.ij.steger.Line;
import de.biomedical_imaging.ij.steger.Lines;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by sampanna.kahu on 14/06/17.
 */
public class DisplayUtils {
    private static void displayOverlayOnImage(ImageProcessor imageProcessor, Overlay overlay, String title) {
        for (int i = 0; i < overlay.size(); i++) {
            overlay.get(i).drawPixels(imageProcessor);
        }
        JFrame frame = new JFrame(title);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(imageProcessor.getBufferedImage())));
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    static void displayImage(BufferedImage image, String title) {
        JFrame frame = new JFrame(title);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    static BufferedImage displayContours(Lines lines, ImageProcessor imageProcessor, boolean doEstimateWidth) {
        BufferedImage outputImage = imageProcessor.getBufferedImage();

        double linePointX, linePointY, nx, ny, rightEdgeX, rightEdgeY, leftEdgeX, leftEdgeY;
        double lastWidthRight, lastWidthLeft;

        // Print the line's center and the width.
        for (Line line : lines) {
            int num_points = line.getNumber();
            lastWidthRight = 0;
            lastWidthLeft = 0;

            for (int j = 0; j < num_points; j++) {
                linePointX = line.getXCoordinates()[j];
                linePointY = line.getYCoordinates()[j];
                plotPointOnImage(outputImage, new Double(linePointX + 0.5).intValue(), new Double(linePointY + 0.5).intValue(), Color.red.getRGB());

                if (doEstimateWidth) {
                    nx = Math.sin(line.getAngle()[j]);
                    ny = Math.cos(line.getAngle()[j]);
                    rightEdgeX = linePointX + line.getLineWidthR()[j] * nx;
                    rightEdgeY = linePointY + line.getLineWidthR()[j] * ny;
                    leftEdgeX = linePointX - line.getLineWidthL()[j] * nx;
                    leftEdgeY = linePointY - line.getLineWidthL()[j] * ny;

                    if (lastWidthRight > 0 && line.getLineWidthR()[j] > 0) {
                        plotPointOnImage(outputImage, new Double(rightEdgeX + 0.5).intValue(), new Double(rightEdgeY + 0.5).intValue(), Color.green.getRGB());
                    }
                    if (lastWidthLeft > 0 && line.getLineWidthL()[j] > 0) {
                        plotPointOnImage(outputImage, new Double(leftEdgeX + 0.5).intValue(), new Double(leftEdgeY + 0.5).intValue(), Color.green.getRGB());
                    }
                    lastWidthRight = line.getLineWidthR()[j];
                    lastWidthLeft = line.getLineWidthL()[j];
                }
            }
        }

        return outputImage;
    }

    private static void plotPointOnImage(BufferedImage image, int x, int y, int rgb) {
        try {
            image.setRGB(x, y, rgb);
        } catch (Exception e) {
            System.out.println("Exception while plotting point on image! " + e.toString());
        }
    }

    private static BufferedImage displayContours_old(Lines lines, ImageProcessor imageProcessor, boolean doEstimateWidth) {
        ImagePlus imagePlus = new ImagePlus("venogram_with_contours_drawn", imageProcessor);
        imagePlus.setOverlay(null);
        Overlay ovpoly = new Overlay();

        double px, py, nx, ny, px_r = 0, py_r = 0, px_l = 0, py_l = 0;
        double last_w_r, last_w_l;

        ArrayList<Lines> result = new ArrayList<>();
        result.add(lines);
        // Print contour and boundary
        for (int k = 0; k < result.size(); k++) {
            for (int i = 0; i < result.get(k).size(); i++) {
                FloatPolygon polyCentre = new FloatPolygon();

                FloatPolygon polyR = new FloatPolygon();
                FloatPolygon polyL = new FloatPolygon();
                Line contour = result.get(k).get(i);
                int num_points = contour.getNumber();
                last_w_r = 0;
                last_w_l = 0;

                for (int j = 0; j < num_points; j++) {

                    px = contour.getXCoordinates()[j];
                    py = contour.getYCoordinates()[j];
                    nx = Math.sin(contour.getAngle()[j]);
                    ny = Math.cos(contour.getAngle()[j]);
                    if (doEstimateWidth) {
                        px_r = px + contour.getLineWidthR()[j] * nx;
                        py_r = py + contour.getLineWidthR()[j] * ny;
                        px_l = px - contour.getLineWidthL()[j] * nx;
                        py_l = py - contour.getLineWidthL()[j] * ny;
                    }

                    polyCentre.addPoint((px + 0.5), (py + 0.5));
                    if (doEstimateWidth) {
                        if (last_w_r > 0 && contour.getLineWidthR()[j] > 0) {
                            polyR.addPoint((px_r + 0.5), (py_r + 0.5));
                        }
                        if (last_w_l > 0 && contour.getLineWidthL()[j] > 0) {
                            polyL.addPoint((px_l + 0.5), (py_l + 0.5));
                        }
                    }
                    if (doEstimateWidth) {
                        last_w_r = contour.getLineWidthR()[j];
                        last_w_l = contour.getLineWidthL()[j];
                    }
                }


                PolygonRoi polyRoiMitte = new PolygonRoi(polyCentre,
                                                         Roi.POLYLINE);

                polyRoiMitte.setStrokeColor(Color.red);
                int position = result.get(k).getFrame();
//                if (!doStack || isPreview) {
//                    position = imp.getCurrentSlice();
//                }

                polyRoiMitte.setPosition(position);
                ovpoly.add(polyRoiMitte);


                if (doEstimateWidth) {
                    if (polyL.npoints > 1) {
                        PolygonRoi polyRoiRand1 = new PolygonRoi(polyL,
                                                                 Roi.POLYLINE);
                        polyRoiRand1.setStrokeColor(Color.green);
                        position = result.get(k).getFrame();
//                        if (!doStack || isPreview) {
//                            position = imp.getCurrentSlice();
//                        }
                        polyRoiRand1.setPosition(position);
                        ovpoly.add(polyRoiRand1);

                        PolygonRoi polyRoiRand2 = new PolygonRoi(polyR,
                                                                 Roi.POLYLINE);
                        polyRoiRand2.setStrokeColor(Color.green);
                        polyRoiRand2.setPosition(position);
                        ovpoly.add(polyRoiRand2);
                    }
                }

                //Show IDs
//                if (showIDs) {/*
//					int posx =  polyMitte.xpoints[0];
//					int posy =  polyMitte.ypoints[0];
//					if(cont.cont_class == contour_class.cont_start_junc){
//						posx =  polyMitte.xpoints[polyMitte.npoints-1];
//						posy =  polyMitte.ypoints[polyMitte.npoints-1];
//					}
//					*/
//
//                    int posx = (int) polyCentre.xpoints[polyCentre.npoints / 2];
//                    int posy = (int) polyCentre.ypoints[polyCentre.npoints / 2];
//                    TextRoi tr = new TextRoi(posx, posy, "" + contour.getID());
//                    tr.setCurrentFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
//                    tr.setIgnoreClipRect(true);
//                    tr.setStrokeColor(Color.orange);
//                    tr.setPosition(resultJunction.get(k).getFrame());
//                    ovpoly.add(tr);
//                }
            }
        }
//        if (showJunctionPoints) {
//            // Print junctions
//
//            for (int k = 0; k < resultJunction.size(); k++) {
//                FloatPolygon pointpoly = new FloatPolygon();
//                for (int i = 0; i < resultJunction.get(k).size(); i++) {
//
//                    pointpoly.addPoint(resultJunction.get(k).get(i).x + 0.5, resultJunction.get(k).get(i).y + 0.5);
//                }
//
//                PointRoi pointroi = new PointRoi(pointpoly);
//                pointroi.setShowLabels(false);
//                int position = resultJunction.get(k).getFrame();
//                if (!doStack || isPreview) {
//                    position = imp.getCurrentSlice();
//                }
//                pointroi.setPosition(position);
//                ovpoly.add(pointroi);
//            }
//        }
        if (ovpoly.size() > 0) {
            imagePlus.setOverlay(ovpoly);
        }
        return imagePlus.getBufferedImage();
    }
}
