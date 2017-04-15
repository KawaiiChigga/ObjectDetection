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
import java.util.ArrayList;
import java.util.Random;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Daniel
 */
public class VideoCap {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    Random rand = new Random();
    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();
    int warna = 0;
    ArrayList<ArrayList> daftar;

    VideoCap() {
        cap = new VideoCapture();
        cap.open(0);
    } 
 
    public BufferedImage getOneFrameOld() throws IOException {
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

    public BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);
        Imgproc.cvtColor(mat2Img.mat, mat2Img.mat, Imgproc.COLOR_BGR2RGB);
        BufferedImage img = mat2Img.getImage(mat2Img.mat);
        BufferedImage hasil = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        daftar = new ArrayList<>();

        process(img, 0, img.getWidth(), 0, img.getHeight());
         Integer warna;
        for (int i = 0; i < daftar.size(); i++) {
            warna = (Integer) daftar.get(i).get(4);
            int start1 = (int) daftar.get(i).get(0);
            int start2 = (int) daftar.get(i).get(2);
            int end1 = (int) daftar.get(i).get(1);
            int end2 = (int) daftar.get(i).get(3);
//            System.out.println(start1+" "+end1+" "+start2+" "+end2);
            for (int j = start1; j <end1; j++) {
                for (int k = start2; k <end2; k++) {
                    hasil.setRGB(j, k, new Color(warna, warna, warna).getRGB());
                }
            }
        }
        
        return hasil;
    }

    public void process(BufferedImage img, int startWidth, int endWidth, int startHeight, int endHeight) {

        if (check(img, startWidth, endWidth, startHeight, endHeight)
                || Math.abs(startWidth - endWidth) <= 2 || Math.abs(startHeight - endHeight) <= 2) {
            return;
        } else {
            process(img, startWidth, Math.abs(endWidth / 2), startHeight, Math.abs(endHeight / 2));//kiri atas
            process(img, Math.abs(endWidth / 2), endWidth, startHeight, Math.abs(endHeight / 2));//kanan atas
            process(img, startWidth, Math.abs(endWidth / 2), Math.abs(endHeight / 2), endHeight);//kiri bawah
            process(img, Math.abs(endWidth / 2), Math.abs(endWidth - 1), Math.abs(endHeight / 2), Math.abs(endHeight - 1));//kanan bawah
        }
    }

    public boolean check(BufferedImage img, int startWidth, int endWidth, int startHeight, int endHeight) {
        boolean same = true;
        Color c;
        int r;
        int g;
        int b;
        int threshold = 2;
        int grayscale;
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = startWidth + 1; i < endWidth - 1; i++) {
            for (int j = startHeight + 1; j < endHeight - 1; j++) {
                c = new Color(img.getRGB(i, j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                grayscale = (r + g + b) / 3;

                c = new Color(img.getRGB(i - 1, j));
                int kiri = (c.getRed() + c.getGreen() + c.getBlue()) / 3;

                c = new Color(img.getRGB(i + 1, j));
                int kanan = (c.getRed() + c.getGreen() + c.getBlue()) / 3;

                c = new Color(img.getRGB(i, j - 1));
                int atas = (c.getRed() + c.getGreen() + c.getBlue()) / 3;

                c = new Color(img.getRGB(i, j + 1));
                int bawah = (c.getRed() + c.getGreen() + c.getBlue()) / 3;

                if (Math.abs(grayscale - kiri) > threshold || Math.abs(grayscale - kanan) > threshold
                        || Math.abs(grayscale - atas) > threshold || Math.abs(grayscale - bawah) > threshold) {
                    same = false;
                    break;
                }
            }
            if (!same) {
                break;
            }
        }
        if (same) {
            int warna;
            temp.add(startWidth);
            temp.add(endWidth);
            temp.add(startHeight);
            temp.add(endHeight);
            warna = rand.nextInt(255) + 0;
            temp.add(warna);
            daftar.add(temp);
        }
        return same;
    }

    public void close() {
        cap.release();
    }
}
