package com.uaic.ai.service;

public interface BinaryMatrix{

    void createMatrix(String path);

    boolean[][] getMatrix();

    void setMatrix(boolean[][] matrix);

    boolean containsFootnotes(String path);

    int[] getBlackPixelsPerLine(boolean[][] matrix);

    Integer getFootnotesBeginningLine(String path);

}
