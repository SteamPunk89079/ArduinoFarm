package org.example.GUI;

import javax.swing.*;
import javax.swing.border.Border;

import org.example.Com;
import org.example.JsonReading;
import org.example.Reading;

import com.fazecast.jSerialComm.SerialPortIOException;

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
    private JButton btn_settings = new JButton("Setting");
    
    private volatile boolean isScanRunning = false;
    private Thread scanThread;

    private BigInteger package_number = new BigInteger("0") ;
    private int nr_prints_onscreen = 0 ;

    Com com;

    public MyPanel() {
        this.setPreferredSize(new Dimension(1100, 800));
        this.setBackground(Color.green);


        background_setup(); // Load the background image
        screen_setup();

        btn_continous_scan.addActionListener(new ContinousScanListener());
        btn_stop_scan.addActionListener(new StopScanListener());
        btn_settings.addActionListener(new SettingsListener());
        
        button_setup();
        
        try{
            com = new Com();
        }catch (Exception e){
            System.out.println("AICI");
            add_text_to_screen(2, new Reading());
        } finally {
            if (com != null){
                com.close_serial_port();
            }
        }


        
        this.add(SCREEN);
        this.add(btn_continous_scan); this.add(btn_stop_scan);this.add(btn_settings);
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
                System.out.println("|---------------------------"+formattedDateTime+"--|");
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
    
    private class SettingsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            show_settings_dialog();

        }
    }


    private void show_settings_dialog(){
        JDialog settingsDialog = new JDialog();
        JTextField max_tf = new JTextField("degrees");
        JTextField min_tf = new JTextField("degrees");
        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(3, 2));

        settingsDialog.add(new JLabel("Max temperature:"));
        settingsDialog.add(max_tf);

        settingsDialog.add(new JLabel("Min temperature:"));
        settingsDialog.add(min_tf);
        

        JButton SaveButton = new JButton("SAVE");
        SaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.dispose(); 
            }
        });
        JButton ExitButton = new JButton("EXIT");
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.dispose();
            }
        });
        
        settingsDialog.add(SaveButton);settingsDialog.add(ExitButton);
        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.setVisible(true);

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
        Font font = new Font("Arial", Font.BOLD, 14);

        Color backgroundColor = new Color(60, 63, 65); // Dark gray
        Color textColor = Color.WHITE; // White text
        Color hoverColor = new Color(80, 80, 80); // Lighter gray for hover


        Border border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        );

        configureButton(btn_continous_scan, font, backgroundColor, textColor, border, hoverColor);
        configureButton(btn_stop_scan, font, backgroundColor, textColor, border, hoverColor);
        configureButton(btn_settings, font, backgroundColor, textColor, border, hoverColor);
    
        
    }
  
    public void background_setup() {
        backgroundImage = new ImageIcon("C:\\Users\\samsu\\OneDrive\\Desktop\\JAVA BEST\\FARM 15JUN\\java_code\\src\\main\\resources\\Background.jpg").getImage();
        repaint();
    }

    private void configureButton(JButton button, Font font, Color backgroundColor, Color textColor, Border border, Color hoverColor) {
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(font);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setBorder(border);
        button.setFocusPainted(false);
    
        // Add a hover effect using mouse listeners
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
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
        for (int i = 0 ; i < nr_readings ; i++){

            String line = null;
            
            line = Com.readDataRequest();
          

            

            //System.out.println(line1);
            JsonReading reading_raw_1 = new JsonReading(line);
            reading = reading_raw_1.getREADING();
            System.out.println("READING :"+reading.toString());
            
        }
        return reading;
    }

    private void add_text_to_screen(int code, Reading reading){
        String text = null;
        
        if (code == 1){
            text = "------------------------------------------------"+"\n"
            +reading.toString();
            SCREEN.setText(SCREEN.getText()+ text +"\n");
            
        }

        if (code == 2){
            text = "------------------------------------------------"+"\n"
            +"Unable to establish hardware connection";
            SCREEN.setText(SCREEN.getText()+ text +"\n");
            
        }




        

        /*
         * OLED SCREEN .ino CODE IS NEXT
         */
    }





}
