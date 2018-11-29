package com.uaic.ai.service;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class BinaryMatrixImpl implements BinaryMatrix {

    private boolean[][] matrix;

    public BinaryMatrixImpl() { }

    public void compute(String path) {
        Mat pixelMatrix = Imgcodecs.imread(path);
        byte[] data = new byte[3];
        matrix = new boolean[pixelMatrix.rows()][pixelMatrix.cols()];
        int darkPixel = 0, brightPixel = 0, brightness = 0, pixel, darkPixelCount = 0;
        //GET BRIGHTEST AND DARKEST PIXEL
        for (int i = 0; i < pixelMatrix.rows(); i++) {
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                brightness = brightness + (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
            }
        }

        //System.out.println(brightness + " " + pixelMatrix.rows() * pixelMatrix.cols());
        brightness = (brightness / (pixelMatrix.rows() * pixelMatrix.cols()));

        //BUILD THE MATRIX
        for (int i = 0; i < pixelMatrix.rows(); i++)
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                pixel = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;

                if (pixel < brightness) {
                    darkPixel += pixel;
                    darkPixelCount++;
                } else {
                    brightPixel += pixel;
                }
            }

        if (darkPixelCount > 0)
            darkPixel = darkPixel / darkPixelCount;
        brightPixel = brightPixel / (pixelMatrix.rows() * pixelMatrix.cols() - darkPixelCount);

        for (int i = 0; i < pixelMatrix.rows(); i++)
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                pixel = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
                matrix[i][j] = ((pixel - darkPixel) * (pixel - darkPixel) < (brightPixel - pixel) * (brightPixel - pixel));
            }

    }

    public boolean[][] getMatrix() {
        return matrix;
    }


    public void setMatrix(boolean[][] matrix) {
        this.matrix = matrix;
    }

    public boolean containsFootnotes() {
        return getFootnotesBeginningLine() != -1;
    }

    public int getFootnotesBeginningLine() {

        //TODO: remove this when refactor code, should be only in Application
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        BinaryMatrixImpl m = new BinaryMatrixImpl();
        m.compute("C:\\Users\\MasterCode\\Desktop\\AI proiect\\2.jpg");
        double blackPixelsPerLine = 0, blackPixelsPerTextLine = 0, blackPixelsPerEmptyLine = 0;
        int blackPixelCount = 0, textLines = 0, emptyLines = 0;
        int[] blackPixelsInLine = new int[m.matrix.length];
        double[] linesPixelPercentage = new double[m.matrix.length];

        double actualPixelsPerLine = 0;
        double avgEmptyLinesBetweenTextsLines = 0;
        int emptySpacesCount = 0;
        double inferiorLimit = 0;



        for (int i = 0; i < m.matrix.length; i++) {
            blackPixelCount = 0;
            for (int j = 0; j < m.matrix[i].length; j++) {
                blackPixelCount += m.matrix[i][j] ? 1 : 0;
            }
            blackPixelsInLine[i] = blackPixelCount;
        }
        blackPixelCount = 0;
        for (int i = 0; i < blackPixelsInLine.length; i++)
            blackPixelCount += blackPixelsInLine[i];

        blackPixelsPerLine = (double) blackPixelCount / (m.matrix.length * m.matrix[0].length);
        for (int i = 0; i < m.matrix.length; i++) {

            if (((double) blackPixelsInLine[i] / m.matrix[i].length) > blackPixelsPerLine) {
                blackPixelsPerTextLine += (double) blackPixelsInLine[i] / m.matrix[i].length;
                textLines++;
            } else {
                blackPixelsPerEmptyLine += (double) blackPixelsInLine[i] / m.matrix[i].length;
                emptyLines++;
            }
        }
        blackPixelsPerTextLine = blackPixelsPerTextLine / textLines;
        blackPixelsPerEmptyLine = blackPixelsPerEmptyLine / emptyLines;


        emptyLines = 0;

        boolean lineIsText[] = new boolean[blackPixelsInLine.length];

        for (int i = 0; i < blackPixelsInLine.length; i++) {
            actualPixelsPerLine = (double) blackPixelsInLine[i] / m.matrix[i].length;
            if (((actualPixelsPerLine - blackPixelsPerEmptyLine) * (actualPixelsPerLine - blackPixelsPerEmptyLine)) < ((actualPixelsPerLine - blackPixelsPerLine) * (actualPixelsPerLine - blackPixelsPerLine))) {
                lineIsText[i] = false;
                emptyLines++;
            } else {
                lineIsText[i] = true;
                if (emptyLines > 0) {
                    avgEmptyLinesBetweenTextsLines += emptyLines;
                    emptyLines = 0;
                    emptySpacesCount++;
                }

            }
        }
        avgEmptyLinesBetweenTextsLines = avgEmptyLinesBetweenTextsLines / emptySpacesCount;

        emptyLines = 0;
        emptySpacesCount = 0;
        for (int i = 0; i < lineIsText.length; i++) {
            if (!lineIsText[i]) {
                emptyLines++;

            } else {
                if ((emptyLines > avgEmptyLinesBetweenTextsLines) && (emptyLines > 0)) {
                    inferiorLimit += emptyLines;
                    emptySpacesCount++;
                    emptyLines = 0;
                }
            }
        }
        avgEmptyLinesBetweenTextsLines = inferiorLimit / emptySpacesCount;
        emptyLines = 0;
        emptySpacesCount = 0;

        //UNCOMMENT FOR DEBUG PURPOSE
        for (int i = lineIsText.length - (lineIsText.length / 3); i < lineIsText.length; i++) {

            if (!lineIsText[i]) {
                emptyLines++;
                //System.out.print("EMPTY: ");
            } else {
                if (emptyLines > avgEmptyLinesBetweenTextsLines) {
                    emptySpacesCount++;
                    return i;
                    //System.out.println("UNUSUAL:");

                }
                emptyLines = 0;
                //System.out.print("TEXT:  ");
            }

			/*
			for(int j=0;j<m.matrix[i].length;j++)
				System.out.print(m.matrix[i][j]?1:" ");
			System.out.println(" ");
			*/
        }


        //PRINT STATISTICS
		/*System.out.println("Empty lines: "+emptyLines);
		System.out.println("Black Pixels Per Line: "+blackPixelsPerLine);
		System.out.println("Black Pixels Per Text Line: "+blackPixelsPerTextLine);
		System.out.println("Black Pixels Per Empty Line: "+blackPixelsPerEmptyLine);
		System.out.println("Average Empty Lines Between Texts Lines: "+avgEmptyLinesBetweenTextsLines);
		System.out.println("Large Empty Spaces between texts in the footnote zone: "+emptySpacesCount);*/

        return -1;


    }

}
