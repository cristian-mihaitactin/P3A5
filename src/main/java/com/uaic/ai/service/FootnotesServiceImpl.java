package com.uaic.ai.service;

import com.uaic.ai.model.Footnote;
import com.uaic.ai.model.Image;
import org.springframework.stereotype.Service;

import java.awt.Point;

@Service
public class FootnotesServiceImpl implements FootnotesService {

	private int getFootnotesBeginningLine(Image img) {
		int emptyLines = 0;
		boolean[] lineIsText = img.statistics.lineIsText;

		for (int i = lineIsText.length - (lineIsText.length / 3); i < lineIsText.length; i++) {

			if (!lineIsText[i]) {
				emptyLines++;
			} else {
				if (emptyLines > img.statistics.avgEmptyLinesBetweenTextsLines) {
					return i;

				}
				emptyLines = 0;
			}
		}
		return -1;
	}

	@Override
	public void computeFootnotes(Image img) {
		int x1 = getFootnotesBeginningLine(img);
		int x2 = x1;
		int y1 = -1, y2 = -1;
		int verticalSum = 0;
		double averageBlackPixelsInVerticalLine = 0;

		if (x1 == -1) {
			img.footnote = null;
			return;
		}

		for (int i = img.statistics.lineIsText.length - 1; i > x1; i--)
			if (img.statistics.lineIsText[i]) {
				x2 = i;
				break;
			}

		boolean[][] matrix = img.pixels;

		for (int i = x1; i <= x2; i++) {
			for (int j = 0; j < matrix[i].length; j++)
				if (matrix[i][j])
					averageBlackPixelsInVerticalLine++;
		}

		averageBlackPixelsInVerticalLine = averageBlackPixelsInVerticalLine / (matrix[x1].length);
		for (int i = 0; i < matrix[x1].length; i++) {
			verticalSum = 0;
			for (int j = x1; j < x2; j++) {
				if (matrix[j][i])
					verticalSum++;
			}
			if (verticalSum >= averageBlackPixelsInVerticalLine) {
				y1 = i;
				break;
			}
		}

		for (int i = matrix[x1].length - 1; i >= 0; i--) {
			verticalSum = 0;
			for (int j = x1; j < x2; j++) {
				if (matrix[j][i])
					verticalSum++;
			}
			if (verticalSum >= averageBlackPixelsInVerticalLine) {
				y2 = i;
				break;
			}
		}

		if (y1 < 0) {
			img.footnote = null;
			return;
		}

		img.footnote = new Footnote(new Point(y1, x1), new Point(y2, x1), new Point(y1, x2), new Point(y2, x2));
	}

}
