package com.uaic.ai;

import com.uaic.ai.model.Pixel;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        nu.pattern.OpenCV.loadShared();

        SpringApplication.run(Application.class);




//        TesseractTextRecognitionImpl service = new TesseractTextRecognitionImpl();
//        System.out.println(service.getImgText("D:\\Andy\\an3\\InteligentaArtificiala_IA\\segmofwrittings\\141-text.jpg"));




        /*
        //give ABSOLUTE path to resources. Relative won't work
        Mat mat = Imgcodecs.imread("C:\\Users\\gabri\\Desktop\\Git 2\\P3A5\\src\\main\\resources\\sample.jpg");

        // Image image = new Image();
        //computeBinaryMatrix(image,mat);
        //computeFootnote(image);
        //computeHeader(image);
        //computeColumns(image);
        //return image


        */

//        TextRecognitionImpl tr = new TextRecognitionImpl();
//        //tr.analyse1();
//        Mat img1 = Imgcodecs.imread("D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5.png");
//        List<Rect> letterBBoxes1=tr.detectLetters(img1);
//
//        for(int i=0; i< letterBBoxes1.size(); i++)
//            Imgproc.rectangle(img1,letterBBoxes1.get(i).br(), letterBBoxes1.get(i).tl(),new Scalar(0,255,0),3,8,0);
//        Imgcodecs.imwrite("D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5-XX.png", img1);
//


    }


}
