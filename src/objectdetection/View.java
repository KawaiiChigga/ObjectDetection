/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objectdetection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Daniel
 */
public class View extends javax.swing.JFrame implements Runnable {

    private JPanel contentPane;
    private JLabel content;
    private ImageIcon imgFrame;
    private JSlider treshold;
    private JLabel nilaitres;
    private JButton btnCamera;
    private JButton btnBrowse;
    private JComboBox<String> cbMode;
    
    private int cam = 0;
    private String location = "";
    private int mode = 0;
    VideoCap videoCap;
    Thread th;
    
    public View() {
        videoCap = new VideoCap();
        initComponents();
        setLayout(null);
        setTitle("Object Detection");
        setMinimumSize(new Dimension(450, 450));
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e); //To change body of generated methods, choose Tools | Templates.
                videoCap.close();
            }
        });
        prepare();
        th = new Thread(this);
        th.start();
    }

    public void prepare() {
        location = "src/img/sample.jpg";
        btnBrowse = new JButton("Browse");
        btnBrowse.setBounds(10, 10, 80, 30);
        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCamera.setText("Turn On Camera");
                cam = 0;
                
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int val = fileChooser.showOpenDialog(null);
                if (val == JFileChooser.APPROVE_OPTION) {
                    location = fileChooser.getSelectedFile().getAbsolutePath();
                }
            }
        });
        
        btnCamera = new JButton("Turn On Camera");
        btnCamera.setBounds(100, 10, 120, 30);
        btnCamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cam == 0) {
                    cam = 1;
                } else {
                    cam = 0;
                    videoCap.close();
                }
            }
        });
        
        String [] mod = new String[] {"Splitting and Merging", "Color Image Segmentation"};
        cbMode = new JComboBox(mod);
        cbMode.setBounds(240, 10, 170, 30);
        cbMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbMode.getSelectedIndex() == 0) {
                    mode = 0;
                } else {
                    mode = 1;
                }
            }
        });
        
        content = new JLabel();
        content.setOpaque(true);
        
        treshold = new JSlider();
        treshold.setOrientation(JSlider.HORIZONTAL);
        treshold.setMaximum(255);
        treshold.setMinimum(0);
        treshold.setValue(40);
        
        nilaitres = new JLabel();
        
        add(btnBrowse);
        add(btnCamera);
        add(cbMode);
        add(content);
        add(treshold);
        add(nilaitres);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(656, 521));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        new View().setVisible(true);
    }
    
    @Override
    public void run() {
        while(true) {
            repaint();
            if (cam == 0) {
                btnCamera.setText("Turn On Camera");
            } else {
                btnCamera.setText("Turn Off Camera");
            }
            
            try {
                int t = treshold.getValue();
                imgFrame = new ImageIcon(videoCap.getOneFrame(t, location, cam, mode));
                setSize(imgFrame.getIconWidth() + 40, imgFrame.getIconHeight() + 140);
                
                treshold.setBounds(10, getHeight()-85, imgFrame.getIconWidth(), 20);
                
                nilaitres.setBounds(10, getHeight()-65, imgFrame.getIconWidth(), 20);
                nilaitres.setText(t+"");
                nilaitres.setHorizontalAlignment(JLabel.CENTER);
                
                content.setBounds(10, 50, imgFrame.getIconWidth(), imgFrame.getIconHeight());
                content.setIcon(imgFrame);
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
