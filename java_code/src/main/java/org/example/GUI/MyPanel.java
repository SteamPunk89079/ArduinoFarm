package org.example.GUI;

import javax.swing.*;

import org.example.Com;
import org.example.JsonReading;
import org.example.Reading;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPanel extends JPanel {

    private JTextArea SCREEN = new JTextArea("");
    private Image backgroundImage;
    
    private JButton button1 = new JButton("Button1");
    private JButton button2 = new JButton("Button2");
    

    public MyPanel() {
        this.setPreferredSize(new Dimension(1100, 800));
        this.setBackground(Color.green);

        background_setup(); // Load the background image
        screen_setup();

        button1.addActionListener(new MyActionListener());




        this.add(SCREEN);
        this.add(button1); this.add(button2);
        this.setVisible(true);
    }


    public class MyActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }

    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image, scaled to fit the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void screen_setup() {
        SCREEN.setSize(new Dimension(300, 500));
        SCREEN.setEditable(false);
        SCREEN.setForeground(Color.red);
        SCREEN.setBackground(Color.gray);
        SCREEN.setColumns(80);
        SCREEN.setRows(18);

    }

    public void button_setup() {
        Dimension btn_dimension = new Dimension(70, 20);
        button1.setSize(btn_dimension);
        button2.setSize(btn_dimension);

        
    }

    public void background_setup() {
        backgroundImage = new ImageIcon("C:\\Users\\samsu\\OneDrive\\Desktop\\JAVA BEST\\FARM 15JUN\\java_code\\src\\main\\resources\\Background.jpg").getImage();
        repaint();
    }





    public void request_readings(int nr_readings){


        Com com = new Com();
        String line1 = Com.readDataRequest();
        System.out.println(line1);
        JsonReading reading_raw_1 = new JsonReading(line1);
        Reading reading1 = reading_raw_1.getREADING();
        System.out.println("READING1 :"+reading1.toString());

        //continous scanning function

        com.close_serial_port();
    }

}
