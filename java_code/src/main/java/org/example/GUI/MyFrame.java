package org.example.GUI;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;



public class MyFrame extends JFrame {

    private MyPanel my_panel;

    public MyFrame(){

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1100, 800));
        this.setResizable(false);
        this.setTitle("My_Farm");
        this.setLocationRelativeTo(null);

        my_panel = new MyPanel();

        this.add(my_panel);
        set_icon();




        this.setVisible(true);
        this.pack();

    }



    public void set_icon(){
        ImageIcon icon = new ImageIcon("C:\\Users\\samsu\\OneDrive\\Desktop\\JAVA BEST\\FARM 15JUN\\java_code\\src\\main\\resources\\Icon.png");
        setIconImage(icon.getImage());
    }


    
}
