package com.uaic.ai;

import com.uaic.ai.model.Pixel;
import com.uaic.ai.service.ColumnsRecognitionImpl;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        nu.pattern.OpenCV.loadShared();

        //SpringApplication.run(Application.class);




//        TesseractTextRecognitionImpl service = new TesseractTextRecognitionImpl();
//        System.out.println(service.getImgText("D:\\Andy\\an3\\InteligentaArtificiala_IA\\segmofwrittings\\141-text.jpg"));


        //give ABSOLUTE path to resources. Relative won't work
        Mat mat = Imgcodecs.imread("C:\\Users\\gabri\\Desktop\\Git 2\\P3A5\\src\\main\\resources\\sample.jpg");

        // Image image = new Image();
        //computeBinaryMatrix(image,mat);
        //computeFootnote(image);
        //computeHeader(image);
        //computeColumns(image);
        //return image

    }


}
