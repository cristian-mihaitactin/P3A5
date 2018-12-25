package com.uaic.ai.service;

import java.awt.Point;
import java.util.ArrayList;

public interface SimpleOperations {
    ArrayList<Double> getVerticalBlackness(boolean[][] pixels);

    ArrayList<Double> getHorizontalBlackness(boolean[][] pixels);
    
    boolean[][] getPartOfPixels(boolean[][] pixels, Point topLeftCorner, Point topRightCorner, Point bottomLeftCorner, Point bottomRightCorner);
}
