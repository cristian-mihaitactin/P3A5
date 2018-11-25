package com.uaic.ai.service;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class BinaryMatrixImpl implements BinaryMatrix {

    private boolean[][] matrix;

    public BinaryMatrixImpl() { }

    public void compute(String path) {
        Mat pixelMatrix = Imgcodecs.imread(path);
        byte[] data = new byte[3];
        matrix = new boolean[pixelMatrix.rows()][pixelMatrix.cols()];
        int min = 255, max = 0, brightness;
        //GET BRIGHTEST AND DARKEST PIXEL
        for (int i = 0; i < pixelMatrix.rows(); i++) {
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                brightness = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
                if (min > brightness)
                    min = brightness;
                if (max < brightness)
                    max = brightness;
            }
        }

        //BUILD THE MATRIX
        for (int i = 0; i < pixelMatrix.rows(); i++)
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                brightness = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
                matrix[i][j] = ((brightness - min) < (max - brightness));
            }

    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(boolean[][] matrix) {
        this.matrix = matrix;
    }

}
