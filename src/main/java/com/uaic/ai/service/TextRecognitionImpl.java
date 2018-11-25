package com.uaic.ai.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class TextRecognitionImpl implements TextRecognition{

    /**
     * BAD
     */
    //BAD
    //    public Vector<Rect> detectLetters(Mat img) {
//        Vector<Rect> boundRect;
//        Mat img_gray = null;
//        Mat img_sobel = null;
//        Mat img_threshold = null;
//        Mat element = null;
//
//        Imgproc.cvtColor(img, img_gray, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.Sobel(img_gray, img_sobel, CvType.CV_8U, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
//        Imgproc.threshold(img_sobel, img_threshold, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
//        element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17,3));
//        Imgproc.morphologyEx(img_threshold, img_threshold, 3, element);     //Does the trick
//        ArrayList<MatOfPoint> contours = null;
//        Imgproc.findContours(img_threshold, contours, new Mat(),1,1);
//        List<MatOfPoint> contours_poly = new ArrayList<MatOfPoint>(contours.size());
//
//        for(int i=0; i<contours.size(); i++) {
//            if(contours.get(i).size().width > 100) {
//                Imgproc.approxPolyDP(new Mat(contours.get(i).get), contours_poly.get(i),3,true);
//            }
//        }
//
//    }

    /**
     * BAD
      */
    public void analyse1(){

        Mat Main = Imgcodecs.imread("D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5.png");
        Mat rgb = new Mat();

        Imgproc.pyrDown(Main, rgb);     // Blurs an image and downsamples it.

        Mat small = new Mat();

        Imgproc.cvtColor(rgb, small, Imgproc.COLOR_RGB2GRAY);   //Converts an image from one color space to another.

        Mat grad = new Mat();

        Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));

        Imgproc.morphologyEx(small, grad, Imgproc.MORPH_GRADIENT , morphKernel);

        Mat bw = new Mat();

        Imgproc.threshold(grad, bw, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        Mat connected = new Mat();

        morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,1));

        Imgproc.morphologyEx(bw, connected, Imgproc.MORPH_CLOSE  , morphKernel);


        Mat mask = Mat.zeros(bw.size(), CvType.CV_8UC1);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat hierarchy = new Mat();

        Imgproc.findContours(connected, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        for(int idx = 0; idx < contours.size(); idx++)
        {
            Rect rect = Imgproc.boundingRect(contours.get(idx));

            Mat maskROI = new Mat(mask, rect);

            maskROI.setTo(new Scalar(0, 0, 0));

            Imgproc.drawContours(mask, contours, idx, new Scalar(255, 255, 255), Core.FILLED);

            double r = (double)Core.countNonZero(maskROI)/(rect.width*rect.height);

            if (r > .45 && (rect.height > 8 && rect.width > 8))
            {
                Imgproc.rectangle(rgb, rect.br() , new Point( rect.br().x-rect.width ,rect.br().y-rect.height),  new Scalar(0, 255, 0));
            }

            String outputFile = "D:\\Andy\\an3\\InteligentaArtificiala_IA\\images\\Screenshot_5_result.png";
            Imgcodecs.imwrite(outputFile,rgb);
        }
    }

    /**
     * GOOD
     */
    public List<Rect> detectLetters(Mat img){
        List<Rect> boundRect=new ArrayList<>();

        Mat img_gray =new Mat(), img_sobel=new Mat(), img_threshold=new Mat(), element=new Mat();
        Imgproc.cvtColor(img, img_gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Sobel(img_gray, img_sobel, CvType.CV_8U, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
        //at src, Mat dst, double thresh, double maxval, int type

        System.out.println("xxxxx");
        Imgproc.threshold(img_sobel, img_threshold, 0, 255, 8);
        element=Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,1));
        System.out.println("zzzz");

        Imgproc.morphologyEx(img_threshold, img_threshold, Imgproc.MORPH_CLOSE, element);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(img_threshold, contours,hierarchy, 0, 1);

        List<MatOfPoint> contours_poly = new ArrayList<MatOfPoint>(contours.size());

        System.out.println(contours.size());
        for( int i = 0; i < contours.size(); i++ ){

            MatOfPoint2f  mMOP2f1=new MatOfPoint2f();
            MatOfPoint2f  mMOP2f2=new MatOfPoint2f();

            contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
            Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 2, true);
            mMOP2f2.convertTo(contours.get(i), CvType.CV_32S);


            Rect appRect = Imgproc.boundingRect(contours.get(i));
            if (appRect.width>appRect.height) {
                boundRect.add(appRect);
            }
        }

        return boundRect;
    }


}
