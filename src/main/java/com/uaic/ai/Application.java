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
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        nu.pattern.OpenCV.loadShared();

        //SpringApplication.run(Application.class);




//        TesseractTextRecognitionImpl service = new TesseractTextRecognitionImpl();
//        System.out.println(service.getImgText("D:\\Andy\\an3\\InteligentaArtificiala_IA\\segmofwrittings\\141-text.jpg"));




        /*
        //give ABSOLUTE path to resources. Relative won't work
        Mat image = Imgcodecs.imread("\\...\\sample.jpg");

        Pixel[][] pixels = ColumnsRecognitionImpl.getPixelsFromImage(image);

        ArrayList<Double> columnsBlackness = ColumnsRecognitionImpl.getColumnsBlackness(pixels);

        ArrayList<Integer> columnsDelimitation = ColumnsRecognitionImpl.getColumnsDelimitation(columnsBlackness);

        for (Integer number : columnsDelimitation) {
            System.out.print(number);
        }

        System.out.println();
        boolean inColumn = false;
        for (int i = 1; i < columnsDelimitation.size(); i++) {
            if (!columnsDelimitation.get(i).equals(columnsDelimitation.get(i - 1))) {
                if (!inColumn) {
                    System.out.print("Coloana incepe de la " + i);
                } else {
                    System.out.println(" si se sfarseste la " + i);
                }
                inColumn = !inColumn;
            }
        }


        */

        TextRecognitionImpl tr = new TextRecognitionImpl();
        //tr.analyse1();
        Mat img1 = Imgcodecs.imread("D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5.png");
        List<Rect> letterBBoxes1=tr.detectLetters(img1);

        for(int i=0; i< letterBBoxes1.size(); i++)
            Imgproc.rectangle(img1,letterBBoxes1.get(i).br(), letterBBoxes1.get(i).tl(),new Scalar(0,255,0),3,8,0);
        Imgcodecs.imwrite("D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5-XX.png", img1);



    }


}
