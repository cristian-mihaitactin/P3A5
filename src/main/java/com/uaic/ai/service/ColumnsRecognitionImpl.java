package com.uaic.ai.service;

import com.uaic.ai.model.Pixel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ColumnsRecognitionImpl implements ColumnsRecognition {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static boolean isGoodSelection(int i, ArrayList<Integer> selection) {
        if (i > 2 && (selection.get(i - 3).equals(selection.get(i)) && selection.get(i - 2).equals(selection.get(i))
                && selection.get(i - 1).equals(selection.get(i))))
            return true;
        if (i < selection.size() - 3 && (selection.get(i + 3).equals(selection.get(i))
                && selection.get(i + 2).equals(selection.get(i)) && selection.get(i + 1).equals(selection.get(i))))
            return true;

        return false;
    }

    public static Pixel[][] getPixelsFromImage(Mat image) {
        Pixel[][] pixels = new Pixel[image.rows()][image.cols()];

        for (int i = 0; i < image.rows(); i++) {
            for (int j = 0; j < image.cols(); j++) {
                pixels[i][j] = new Pixel(image.get(i, j)[0], image.get(i, j)[1], image.get(i, j)[2]);
            }
        }

        return pixels;
    }

    public static ArrayList<Double> getColumnsBlackness(Pixel[][] pixels) {
        ArrayList<Double> columnsBlackness = new ArrayList<Double>();

        for (int i = 0; i < pixels[0].length; i++) {
            columnsBlackness.add(0d);
        }

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                columnsBlackness.set(j, columnsBlackness.get(j) + pixels[i][j].getBlackness());
            }
        }

        for (int i = 0; i < columnsBlackness.size(); i++) {
            columnsBlackness.set(i, columnsBlackness.get(i) / pixels.length);
        }

        return columnsBlackness;
    }

    public static ArrayList<Integer> getColumnsDelimitation(ArrayList<Double> columnsBlackness) {
        double averageBlackness = 0d;

        for (Double number : columnsBlackness) {
            averageBlackness += number;
        }

        averageBlackness /= columnsBlackness.size();

        double averageAddon = (1 - averageBlackness) / 5;

        ArrayList<Integer> columnsDelimitation = new ArrayList<Integer>();

        for (Double number : columnsBlackness) {
            columnsDelimitation.add(number > averageBlackness + averageAddon ? 1 : 0);
        }

        ArrayList<Integer> leftRightNormalization = (ArrayList<Integer>) columnsDelimitation.clone();
        ArrayList<Integer> rightLeftNormalization = (ArrayList<Integer>) columnsDelimitation.clone();

        for (int i = 0; i < leftRightNormalization.size(); i++) {

            if (isGoodSelection(i, leftRightNormalization))
                leftRightNormalization.set(i, leftRightNormalization.get(i));
            else
                leftRightNormalization.set(i, ((leftRightNormalization.get(i) + 1) % 2));
        }

        for (int i = 0; i < rightLeftNormalization.size(); i++) {

            if (isGoodSelection(i, rightLeftNormalization))
                rightLeftNormalization.set(i, rightLeftNormalization.get(i));
            else
                rightLeftNormalization.set(i, ((rightLeftNormalization.get(i) + 1) % 2));
        }

        columnsDelimitation = new ArrayList<Integer>();

        for (int i = 0; i < leftRightNormalization.size(); i++) {
            columnsDelimitation.add((rightLeftNormalization.get(i) == 0 || leftRightNormalization.get(i) == 0) ? 0 : 1);
        }

        return columnsDelimitation;
    }

    public static void main(String[] args) {

        Mat image = Imgcodecs.imread("sample.jpg");

        Pixel[][] pixels = getPixelsFromImage(image);

        ArrayList<Double> columnsBlackness = getColumnsBlackness(pixels);

        ArrayList<Integer> columnsDelimitation = getColumnsDelimitation(columnsBlackness);

        for (Integer number : columnsDelimitation) {
            System.out.print(number);
        }

        System.out.println();
        boolean inColumn = false;
        for (int i = 1; i < columnsDelimitation.size(); i++) {
            if (columnsDelimitation.get(i) != columnsDelimitation.get(i - 1)) {
                if (!inColumn) {
                    System.out.print("Coloana incepe de la " + i);
                } else {
                    System.out.println(" si se sfarseste la " + i);
                }
                inColumn = !inColumn;
            }
        }

    }

}
