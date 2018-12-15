import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class ConvertImageService {

	public static Image processImage(byte[] bytes)
	{
		return processImageMatrix(Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_UNCHANGED));
	}
	public static Image processImage(String path)
	{
		return processImageMatrix(Imgcodecs.imread(path));
	}
	
	private static Image processImageMatrix(Mat pixelMatrix)
	{
		boolean[][] matrix;
		
		byte[] data = new byte[3];
        matrix = new boolean[pixelMatrix.rows()][pixelMatrix.cols()];
        int darkPixel = 0, brightPixel = 0, brightness = 0, pixel, darkPixelCount = 0;

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
        

        
        
        for (int i = 0; i < pixelMatrix.rows(); i++)
            for (int j = 0; j < pixelMatrix.cols(); j++) {
                pixelMatrix.get(i, j, data);
                pixel = (data[0] & 0xFF + data[1] & 0xFF + data[2] & 0xFF) / 3;
                matrix[i][j] = ((pixel - darkPixel) * (pixel - darkPixel) < (brightPixel - pixel) * (brightPixel - pixel));
            }
        Image img=new Image();
        img.pixels=matrix;
        return img;
	}
	
}
