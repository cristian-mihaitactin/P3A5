package com.uaic.ai.service;

import com.uaic.ai.model.Column;
import com.uaic.ai.model.Image;

import java.util.ArrayList;

public interface ColumnsRecognition {

    boolean isGoodSelection(int i, ArrayList<Integer> selection);

    ArrayList<Double> getVerticalBlackness(boolean[][] pixels);

    ArrayList<Double> getHorizontalBlackness(boolean[][] pixels);

    void thickenDelimitation(ArrayList<Integer> columnsDelimitation);

    ArrayList<Integer> normalizeDelimitation(ArrayList<Integer> delimitation);

    ArrayList<Integer> getDelimitation(ArrayList<Double> blackness);

    void verticallyCorrectColumn(Image image, Column column);

    void computeLinesOfColumns(Image image);

    void computeColumns(Image image);

}
