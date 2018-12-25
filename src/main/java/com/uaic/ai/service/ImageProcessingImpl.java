package com.uaic.ai.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class ImageProcessingImpl implements ImageProcessing {
	BinaryMatrix binaryMatrix;
	SimpleOperations simpleOperations;
	ColumnsRecognition columnsRecognition;

	public ImageProcessingImpl() {
		this.binaryMatrix = new BinaryMatrixImpl();
		this.simpleOperations = new SimpleOperationsImpl();
		this.columnsRecognition = new ColumnsRecognitionImpl();
	}

	private void clearImage(String inPath, String outPath) {
		String[] command = { "magick", "convert", "-lat", "20x20-8%", inPath, outPath };
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void rotateImage(String inPath, String outPath, Integer degrees) {
		String[] command = { "magick", "convert", "-rotate", degrees.toString(), inPath, outPath };
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getScore(boolean pixels[][]) {
		return (int) columnsRecognition.getDelimitation(simpleOperations.getVerticalBlackness(pixels), 4).stream()
				.filter(f -> f == 0).count();
	}

	@Override
	public void correctImage(String inPath, String allignedImagePath, String outPath) {
		int upperLimit = 32;
		int lowerLimit = -32;
		int currentLimit = 0;
		clearImage(inPath, outPath);
		HashMap<Integer, Integer> scores = new HashMap<Integer, Integer>();
		int leftScore;
		int rightScore;

		while (true) {
			if (upperLimit - lowerLimit <= 1)
			{
				currentLimit = Integer.compare(scores.get(upperLimit), scores.get(lowerLimit)) < 0 ? upperLimit : lowerLimit;
				break;
			}
			int leftTest = (lowerLimit + currentLimit) / 2;
			int rightTest = (upperLimit + currentLimit) / 2;

			if (scores.containsKey(leftTest))
				leftScore = scores.get(leftTest);
			else {
				rotateImage(outPath, allignedImagePath, leftTest);
				binaryMatrix.createMatrix(allignedImagePath);
				leftScore = getScore(binaryMatrix.getMatrix());
				scores.put(leftTest, leftScore);
			}

			if (scores.containsKey(rightTest))
				rightScore = scores.get(rightTest);
			else {
				rotateImage(outPath, allignedImagePath, rightTest);
				binaryMatrix.createMatrix(allignedImagePath);
				rightScore = getScore(binaryMatrix.getMatrix());
				scores.put(rightTest, rightScore);
			}
			
			if (leftScore < rightScore) {
				upperLimit = currentLimit;
				currentLimit = leftTest;
			} else {
				lowerLimit = currentLimit;
				currentLimit = rightTest;
			}

		}

		rotateImage(outPath, outPath, currentLimit);
		rotateImage(inPath, allignedImagePath, currentLimit);

	}

}
