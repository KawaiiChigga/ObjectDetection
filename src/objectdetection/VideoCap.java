/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objectdetection;

import com.sun.imageio.plugins.common.I18N;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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
        cap.open(1);
    } 
 
    public BufferedImage getOneFrame() throws IOException {
        cap.read(mat2Img.mat);
        Imgproc.cvtColor(mat2Img.mat, mat2Img.mat, Imgproc.COLOR_BGR2RGB);
        BufferedImage img = mat2Img.getImage(mat2Img.mat);
//        BufferedImage img = ImageIO.read(new File("src/img/sample.jpg"));
        BufferedImage hasil = new BufferedImage(img.getWidth()+1, img.getHeight()+1, img.getType());
        int kernel [] = new int [] {
            1, 2, 1,
            2, 4, 2,
            1, 2, 1
        };
        for (int i = 0; i <= img.getWidth(); i++) {
            for (int j = 0; j <= img.getHeight(); j++) {
                if (i == 0 || i == img.getWidth() || j == 0 || j == img.getHeight()) {
                    hasil.setRGB(i, j, Color.RED.getRGB());
                } else {
//                    int r, g, b;
//                    for (int i = 0; i < kernel.length;)
//                    Color c = new Color(img.getRGB(i, j));
//                    int r = c.getRed();
//                    int g = c.getGreen();
//                    int b = c.getBlue();

//                    int grayscale = (r + g + b) /3;
                    hasil.setRGB(i, j, img.getRGB(i-1, j-1));
                }
            }
        }
        for (int i = 1; i < img.getWidth(); i++) {
            for (int j = 1; j < img.getHeight(); j++) {
                Color[] pixel = new Color[9];
                pixel[0] = new Color(hasil.getRGB(i-1, j-1));
                pixel[1] = new Color(hasil.getRGB(i, j-1));
                pixel[2] = new Color(hasil.getRGB(i+1, j-1));
                pixel[3] = new Color(hasil.getRGB(i-1, j));
                pixel[4] = new Color(hasil.getRGB(i, j));
                pixel[5] = new Color(hasil.getRGB(i+1, j));
                pixel[6] = new Color(hasil.getRGB(i-1, j+1));
                pixel[7] = new Color(hasil.getRGB(i, j+1));
                pixel[8] = new Color(hasil.getRGB(i+1, j+1));
                
                int r = 0, g = 0, b = 0;
                for (int k = 0; k < kernel.length; k++) {
                    r += pixel[k].getRed()*kernel[k];
                    g += pixel[k].getGreen()*kernel[k];
                    b += pixel[k].getBlue()*kernel[k];
                }
                r = r/16;
                g = g/16;
                b = b/16;
                hasil.setRGB(i, j, new Color(r, g, b).getRGB());
            }
        }
        
        return hasil;
    }
    
    public void close() {
        cap.release();
    }
}
