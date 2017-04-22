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
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    class Point {
        int x;
        int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    Random rand = new Random();
    int id = 0;
    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();
    int warna = 0;
    int tres = 0;
    
    ArrayList<ArrayList> daftar;
    ArrayList<ArrayList> akhir;

    int checked[][];
    Vector<Point> pointlist;
    
    VideoCap() {
//        cap = new VideoCapture();
//        cap.open(0);
    }

    public BufferedImage getOneFrame(int tres){
        this.tres = tres;
//        cap.read(mat2Img.mat);
        id = 0;
//        Imgproc.cvtColor(mat2Img.mat, mat2Img.mat, Imgproc.COLOR_BGR2RGB);
//        BufferedImage img = mat2Img.getImage(mat2Img.mat);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src/img/sample.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(VideoCap.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage hasil;
        int mode = 0;
        if (mode == 0) {
            hasil = split(img);
            System.out.println(daftar.size());
        } else {
            hasil = colorimage(img, tres);
        }

        return hasil;
    }
    
    public BufferedImage colorimage(BufferedImage img, int treshold) {
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        
        checked = new int[img.getWidth()][img.getHeight()];
        pointlist = new Vector();

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                checked[i][j] = 0;
            }
        }
        
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (checked[x][y] == 0) {
                    Color color = new Color(img.getRGB(x, y));
                    pointlist.add(new Point(x, y));
                    search(img, temp, color, treshold);
                }
            }
        }
        
        return temp;
    }
    
    public void search(BufferedImage img, BufferedImage temp, Color color, int treshold){
        while(pointlist.size() > 0) {
            Point p = pointlist.remove(0);
            if ((p.x >= 0) && (p.y >= 0) && (p.x < img.getWidth()) && (p.y < img.getHeight())) {
                if (checked[p.x][p.y] == 0) {
                    Color c = new Color(img.getRGB(p.x, p.y));
                    if (Math.abs(c.getRed()-color.getRed()) <= treshold &&
                            Math.abs(c.getGreen()-color.getGreen()) <= treshold &&
                            Math.abs(c.getBlue()-color.getBlue()) <= treshold) {
                        temp.setRGB(p.x, p.y, color.getRGB());
                        checked[p.x][p.y] = 1;
                        
                        pointlist.add(new Point(p.x - 1, p.y - 1));
                        pointlist.add(new Point(p.x, p.y - 1));
                        pointlist.add(new Point(p.x + 1, p.y - 1));
                        pointlist.add(new Point(p.x - 1, p.y));
                        pointlist.add(new Point(p.x + 1, p.y));
                        pointlist.add(new Point(p.x - 1, p.y + 1));
                        pointlist.add(new Point(p.x, p.y + 1));
                        pointlist.add(new Point(p.x + 1, p.y + 1));
                    }
                }
            }
        }
    }
    
    public BufferedImage split(BufferedImage img){
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        daftar = new ArrayList<>();
        akhir = new ArrayList<>();
        process(img, 0, img.getWidth(), 0, img.getHeight());
        System.out.println("Merge");
        merge2();
//        Integer warna;
//        merge();
        for (int i = 0; i < akhir.size()-1; i++) {
            
            int r = (Integer) akhir.get(i).get(4);
            int g = (Integer) akhir.get(i).get(5);
            int b = (Integer) akhir.get(i).get(6);
//            int r = (Integer) akhir.get((int)akhir.get(i).get(7)).get(4);
//            int g = (Integer) akhir.get((int)akhir.get(i).get(7)).get(5);
//            int b = (Integer) akhir.get((int)akhir.get(i).get(7)).get(6);
//            warna = (Integer) akhir.get(i).get(4);
            int start1 = (int) akhir.get(i).get(0);
            int start2 = (int) akhir.get(i).get(2);
            int end1 = (int) akhir.get(i).get(1);
            int end2 = (int) akhir.get(i).get(3);
//        for (int i = 0; i < daftar.size(); i++) {
//            int r = (Integer) daftar.get(i).get(4);
//            int g = (Integer) daftar.get(i).get(5);
//            int b = (Integer) daftar.get(i).get(6);
////            warna = (Integer) daftar.get(i).get(4);
//            int start1 = (int) daftar.get(i).get(0);
//            int start2 = (int) daftar.get(i).get(2);
//            int end1 = (int) daftar.get(i).get(1);
//            int end2 = (int) daftar.get(i).get(3);

            for (int j = start1; j < end1; j++) {
                for (int k = start2; k < end2; k++) {
//                    hasil.setRGB(j, k, new Color(warna, warna, warna).getRGB());
                    
                    temp.setRGB(j, k, new Color(r, g, b).getRGB());
                }
            }
        }
        return temp;
    }
    
    public void process(BufferedImage img, int startWidth, int endWidth, int startHeight, int endHeight) {
        if (check(img, startWidth, endWidth, startHeight, endHeight)
                || Math.abs(startWidth - endWidth) <= 2 || Math.abs(startHeight - endHeight) <= 2) {
            return;
        } else {
            process(img, startWidth, Math.abs((startWidth + endWidth) / 2), startHeight, Math.abs((endHeight + startHeight) / 2));//kiri atas
            process(img, Math.abs((startWidth + endWidth) / 2), endWidth, startHeight, Math.abs((endHeight + startHeight) / 2));//kanan atas
            process(img, startWidth, Math.abs((startWidth + endWidth) / 2), Math.abs((endHeight + startHeight) / 2), endHeight);//kiri bawah
            process(img, Math.abs((startWidth + endWidth) / 2), endWidth, Math.abs((endHeight + startHeight) / 2), endHeight);//kanan bawah
        }
    }

    public boolean check(BufferedImage img, int startWidth, int endWidth, int startHeight, int endHeight) {
        boolean same = true;
        Color c;
        int r = 0;
        int g = 0;
        int b = 0;
        int grayscale = 0;
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = startHeight; i < endHeight; i++) {
            for (int j = startWidth; j < endWidth; j++) {
                c = new Color(img.getRGB(j, i));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                grayscale = (r + g + b) / 3;

                int kiri, kanan, atas, bawah;
                if (j == 0) {
                    kiri = grayscale;
                } else {
                    c = new Color(img.getRGB(j - 1, i));
                    kiri = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                }

                if (j == img.getWidth() - 1) {
                    kanan = grayscale;
                } else {
                    c = new Color(img.getRGB(j + 1, i));
                    kanan = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                }

                if (i == 0) {
                    atas = grayscale;
                } else {
                    c = new Color(img.getRGB(j, i - 1));
                    atas = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                }

                if (i == img.getHeight() - 1) {
                    bawah = grayscale;
                } else {
                    c = new Color(img.getRGB(j, i + 1));
                    bawah = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                }

                if (Math.abs(grayscale - kiri) > tres || Math.abs(grayscale - kanan) > tres
                        || Math.abs(grayscale - atas) > tres || Math.abs(grayscale - bawah) > tres) {
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
            warna = grayscale;
//            temp.add(warna);
            temp.add(r);
            temp.add(g);
            temp.add(b);
            temp.add(id);
            id++;
            daftar.add(temp);
        }
        return same;
    }

    public boolean isNeighbour(ArrayList<Integer> p1, ArrayList<Integer> p2) {
        ArrayList<Integer> max;
        ArrayList<Integer> min;
        if ((p1.get(1) - p1.get(0)) > (p2.get(1) - p2.get(0))) {
            max = p1;
            min = p2;
        } else {
            max = p2;
            min = p1;
        }
//        System.out.println(max.get(0) + " " + max.get(1) + " " + max.get(2) + " " + max.get(3));
//        System.out.println(min.get(0) + " " + min.get(1) + " " + min.get(2) + " " + min.get(3));
        if (min.get(2) >= max.get(2) && min.get(3) <= max.get(3)) {
//            System.out.println("horizontal");
            if (min.get(0) == max.get(1) || max.get(0) == min.get(1)) {
                return true;
            }
            return false;
        }
        if (min.get(0) >= max.get(0) && min.get(1) <= max.get(1)) {
            if (min.get(2) == max.get(3) || max.get(2) == min.get(3)) {
                return true;
            }
        }
        return false;
    }
    
    public void merge2() {
        ArrayList<ArrayList> merge = new ArrayList();
        System.out.println("cek tetangga");
        for (int i = 0; i < daftar.size()-1; i++) {
            ArrayList<Integer> tetangga = new ArrayList();
            tetangga.add(i);
            for (int j = (i+1); j < daftar.size(); j++) {
                if (isNeighbour(daftar.get(i), daftar.get(j))) {
                    tetangga.add(j);
                }
            }
            merge.add(tetangga);
        }
        int [] id = new int[daftar.size()];
        System.out.println("prepare id");
        for (int i = 0; i < id.length; i++) {
            id[i] = -1;
        }
        int pk = 0;
        System.out.println("gabung by id");
        for (int i = 0; i < merge.size(); i++) {
            int r = (Integer) daftar.get(i).get(4);
            int g = (Integer) daftar.get(i).get(5);
            int b = (Integer) daftar.get(i).get(6);
            int gray = (r + g + b) /3;
            if (id[i] == -1) {
                id[i] = pk;
                pk++;
            }
//            System.out.println(i + " - " + pk + " - ");
            for (int j = 1; j < merge.get(i).size(); j++) {
                int change = (Integer) merge.get(i).get(j);
                int rj = (Integer) daftar.get(change).get(4);
                int gj = (Integer) daftar.get(change).get(5);
                int bj = (Integer) daftar.get(change).get(6);
                int grayj = (rj + gj + bj) /3;
//                System.out.println("  " + grayj);
                if (Math.abs(gray - grayj) < tres) {
                    id[change] = id[i];
                }
            }
        }
//        for (int i = 0; i < id.length; i++) {
//            System.out.println(i + " ID : " + id[i]);
//        }
        System.out.println("pecah warna");
        ArrayList<ArrayList> warna = new ArrayList();
//        System.out.println(pk);
        for (int i = 0; i < pk; i++) {
            ArrayList<Integer> temp = new ArrayList();
            int r = 0;
            int g = 0;
            int b = 0;
            int count = 0;
            for (int j = 0; j < id.length; j++) {
                if (id[j] == i) {
                    count++;
                    r = r + (Integer) daftar.get(j).get(4);
                    g = g + (Integer) daftar.get(j).get(5);
                    b = b + (Integer) daftar.get(j).get(6);
                }
            }
            temp.add(r/count);
            temp.add(g/count);
            temp.add(b/count);
            warna.add(temp);
        }
        System.out.println("add ke akhir");
        for (int i = 0; i < id.length; i++) {
            ArrayList<Integer> temp = new ArrayList();
            temp.add((Integer) daftar.get(i).get(0));
            temp.add((Integer) daftar.get(i).get(1));
            temp.add((Integer) daftar.get(i).get(2));
            temp.add((Integer) daftar.get(i).get(3));
            for (int j = 0; j < pk; j++) {
                if (id[i] == j) {
                    temp.add((Integer) warna.get(j).get(0));
                    temp.add((Integer) warna.get(j).get(1));
                    temp.add((Integer) warna.get(j).get(2));
                }
            }
            akhir.add(temp);
        }
    }
    
    public void merge() {
        ArrayList<Integer> temp;
        int gray ;
        int gray2 ;
        for (int i = 0; i < daftar.size() ; i++) {
            gray = ((int) daftar.get(i).get(4) + (int) daftar.get(i).get(5) + (int) daftar.get(i).get(6)) / 3;
            akhir.add(daftar.get(i));
            for (int j = i; j < daftar.size(); j++) {
                gray2 = ((int) daftar.get(j).get(4) + (int) daftar.get(j).get(5) + (int) daftar.get(j).get(6)) / 3;
                temp = new ArrayList<>();
                if (Math.abs(gray - gray2) < 5) {
                    temp.add((Integer) daftar.get(j).get(0));
                    temp.add((Integer) daftar.get(j).get(1));
                    temp.add((Integer) daftar.get(j).get(2));
                    temp.add((Integer) daftar.get(j).get(3));
                    temp.add((Integer) daftar.get(j).get(4));
                    temp.add((Integer) daftar.get(j).get(5));
                    temp.add((Integer) daftar.get(j).get(6));
                    temp.add((Integer) daftar.get(i).get(7));
                    akhir.add(temp);
                }
            }

//            if (Math.abs((int) daftar.get(i).get(4) - (int) daftar.get(i + 1).get(4)) < 10) {
//                temp.add((Integer) daftar.get(i).get(0));
//                temp.add((Integer) daftar.get(i + 1).get(1));
//                temp.add((Integer) daftar.get(i).get(2));
//                temp.add((Integer) daftar.get(i + 1).get(3));
//                temp.add((Integer) daftar.get(i).get(4));
//                temp.add((Integer) daftar.get(i).get(5));
//                temp.add((Integer) daftar.get(i).get(6));
//                akhir.add(temp);
//            }
        }
    }

    public void close() {
        cap.release();
    }
}
