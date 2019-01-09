package com.uaic.ai.service;

import com.uaic.ai.model.Column;
import com.uaic.ai.model.Image;

import java.util.ArrayList;

public interface ColumnsRecognition {

    ArrayList<Integer> getDelimitation(ArrayList<Double> blackness, double sensitivity);
    
    void computeLinesOfColumns(Image image);
    
    void computeParagraphs(Image image);

    void computeColumns(Image image);

}
