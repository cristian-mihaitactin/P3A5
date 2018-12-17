package com.uaic.ai.service;

import com.uaic.ai.model.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class ImageServiceImpl implements ImageService {

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

//      Image img = new Image();
//      img.pixels = matrix;
        return matrix;
    }

}
