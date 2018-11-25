package com.uaic.ai.service;

import java.text.DecimalFormat;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class BinaryMatrixImpl implements BinaryMatrix {

    private boolean[][] matrix;

    public BinaryMatrixImpl() {
    }

    public void compute(String path) {
        Mat pixelMatrix = Imgcodecs.imread(path);
        byte[] data = new byte[3];
        matrix = new boolean[pixelMatrix.rows()][pixelMatrix.cols()];
        int min = 255;
        int max = 0;
        int pixelBrightness;


        // GET BRIGHTEST AND DARKEST PIXEL
        for (int i = 0; i < pixelMatrix.rows(); i++) {
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                pixelBrightness = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
                if (min > pixelBrightness)
                    min = pixelBrightness;
                if (max < pixelBrightness)
                    max = pixelBrightness;
            }
        }

        // BUILD THE MATRIX
        // 1 - pixel is closer to black     0 - pixel is closer to white
        for (int i = 0; i < pixelMatrix.rows(); i++)
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                pixelBrightness = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
                matrix[i][j] = ((pixelBrightness - min) < (max - pixelBrightness));
            }
    }


    public boolean containsFootnotes() {

        //REFACTOR THIS
        BinaryMatrixImpl m = new BinaryMatrixImpl();
        m.compute("C:\\Users\\MasterCode\\Desktop\\AI proiect\\2.jpg");
        double[] linesPixelPercentage = new double[m.matrix.length];
        /*
         * for(int i=0;i<110;i++) { for(int j=0;j<110;j++)
         * System.out.print((m.matrix[i][j]?1:" ")+" "); System.out.println(""); }
         */
        for (int i = 0; i < m.matrix.length; i++) {
            int zeroP = 0, oneP = 0;
            for (int j = 0; j < m.matrix[i].length; j++) {
                if (m.matrix[i][j]) {
                    oneP++;
                } else
                    zeroP++;
                // System.out.print((m.matrix[i][j]?1:" ")+" ");
            }
            linesPixelPercentage[i] = (((double) oneP / (oneP + zeroP)) * 100);
            // System.out.println("\nLine: "+i+" Zeros: "+zeroP+" Ones: "+oneP);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        int sum = 0;

        for (int i = 0; i < linesPixelPercentage.length; i++) {
            sum += (linesPixelPercentage[i] * 100);
            System.out.println("Line: " + i + " Percentage: " + df.format(linesPixelPercentage[i]) + "%");
        }
        sum = (sum / 100) / linesPixelPercentage.length;
        System.out.println("MEDIA: " + sum);
        double linieLibera = sum / 10;
        int countLiniiLibere = 0;
        double medieCount = 0;
        int spatii = 0;
        boolean ok = false;
        for (int i = 0; i < linesPixelPercentage.length; i++) {
            if (linesPixelPercentage[i] < linieLibera) {
                countLiniiLibere++;
                ok = true;
            } else {
                if (ok) {
                    medieCount += countLiniiLibere;
                    spatii++;
                    countLiniiLibere = 0;
                    ok = false;
                }
            }
        }


        System.out.println("Media Count: " + medieCount / spatii);
        ok = false;
        countLiniiLibere = 0;
        for (int i = 0; i < linesPixelPercentage.length; i++) {
            if (linesPixelPercentage[i] < linieLibera) {
                countLiniiLibere++;

                if (countLiniiLibere > countLiniiLibere + (countLiniiLibere / 2))
                    ok = true;
            }
            if (ok) {

            }

        }
        return true;
    }




    public boolean[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(boolean[][] matrix) {
        this.matrix = matrix;
    }

}
