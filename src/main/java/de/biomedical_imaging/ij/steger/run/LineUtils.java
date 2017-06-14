package de.biomedical_imaging.ij.steger.run;

import de.biomedical_imaging.ij.steger.Line;
import de.biomedical_imaging.ij.steger.Lines;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sampanna.kahu on 14/06/17.
 */
public class LineUtils {

    public static Lines deduplicateLines(Lines lines) {
        Lines uniqueLines = new Lines(0);
        //iterate over each input lines and add it to the unique lines list depending on checks.
        lines.forEach(line -> {
            final boolean[] duplicateFound = {false};
            // iterate over each unique line.
            uniqueLines.forEach(uniqueLine -> {
                //check if the line of interest is equal to the current unique line. If duplicate is found, set the flag to true.
                if (Arrays.equals(uniqueLine.getXCoordinates(), line.getXCoordinates()) && Arrays.equals(uniqueLine.getYCoordinates(), line.getYCoordinates())) {
                    duplicateFound[0] = true;
                }
            });
            //if no duplicates were found, add the line of interest to the unique lines.
            if (!duplicateFound[0]) {
                uniqueLines.add(line);
            }
        });
        return uniqueLines;
    }

    static Lines findJoinedLines(Lines lesserLines, Lines moreLines, float gapTolerance) {
        Lines outputLines = new Lines(0);
        lesserLines.forEach(line -> {
            //add the current line to the output
            outputLines.add(line);
            //search and add to output
            for (int i = 0; i < line.getNumber(); i++) {
                outputLines.addAll(centerToCenterNearnessSearch(line.getXCoordinates()[i], line.getYCoordinates()[i], moreLines, gapTolerance));
            }
        });
        return outputLines;
    }

    private static Lines centerToCenterNearnessSearch(float xCoordinate, float yCoordinate, Lines searchableLines, float searchTolerance) {
        Lines matchedLines = new Lines(0);
        searchableLines.forEach(searchableLine -> {
            if (containsIn(searchableLine.getXCoordinates(), xCoordinate, searchTolerance) && containsIn(searchableLine.getYCoordinates(), yCoordinate, searchTolerance)) {
                matchedLines.add(searchableLine);
            }
        });
        return matchedLines;
    }

    private static boolean containsIn(float[] arrayToSearchIn, float termToSearch, float tolerance) {
        for (float possibleMatch : arrayToSearchIn) {
            if (Math.abs(possibleMatch - termToSearch) < tolerance) {
                return true;
            }
        }
        return false;
    }

    private static Overlay getOverlayPolygon(Lines lines, ImageProcessor imageProcessor, boolean doEstimateWidth) {
        ImagePlus imagePlus = new ImagePlus("venogram_with_contours_drawn", imageProcessor);
        imagePlus.setOverlay(null);
        Overlay overlay = new Overlay();

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
                overlay.add(polyRoiMitte);


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
                        overlay.add(polyRoiRand1);

                        PolygonRoi polyRoiRand2 = new PolygonRoi(polyR,
                                                                 Roi.POLYLINE);
                        polyRoiRand2.setStrokeColor(Color.green);
                        polyRoiRand2.setPosition(position);
                        overlay.add(polyRoiRand2);
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
        return overlay;
    }
}
