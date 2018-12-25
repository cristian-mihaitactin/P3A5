package com.uaic.ai.service;

import java.awt.Point;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.springframework.stereotype.Service;


@Service
public class SimpleOperationsImpl implements SimpleOperations {
	
	@Override
	public boolean[][] getPartOfPixels(boolean[][] pixels, Point topLeftCorner, Point topRightCorner, Point bottomLeftCorner, Point bottomRightCorner) {
		boolean[][] result = new boolean[bottomRightCorner.y - topRightCorner.y][topRightCorner.x - topLeftCorner.x];

		for (int i = topLeftCorner.y; i < bottomLeftCorner.y; i++) {
			for (int j = topLeftCorner.x; j < topRightCorner.x; j++) {
				result[i - topRightCorner.y][j - topLeftCorner.x] = pixels[i][j];
			}
		}

		return result;
	}
	
	@Override
	public ArrayList<Double> getVerticalBlackness(boolean[][] pixels) {
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

	@Override
	public ArrayList<Double> getHorizontalBlackness(boolean[][] pixels) {
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
}
