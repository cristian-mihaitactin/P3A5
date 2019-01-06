package com.uaic.ai;

import com.uaic.ai.model.Image;
import com.uaic.ai.service.ColumnsRecognitionImpl;
import com.uaic.ai.service.ImageProcessing;
import com.uaic.ai.service.ImageProcessingImpl;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		nu.pattern.OpenCV.loadShared();

		SpringApplication.run(Application.class);

		
/*
		//The outPath is the output of the text cleaning command
		ColumnsRecognitionImpl columnsRecognitionImpl = new ColumnsRecognitionImpl();
		
		//Path of the original image (from the client)
		String inPath = "C:\\Users\\gabri\\Desktop\\Git\\P3A5\\src\\main\\resources\\tilted3.jpg";
		
		//Path of the alligned image, is not cleared, this file will be returned to the front-end
		String allignedImagePath = "C:\\Users\\gabri\\Desktop\\Git\\P3A5\\src\\main\\resources\\clientImage.jpg";
		
		//Path of the output, alligned and cleaned, we will work on this file
		String outPath = "C:\\Users\\gabri\\Desktop\\Git\\P3A5\\src\\main\\resources\\output.jpg";
		ImageProcessing imageProcessing = new ImageProcessingImpl();

		imageProcessing.correctImage(inPath,allignedImagePath,outPath);
		Image image = new Image();
		
		//this needs to be changed
		//we need a computeBinaryMatrix method that takes the Image and a Mat and updates the Image binary matrix to the real one 
		BinaryMatrixImpl matrix = new BinaryMatrixImpl();
		matrix.createMatrix(outPath);
		image.pixels = matrix.getMatrix();

		columnsRecognitionImpl.computeColumns(image);
		columnsRecognitionImpl.computeLinesOfColumns(image);
		
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
*/
		

	}

}
