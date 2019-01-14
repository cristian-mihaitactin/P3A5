package com.uaic.ai.service;

import com.uaic.ai.model.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.validation.constraints.NotNull;

@Service
public class ImageServiceImpl implements ImageService {
	
    
	SimpleOperations simpleOperations;
	ColumnsRecognition columnsRecognition;

	public ImageServiceImpl() {
		this.simpleOperations = new SimpleOperationsImpl();
		this.columnsRecognition = new ColumnsRecognitionImpl();
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
				leftScore = getScore(processImage(allignedImagePath).pixels);
				scores.put(leftTest, leftScore);
			}

			if (scores.containsKey(rightTest))
				rightScore = scores.get(rightTest);
			else {
				rotateImage(outPath, allignedImagePath, rightTest);
				rightScore = getScore(processImage(allignedImagePath).pixels);
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

    @Override
    public Image processImage(byte[] bytes) {
        Image image = new Image();
        image.setPixels(processImageMatrix(Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_UNCHANGED)));
        return image;
        //return processImageMatrix(Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_UNCHANGED));
    }

    @Override
    public Image processImage(String path) {
        Image image = new Image();
        image.setPixels(processImageMatrix(Imgcodecs.imread(path)));
        return image;
        //return processImageMatrix(Imgcodecs.imread(path));
    }

    /**
     * Creates the binary matrix for a given Image of type Mat
     *
     * @param pixelMatrix that need to be converted to binary matrix.  1 = black  0 = white
     * @return boolean matrix for Mat parameter
     */
    private boolean[][] processImageMatrix(Mat pixelMatrix) {
        boolean[][] matrix;

        byte[] data = new byte[3];
        matrix = new boolean[pixelMatrix.rows()][pixelMatrix.cols()];
        int darkPixel = 0;
        int brightPixel = 0;
        int brightness = 0;
        int pixel;
        int darkPixelCount = 0;

        //GET BRIGHTEST AND DARKEST PIXEL
        for (int i = 0; i < pixelMatrix.rows(); i++) {
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                brightness = brightness + (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
            }
        }

        //System.out.println(brightness + " " + pixelMatrix.rows() * pixelMatrix.cols());
        brightness = (brightness / (pixelMatrix.rows() * pixelMatrix.cols()));

        //BUILD THE MATRIX
        for (int i = 0; i < pixelMatrix.rows(); i++)
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                pixel = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;

                if (pixel < brightness) {
                    darkPixel += pixel;
                    darkPixelCount++;
                } else {
                    brightPixel += pixel;
                }
            }


        if (darkPixelCount > 0)
            darkPixel = darkPixel / darkPixelCount;
        brightPixel = brightPixel / (pixelMatrix.rows() * pixelMatrix.cols() - darkPixelCount);


        for (int i = 0; i < pixelMatrix.rows(); i++) {
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                pixel = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
                matrix[i][j] = ((pixel - darkPixel) * (pixel - darkPixel) < (brightPixel - pixel) * (brightPixel - pixel));
            }
        }
        
        return matrix;
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
		ArrayList<Integer> vertical = columnsRecognition.getDelimitation(simpleOperations.getVerticalBlackness(pixels), 4);
		ArrayList<Integer> horizontal = columnsRecognition.getDelimitation(simpleOperations.getHorizontalBlackness(pixels), 4);
		int result = 0;
		
		for(Integer i : vertical)
			if(i == 0)
				result++;
		
		for(Integer i : horizontal)
			if(i == 0)
				result++;
	
		return result;
	}
}
