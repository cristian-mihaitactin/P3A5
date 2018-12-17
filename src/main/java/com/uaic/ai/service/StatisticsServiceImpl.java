package com.uaic.ai.service;

import com.uaic.ai.model.Image;
import com.uaic.ai.model.Statistics;
import org.opencv.core.Core;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService{


    private int[] getBlackPixelsPerLine(boolean[][] matrix) {
        int[] blackPixelsInLine = new int[matrix.length];
        int blackPixelCount;
        for (int i = 0; i < matrix.length; i++) {
            blackPixelCount = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                blackPixelCount += matrix[i][j] ? 1 : 0;
            }
            blackPixelsInLine[i] = blackPixelCount;
        }
        return blackPixelsInLine;
    }

    @Override
    public Statistics computeStatistics(Image img) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        boolean[][] matrix = img.pixels;

        double blackPixelsPerLine = 0, blackPixelsPerTextLine = 0, blackPixelsPerEmptyLine = 0;
        int blackPixelCount = 0, emptyLines = 0;
        int[] blackPixelsInLine = new int[matrix.length];

        double avgBlackPixelsPerLine = 0;
        double avgEmptyLinesBetweenTextsLines = 0;
        int emptySpacesCount = 0;
        double inferiorLimit = 0;

        blackPixelsInLine = getBlackPixelsPerLine(matrix);

        // count number of black pixels in page
        for (int i = 0; i < blackPixelsInLine.length; i++)
            blackPixelCount += blackPixelsInLine[i];


        // count empty lines and text lines
        blackPixelsPerLine = (double) blackPixelCount / (matrix.length * matrix[0].length);
        for (int i = 0; i < matrix.length; i++) {

            if (((double) blackPixelsInLine[i] / matrix[i].length) > blackPixelsPerLine) {
                blackPixelsPerTextLine += (double) blackPixelsInLine[i] / matrix[i].length;
            } else {
                blackPixelsPerEmptyLine += (double) blackPixelsInLine[i] / matrix[i].length;
                emptyLines++;
            }
        }

        blackPixelsPerEmptyLine = blackPixelsPerEmptyLine / emptyLines;


        emptyLines = 0;

        boolean lineIsText[] = new boolean[blackPixelsInLine.length];

        for (int i = 0; i < blackPixelsInLine.length; i++) {
            avgBlackPixelsPerLine = (double) blackPixelsInLine[i] / matrix[i].length;

            // avgBlackPixelsPerLine closer to number of blackPixels per empty line, or to number of pixels per text line ?
            if (((avgBlackPixelsPerLine - blackPixelsPerEmptyLine) * (avgBlackPixelsPerLine - blackPixelsPerEmptyLine)) < ((avgBlackPixelsPerLine - blackPixelsPerLine) * (avgBlackPixelsPerLine - blackPixelsPerLine))) {
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


        int consecutiveEmptyLines = 0;
        emptySpacesCount = 0;
        for (int i = 0; i < lineIsText.length; i++) {
            if (!lineIsText[i]) {
                consecutiveEmptyLines++;
            } else {
                if ((consecutiveEmptyLines > avgEmptyLinesBetweenTextsLines) && (consecutiveEmptyLines > 0)) {
                    inferiorLimit += consecutiveEmptyLines;
                    emptySpacesCount++;
                    consecutiveEmptyLines = 0;
                }
            }
        }
        avgEmptyLinesBetweenTextsLines = inferiorLimit / emptySpacesCount;


        Statistics statistics = new Statistics();
        statistics.blackPixelsPerLine = blackPixelsPerLine;
        statistics.blackPixelsPerTextLine = blackPixelsPerTextLine;
        statistics.blackPixelsPerEmptyLine = blackPixelsPerEmptyLine;
        statistics.avgBlackPixelsPerLine = avgBlackPixelsPerLine;
        statistics.avgEmptyLinesBetweenTextsLines = avgEmptyLinesBetweenTextsLines;
        statistics.blackPixelCount = blackPixelCount;
        statistics.blackPixelsInLine = blackPixelsInLine;
        statistics.lineIsText = lineIsText;

        //img.statistics = statistics;
        return statistics;
    }

}
