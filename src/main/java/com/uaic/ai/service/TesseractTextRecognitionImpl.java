package com.uaic.ai.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * This class requires Tesseract ( tess4j )
 */

@Service
public class TesseractTextRecognitionImpl implements TesseractTextRecognition {

    public String getImgText(String imageLocation) {

        ITesseract instance = new Tesseract();
        instance.setDatapath("C:\\Program Files\\Tesseract-OCR");
        instance.setLanguage("eng");
        try {
            String imgText = instance.doOCR(new File(imageLocation));
            return imgText;
        } catch (TesseractException e) {
            e.getMessage();
            return "TesseractException e: Error while reading image";
        }
    }

}
