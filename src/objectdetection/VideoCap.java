/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objectdetection;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Daniel
 */
public class VideoCap {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    VideoCap(){
        cap = new VideoCapture();
        cap.open(0);
    } 
 
    public BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);
        Imgproc.cvtColor(mat2Img.mat, mat2Img.mat, Imgproc.COLOR_BGR2RGB);
        BufferedImage img = mat2Img.getImage(mat2Img.mat);
        BufferedImage hasil = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = new Color(img.getRGB(i, j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                
                int grayscale = (r + g + b) /3;
                hasil.setRGB(i, j, new Color(grayscale, grayscale, grayscale).getRGB());
            }
        }
        return hasil;
    }
    
    public void close() {
        cap.release();
    }
}
