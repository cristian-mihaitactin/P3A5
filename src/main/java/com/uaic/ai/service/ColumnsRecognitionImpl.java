package com.uaic.ai.service;

import com.uaic.ai.model.Column;
import com.uaic.ai.model.Image;
import com.uaic.ai.model.Line;
import com.uaic.ai.model.Paragraph;
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
	SimpleOperations simpleOperations;

	public ColumnsRecognitionImpl() {
		this.simpleOperations = new SimpleOperationsImpl();
	}

	private boolean isGoodSelection(int i, ArrayList<Integer> selection) {
		if (i > 1 && (selection.get(i - 2).equals(selection.get(i)) && selection.get(i - 1).equals(selection.get(i))))
			return true;
		if (i < selection.size() - 2
				&& (selection.get(i + 2).equals(selection.get(i)) && selection.get(i + 1).equals(selection.get(i))))
			return true;

		return false;
	}

	private void thickenDelimitation(ArrayList<Integer> columnsDelimitation) {
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

	private ArrayList<Integer> normalizeDelimitation(ArrayList<Integer> delimitation) {
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

	private void filterColumns(Image image) {
		for (int i = image.columns.size() - 1; i >= 0; i--) {
			float width = image.columns.get(i).topRightCorner.x - image.columns.get(i).topLeftCorner.x;
			if (width * 10 < image.pixels[0].length) {
				image.columns.remove(i);
			}
		}

	}

	@Override
	public ArrayList<Integer> getDelimitation(ArrayList<Double> blackness, double sensitivity) {
		double averageBlackness = 0d;

		for (Double number : blackness) {
			averageBlackness += number;
		}

		averageBlackness /= blackness.size();

		double contrastOffset = (1 - averageBlackness) - (1 - averageBlackness) / sensitivity;

		ArrayList<Integer> delimitation = new ArrayList<Integer>();

		for (Double number : blackness) {
			delimitation.add(number > (averageBlackness + contrastOffset) ? 1 : 0);
		}

		return normalizeDelimitation(delimitation);
	}

	private void verticallyCorrectColumn(Image image, Column column) {
		boolean[][] pixels = simpleOperations.getPartOfPixels(image.pixels, column.topLeftCorner, column.topRightCorner,
				column.bottomLeftCorner, column.bottomRightCorner);
		ArrayList<Double> horizontalBlackness = simpleOperations.getHorizontalBlackness(pixels);

		ArrayList<Integer> delimitation = getDelimitation(horizontalBlackness, 2);

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

	private void horizontallyCorrectLine(Image image, Line line) {
		boolean[][] pixels = simpleOperations.getPartOfPixels(image.pixels, line.topLeftCorner, line.topRightCorner,
				line.bottomLeftCorner, line.bottomRightCorner);
		ArrayList<Double> verticalBlackness = simpleOperations.getVerticalBlackness(pixels);

		ArrayList<Integer> delimitation = getDelimitation(verticalBlackness, 2);

		int startEmptySpace = 0;
		int endEmptySpace = 0;
		for (Integer i : delimitation) {
			startEmptySpace++;
			if (i == 0)
				break;
		}
		Collections.reverse(delimitation);
		for (Integer i : delimitation) {
			endEmptySpace++;
			if (i == 0)
				break;
		}
		line.topLeftCorner.x = line.topLeftCorner.x + startEmptySpace;
		line.topRightCorner.x = line.topRightCorner.x - endEmptySpace;
		line.bottomLeftCorner.x = line.bottomLeftCorner.x + startEmptySpace;
		line.bottomRightCorner.x = line.bottomRightCorner.x - endEmptySpace;
	}

	@Override
	public void computeColumns(Image image) {
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

		boolean[][] pixels = simpleOperations.getPartOfPixels(image.pixels, topLeftCorner, topRightCorner,
				bottomLeftCorner, bottomRightCorner);
		ArrayList<Column> columns = new ArrayList<Column>();

		ArrayList<Double> columnsBlackness = simpleOperations.getVerticalBlackness(pixels);

		ArrayList<Integer> columnsDelimitation = getDelimitation(columnsBlackness, 2);

		int columnStart = 0;
		int lastItem = 1;
		for (int i = 0; i < columnsDelimitation.size(); i++) {
			if (columnsDelimitation.get(i) != lastItem || i == columnsDelimitation.size() - 1) {
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

		filterColumns(image);
	}

	@Override
	public void computeLinesOfColumns(Image image) {

		for (Column column : image.columns) {
			boolean[][] pixels = simpleOperations.getPartOfPixels(image.pixels, column.topLeftCorner,
					column.topRightCorner, column.bottomLeftCorner, column.bottomRightCorner);
			ArrayList<Double> horizontalBlackness = simpleOperations.getHorizontalBlackness(pixels);
			ArrayList<Integer> delimitation = getDelimitation(horizontalBlackness, 2);

			int lineStart = 0;
			int lastItem = 1;
			for (int i = 0; i < delimitation.size(); i++) {
				if (delimitation.get(i) != lastItem || i == delimitation.size() - 1) {
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

		for (Column column : image.columns) {
			for (Line line : column.lines) {
				horizontallyCorrectLine(image, line);
			}
		}
	}

	private int getMinStartingLine(ArrayList<Line> lines) {
		int min = 10000;
		for (Line line : lines) {
			if (line.bottomLeftCorner.x < min) {
				min = line.bottomLeftCorner.x;
			}
		}
		return min;
	}

	private int getMaxEndingLine(ArrayList<Line> lines) {
		int max = 0;
		for (Line line : lines) {
			if (line.bottomLeftCorner.x > max) {
				max = line.bottomRightCorner.x;
			}
		}
		return max;
	}
	
	private void createParagraph(Image image,ArrayList<Line> lines) {
		int endingLine = getMaxEndingLine(lines);
		int startingLine = getMinStartingLine(lines);
		if(lines.isEmpty())
			return;
		image.paragraphs.add(new Paragraph(new Point(startingLine, lines.get(0).topLeftCorner.y),
				new Point(endingLine, lines.get(0).topLeftCorner.y),
				new Point(startingLine, lines.get(lines.size() - 1).bottomRightCorner.y),
				new Point(endingLine, lines.get(lines.size() - 1).bottomRightCorner.y)));
	}

	@Override
	public void computeParagraphs(Image image) {
		image.paragraphs = new ArrayList<Paragraph>();
		
		for (Column column : image.columns) {
			ArrayList<ArrayList<Line>> clusters = simpleOperations.kMeansClustering(column);

			ArrayList<Line> lastParagraph = new ArrayList<Line>();
			for (Line line : column.lines) {
				if (lastParagraph.isEmpty() && !clusters.get(2).contains(line)) {
					lastParagraph.add(line);
				} else if (clusters.get(1).contains(line)) {
					createParagraph(image, lastParagraph);
					
					lastParagraph = new ArrayList<Line>();
					lastParagraph.add(line);
				} else if(clusters.get(2).contains(line)) {
					createParagraph(image, lastParagraph);
					
					lastParagraph = new ArrayList<Line>();
				} else if(column.lines.indexOf(line) == column.lines.size() - 1) {
					lastParagraph.add(line);
					
					createParagraph(image, lastParagraph);
				} else if(clusters.get(0).contains(line)) {
					lastParagraph.add(line);
				}
			}
		}
	}

}