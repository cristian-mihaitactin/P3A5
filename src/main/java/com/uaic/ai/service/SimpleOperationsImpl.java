package com.uaic.ai.service;

import java.awt.Point;

import org.opencv.core.Mat;



public class SimpleOperationsImpl implements SimpleOperations {
	
	public static boolean[][] getPartOfPixels(boolean[][] pixels, Point topLeftCorner, Point topRightCorner,
			Point bottomLeftCorner, Point bottomRightCorner) {
		boolean[][] result = new boolean[bottomRightCorner.y - topRightCorner.y][topRightCorner.x - topLeftCorner.x];

		for (int i = topLeftCorner.y; i < bottomLeftCorner.y; i++) {
			for (int j = topLeftCorner.x; j < topRightCorner.x; j++) {
				result[i - topRightCorner.y][j - topLeftCorner.x] = pixels[i][j];
			}
		}

		return result;
	}
}
