package com.uaic.ai.service;

public interface BinaryMatrix{

    void createMatrix(String path);

    boolean[][] getMatrix();

    void setMatrix(boolean[][] matrix);

    boolean containsFootnotes();

    int[] getBlackPixelsPerLine(boolean[][] matrix);

    int getFootnotesBeginningLine();

}
