package org.example.GUI;

import javax.swing.*;

import org.example.Com;
import org.example.JsonReading;
import org.example.Reading;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyPanel extends JPanel {

    private JTextArea SCREEN = new JTextArea("");
    private Image backgroundImage;
    
    private JButton btn_continous_scan = new JButton("Start Scan");
    private JButton btn_stop_scan = new JButton("Stop Scan");
    
    private volatile boolean isScanRunning = false;
    private Thread scanThread;

    private BigInteger package_number = new BigInteger("0") ;
    private int nr_prints_onscreen = 0 ;


    public MyPanel() {
        this.setPreferredSize(new Dimension(1100, 800));
        this.setBackground(Color.green);


        background_setup(); // Load the background image
        screen_setup();

        btn_continous_scan.addActionListener(new ContinousScanListener());
        btn_stop_scan.addActionListener(new StopScanListener());

        
        Com com = new Com();


        this.add(SCREEN);
        this.add(btn_continous_scan); this.add(btn_stop_scan);
        this.setVisible(true);
    }





    private class SCAN_THREAD implements Runnable {
        @Override
        public void run() {
            while (isScanRunning) {
                if (nr_prints_onscreen < 9){
                    
                    package_number = package_number.add(BigInteger.ONE);
                    Reading reading = request_reading(1);
                    add_text_to_screen(1, reading);
                    nr_prints_onscreen ++;                
                    try {
                        Thread.sleep(1000); // Optional: Delay between scans
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }else{
                    nr_prints_onscreen = 0;
                    SCREEN.setText("");
                }
            }
        }
    }


    private class ContinousScanListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isScanRunning) {
                package_number.add(BigInteger.ONE);
                LocalDateTime time = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = time.format(formatter);
                isScanRunning = true;
                scanThread = new Thread(new SCAN_THREAD());
                scanThread.start();
                System.out.println("|------------------------------------------------|");
                System.out.println("|---------------SCAN-STARTED---------------------|");
                System.out.println("|---------------------------"+formattedDateTime+"|");
                System.out.println();
            }
        }
    }

    private class StopScanListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            isScanRunning = false;
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = time.format(formatter);
            System.out.println("|------------------------------------------------|");
            System.out.println("|---------------SCAN STOPPED---------------------|");
            System.out.println("|---------------------------"+formattedDateTime+"|");
        }
    }
    
    
    public void screen_setup() {
        Font font = new Font("Times New", Font.BOLD, 14);
        SCREEN.setSize(new Dimension(300, 500));
        SCREEN.setEditable(false);
        SCREEN.setFont(font);
        SCREEN.setForeground(Color.decode("#8B0000"));
        SCREEN.setBackground(Color.gray);
        SCREEN.setColumns(80);
        SCREEN.setRows(18);

    }

    public void button_setup() {
        Dimension btn_dimension = new Dimension(70, 20);
        btn_continous_scan.setSize(btn_dimension);
        btn_stop_scan.setSize(btn_dimension);

        
    }

    public void background_setup() {
        backgroundImage = new ImageIcon("C:\\Users\\samsu\\OneDrive\\Desktop\\JAVA BEST\\FARM 15JUN\\java_code\\src\\main\\resources\\Background.jpg").getImage();
        repaint();
    }




    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image, scaled to fit the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    public Reading request_reading(int nr_readings){

        Reading reading = new Reading();
        //Com com = new Com();
        for (int i = 0 ; i < nr_readings ; i++){
            String line1 = Com.readDataRequest();
            //System.out.println(line1);
            JsonReading reading_raw_1 = new JsonReading(line1);
            reading = reading_raw_1.getREADING();
            System.out.println("READING :"+reading.toString());
            
        }
        //continous scanning function
        return reading;
        //com.close_serial_port();
    }

    private void add_text_to_screen(int code, Reading reading){
        String text = null;
        
        if (code == 1){
            text = "------------------------------------------------"+"\n"
            +reading.toString();
            SCREEN.setText(SCREEN.getText()+ text +"\n");
            
        }





        


    }





}
