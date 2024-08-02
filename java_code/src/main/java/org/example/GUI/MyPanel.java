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

    private double max_temp = 999; private double min_temp = -10;
    String degreeSymbol = "\u00B0";

    JDialog notificationDialog = new JDialog();

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

                    if (get_medium_temp(reading) > max_temp){

                        System.out.println("Max temp exceded");
                        add_text_to_screen(3, reading);
                        JOptionPane.showMessageDialog(notificationDialog, 
                            "Temperature exceeding :"+max_temp+degreeSymbol+"/n"+
                            "current temperature :"+get_medium_temp(reading)+degreeSymbol, 
                            "Max temp exceeded", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                    if (get_medium_temp(reading) < min_temp){

                        System.out.println("Min temp exceded");
                        add_text_to_screen(4, reading);
                        JOptionPane.showMessageDialog(notificationDialog, 
                        "Temperature below :"+min_temp+degreeSymbol+"/n"+
                        "current temperature :"+get_medium_temp(reading)+degreeSymbol, 
                        "Max temp exceeded", 
                        JOptionPane.ERROR_MESSAGE);
                    }

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

    protected Double get_medium_temp(Reading reading){
        double temp1, temp2, temp3;
        
        temp1 = parseDouble(reading.getTemp1());
        temp2 = parseDouble(reading.getTemp2());
        temp3 = parseDouble(reading.getTemp3());
        return (temp1 + temp2 + temp3)/3;
    }
    protected Double get_medium_hum(Reading reading){
        double hum1, hum2, hum3;
        
        hum1 = parseDouble(reading.getHum1());
        hum2 = parseDouble(reading.getHum2());
        hum3 = parseDouble(reading.getHum3());
        return (hum1 + hum2 + hum3)/3;
    }
    public static Double parseDouble(String str) {
        if (str == null || str.trim().isEmpty()) {
            System.err.println("Input string is null or empty.");
            return null;
        }

        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            System.err.println("Error: '" + str + "' is not a valid number.");
            return null;
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


    private void show_settings_dialog() {
        JDialog settingsDialog = new JDialog();
        JTextField max_tf = new JTextField();
        JTextField min_tf = new JTextField();
        

        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(3, 2));
    
        settingsDialog.add(new JLabel("       Max temperature:"));
        settingsDialog.add(max_tf);
    
        settingsDialog.add(new JLabel("       Min temperature:"));
        settingsDialog.add(min_tf);
        
        JButton saveButton = new JButton("SAVE");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double maxTemp = Double.parseDouble(max_tf.getText());
                    double minTemp = Double.parseDouble(min_tf.getText());
                    // Implement the logic to use maxTemp and minTemp
                    System.out.println("Max Temp: " + maxTemp + ", Min Temp: " + minTemp);
                    settingsDialog.dispose();
                    max_temp = maxTemp; min_temp = minTemp; 
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(settingsDialog, 
                        "Please enter valid numeric values.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.dispose();
            }
        });
    
        settingsDialog.add(saveButton);
        settingsDialog.add(exitButton);
        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.setVisible(true);
    }
    
    public void configureTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.WHITE);
        textField.setBackground(new Color(60, 63, 65));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    public void configureButton(JButton button, Font font, Color backgroundColor, Color textColor, Border border, Color hoverColor) {
        button.setFont(font);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setBorder(border);
        button.setFocusPainted(false);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
    
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
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
            
        }else if (code == 2){
            text = "------------------------------------------------"+"\n"
            +"Unable to establish hardware connection";
            SCREEN.setText(SCREEN.getText()+ text +"\n");
            
        }else if (code == 3){
            text =  "------------------------------------------------"+"\n"
            +"MAX TEMP EXCEDED";
            SCREEN.setText(SCREEN.getText()+ text +"\n");
        }else if (code == 4){
            text =  "------------------------------------------------"+"\n"
            +"MIN TEMP EXCEDED";
            SCREEN.setText(SCREEN.getText()+ text +"\n");
        }




        

        /*
         * OLED SCREEN .ino CODE IS NEXT
         */
    }





}
