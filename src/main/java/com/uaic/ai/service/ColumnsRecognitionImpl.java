package com.uaic.ai.service;

import com.uaic.ai.model.Column;
import com.uaic.ai.model.Image;
import com.uaic.ai.model.Line;
import com.uaic.ai.model.Pixel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class ColumnsRecognitionImpl implements ColumnsRecognition {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static boolean isGoodSelection(int i, ArrayList<Integer> selection) {
		if (i > 1 && (selection.get(i - 2).equals(selection.get(i)) && selection.get(i - 1).equals(selection.get(i))))
			return true;
		if (i < selection.size() - 2
				&& (selection.get(i + 2).equals(selection.get(i)) && selection.get(i + 1).equals(selection.get(i))))
			return true;

		return false;
	}

	private static ArrayList<Double> getVerticalBlackness(boolean[][] pixels) {
		ArrayList<Double> verticalBlackness = new ArrayList<Double>();

		for (int i = 0; i < pixels[0].length; i++) {
			verticalBlackness.add(0d);
		}

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				verticalBlackness.set(j, verticalBlackness.get(j) + (pixels[i][j] ? 0 : 1));
			}
		}

		for (int i = 0; i < verticalBlackness.size(); i++) {
			verticalBlackness.set(i, verticalBlackness.get(i) / pixels.length);
		}

		return verticalBlackness;
	}

	private static ArrayList<Double> getHorizontalBlackness(boolean[][] pixels) {
		ArrayList<Double> horizontalBlackness = new ArrayList<Double>();

		for (int i = 0; i < pixels.length; i++) {
			horizontalBlackness.add(0d);
		}

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				horizontalBlackness.set(i, horizontalBlackness.get(i) + (pixels[i][j] ? 0 : 1));
			}
		}

		for (int i = 0; i < horizontalBlackness.size(); i++) {
			horizontalBlackness.set(i, horizontalBlackness.get(i) / pixels[0].length);
		}

		return horizontalBlackness;
	}

	private static void thickenDelimitation(ArrayList<Integer> columnsDelimitation) {
		for (int i = 1; i < columnsDelimitation.size() - 1; i++) {
			if (columnsDelimitation.get(i) == 1 && columnsDelimitation.get(i - 1) == 0
					&& columnsDelimitation.get(i + 1) == 0) {
				continue;
			} else if (columnsDelimitation.get(i) == 1 && columnsDelimitation.get(i - 1) == 0) {
				columnsDelimitation.set(i, 0);
				i++;
			} else if (columnsDelimitation.get(i) == 1 && columnsDelimitation.get(i + 1) == 0) {
				columnsDelimitation.set(i, 0);
				i++;
			}
		}
	}

	private static ArrayList<Integer> normalizeDelimitation(ArrayList<Integer> delimitation) {
		int thickeningOffset = delimitation.size() / 60;

		ArrayList<Integer> leftRightNormalization = (ArrayList<Integer>) delimitation.clone();
		ArrayList<Integer> rightLeftNormalization = (ArrayList<Integer>) delimitation.clone();

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

		delimitation = new ArrayList<Integer>();

		for (int i = 0; i < leftRightNormalization.size(); i++) {
			delimitation.add((rightLeftNormalization.get(i) == 0 || leftRightNormalization.get(i) == 0) ? 0 : 1);

		}

		for (int i = 0; i < thickeningOffset; i++) {
			thickenDelimitation(delimitation);
		}

		return delimitation;
	}

	private static ArrayList<Integer> getDelimitation(ArrayList<Double> blackness) {
		double averageBlackness = 0d;

		for (Double number : blackness) {
			averageBlackness += number;
		}

		averageBlackness /= blackness.size();

		double contrastOffset = (1 - averageBlackness) / 2;

		ArrayList<Integer> delimitation = new ArrayList<Integer>();

		for (Double number : blackness) {
			delimitation.add(number > (averageBlackness + contrastOffset) ? 1 : 0);
		}

		return normalizeDelimitation(delimitation);
	}

	private static void verticallyCorrectColumn(Image image, Column column) {
		boolean[][] pixels = SimpleOperationsImpl.getPartOfPixels(image.pixels, column.topLeftCorner,
				column.topRightCorner, column.bottomLeftCorner, column.bottomRightCorner);
		ArrayList<Double> horizontalBlackness = getHorizontalBlackness(pixels);

		ArrayList<Integer> delimitation = getDelimitation(horizontalBlackness);

		int upperEmptySpace = 0;
		int lowerEmptySpace = 0;
		for (Integer i : delimitation) {
			upperEmptySpace++;
			if (i == 0)
				break;
		}
		Collections.reverse(delimitation);
		for (Integer i : delimitation) {
			lowerEmptySpace++;
			if (i == 0)
				break;
		}
		column.topLeftCorner.y = column.topLeftCorner.y + upperEmptySpace;
		column.topRightCorner.y = column.topRightCorner.y + upperEmptySpace;
		column.bottomLeftCorner.y = column.bottomLeftCorner.y - lowerEmptySpace;
		column.bottomRightCorner.y = column.bottomRightCorner.y - lowerEmptySpace;
	}

	public static void computeLinesOfColumns(Image image) {

		for (Column column : image.columns) {
			boolean[][] pixels = SimpleOperationsImpl.getPartOfPixels(image.pixels, column.topLeftCorner,
					column.topRightCorner, column.bottomLeftCorner, column.bottomRightCorner);
			ArrayList<Double> horizontalBlackness = getHorizontalBlackness(pixels);
			ArrayList<Integer> delimitation = getDelimitation(horizontalBlackness);

			int lineStart = 0;
			int lastItem = 1;
			for (int i = 0; i < delimitation.size(); i++) {
				if (delimitation.get(i) != lastItem) {
					if (lastItem == 1) {
						lineStart = i;
					} else {
						column.lines.add(new Line(new Point(column.topLeftCorner.x, column.topLeftCorner.y + lineStart),
								new Point(column.topRightCorner.x, column.topLeftCorner.y + lineStart),
								new Point(column.topLeftCorner.x, column.topLeftCorner.y + i),
								new Point(column.topRightCorner.x, column.topLeftCorner.y + i)));
					}
				}
				lastItem = delimitation.get(i);
			}
		}
	}

	public static void computeColumns(Image image) {
		Point topLeftCorner;
		Point topRightCorner;
		Point bottomLeftCorner;
		Point bottomRightCorner;

		if (image.header == null) {
			topLeftCorner = new Point(0, 0);
			topRightCorner = new Point(image.pixels[0].length, 0);
		} else {
			topLeftCorner = image.header.bottomLeftCorner;
			topRightCorner = image.header.bottomRightCorner;
		}

		if (image.footnote == null) {
			bottomLeftCorner = new Point(0, image.pixels.length);
			bottomRightCorner = new Point(image.pixels[0].length, image.pixels.length);
		} else {
			bottomLeftCorner = image.footnote.topLeftCorner;
			bottomRightCorner = image.footnote.topRightCorner;
		}

		boolean[][] pixels = SimpleOperationsImpl.getPartOfPixels(image.pixels, topLeftCorner, topRightCorner,
				bottomLeftCorner, bottomRightCorner);
		ArrayList<Column> columns = new ArrayList<Column>();

		ArrayList<Double> columnsBlackness = ColumnsRecognitionImpl.getVerticalBlackness(pixels);

		ArrayList<Integer> columnsDelimitation = ColumnsRecognitionImpl.getDelimitation(columnsBlackness);

		int columnStart = 0;
		int lastItem = 1;
		for (int i = 0; i < columnsDelimitation.size(); i++) {
			if (columnsDelimitation.get(i) != lastItem) {
				if (lastItem == 1) {
					columnStart = i;
				} else {
					columns.add(new Column(new Point(columnStart, 0), new Point(i, 0),
							new Point(columnStart, image.pixels.length), new Point(i, image.pixels.length)));
				}
			}
			lastItem = columnsDelimitation.get(i);
		}

		for (Column column : columns) {
			verticallyCorrectColumn(image, column);
		}

		image.columns = columns;
	}
}