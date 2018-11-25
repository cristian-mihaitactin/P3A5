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

    }


}
