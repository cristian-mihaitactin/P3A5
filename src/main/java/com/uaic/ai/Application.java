package com.uaic.ai;

import com.uaic.ai.model.Image;
import com.uaic.ai.model.Pixel;
import com.uaic.ai.service.BinaryMatrix;
import com.uaic.ai.service.BinaryMatrixImpl;
import com.uaic.ai.service.ColumnsRecognitionImpl;
import com.uaic.ai.service.TextRecognition;
import com.uaic.ai.service.TextRecognitionImpl;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		nu.pattern.OpenCV.loadShared();

		SpringApplication.run(Application.class);
		
		//The outPath is the output of the text cleaning command
		String inPath = "C:\\Users\\gabri\\Desktop\\Git 2\\P3A5\\src\\main\\resources\\anotherSample.png";
		String outPath = "C:\\Users\\gabri\\Desktop\\Git 2\\P3A5\\src\\main\\resources\\output.jpg";
		String[] command = { "magick", "convert", "-lat", "20x20-10%", inPath, outPath };
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Image image = new Image();
		
		//this needs to be changed
		//we need a computeBinaryMatrix method that takes the Image and a Mat and updates the Image binary matrix to the real one 
		BinaryMatrixImpl matrix = new BinaryMatrixImpl();
		matrix.createMatrix(outPath);
		image.pixels = matrix.getMatrix();
		
		ColumnsRecognitionImpl.computeColumns(image);
		ColumnsRecognitionImpl.computeLinesOfColumns(image);
		
		System.out.println(image);

		// Image image = new Image();
		// computeBinaryMatrix(image,mat);
		// computeFootnote(image);
		// computeHeader(image);
		// computeColumns(image);
		// return image

		
		
		// TextRecognitionImpl tr = new TextRecognitionImpl();
		// //tr.analyse1();
		// Mat img1 =
		// Imgcodecs.imread("D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5.png");
		// List<Rect> letterBBoxes1=tr.detectLetters(img1);
		//
		// for(int i=0; i< letterBBoxes1.size(); i++)
		// Imgproc.rectangle(img1,letterBBoxes1.get(i).br(),
		// letterBBoxes1.get(i).tl(),new Scalar(0,255,0),3,8,0);
		// Imgcodecs.imwrite("D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5-XX.png",
		// img1);
		//

	}

}
