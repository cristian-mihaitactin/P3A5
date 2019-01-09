package com.uaic.ai.service;

import java.awt.Point;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import com.uaic.ai.model.Column;
import com.uaic.ai.model.Line;

@Service
public class SimpleOperationsImpl implements SimpleOperations {

	@Override
	public boolean[][] getPartOfPixels(boolean[][] pixels, Point topLeftCorner, Point topRightCorner,
			Point bottomLeftCorner, Point bottomRightCorner) {
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
	
	private double calculateCentroid(ArrayList<Line> cluster) {
		double centroid = 0;
		
		for(Line line : cluster) {
			centroid += line.bottomLeftCorner.x;
		}
		
		return centroid/ cluster.size();
	}
	
	@Override
	public ArrayList<ArrayList<Line>> kMeansClustering(Column column) {
		ArrayList<Line> firstCluster;
		ArrayList<Line> secondCluster;
		ArrayList<Line> thirdCluster;
		int columnWidth = column.bottomRightCorner.x - column.bottomLeftCorner.x;

		double firstCentroid = column.bottomLeftCorner.x + 0;
		double secondCentroid = column.bottomLeftCorner.x + columnWidth / 20;
		double thirdCentroid = column.bottomLeftCorner.x + columnWidth / 3;

		do {
			firstCluster = new ArrayList<Line>();
			secondCluster = new ArrayList<Line>();
			thirdCluster = new ArrayList<Line>();
			for (Line line : column.lines) {
				double distance = line.bottomLeftCorner.x;
				if (Math.abs(distance - firstCentroid) < Math.abs(distance - secondCentroid)
						&& Math.abs(distance - firstCentroid) < Math.abs(distance - thirdCentroid)) {
					firstCluster.add(line);
				} else if (Math.abs(distance - secondCentroid) < Math.abs(distance - thirdCentroid)) {
					secondCluster.add(line);
				} else
					thirdCluster.add(line);
			}
			
			double newFirstCentroid = calculateCentroid(firstCluster);
			double newSecondCentroid = calculateCentroid(secondCluster);
			double newThirdCentroid = calculateCentroid(thirdCluster);
			
			newFirstCentroid = Double.isNaN(newFirstCentroid) ? firstCentroid : newFirstCentroid;
			newSecondCentroid = Double.isNaN(newSecondCentroid) ? secondCentroid : newSecondCentroid;
			newThirdCentroid = Double.isNaN(newThirdCentroid) ? thirdCentroid : newThirdCentroid;
			if(newFirstCentroid == firstCentroid && newSecondCentroid == secondCentroid && newThirdCentroid == thirdCentroid)
				break;
			
			firstCentroid = newFirstCentroid;
			secondCentroid = newSecondCentroid;
			thirdCentroid = newThirdCentroid;
		} while (true);
		ArrayList<ArrayList<Line>> result = new ArrayList<ArrayList<Line>>();
		result.add(firstCluster);
		result.add(secondCluster);
		result.add(thirdCluster);
		
		return result;
		
	}

}
