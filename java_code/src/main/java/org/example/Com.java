package org.example;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import com.fazecast.jSerialComm.*;


public class Com {

    private static AtomicReference<String> receivedData = new AtomicReference<>("");

    private static SerialPort serialPort;

    
    public Com() {
        
        startSerialCom();
        
    }

    
    public static String readDataRequest() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread readerThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(serialPort.getInputStream()))) {

                String line;
                String new_line;
                while ((line = reader.readLine()) != null) {//REVISIT
                    new_line = reader.readLine();
                    sendDataRequest();
                    receivedData.set(new_line);

                    //System.out.println("Received: " + new_line);
                    latch.countDown(); // Signal that data has been received
                    break; // Exit loop after receiving data
                }
            } catch (IOException e) {
                System.out.println("read_error");
                e.printStackTrace();
            }
        });

        readerThread.start();

        try {
            latch.await(); // Wait until the latch is counted down
        } catch (InterruptedException e) {
            System.out.println("thread_interrupted");
            e.printStackTrace();
        }

        return receivedData.get();

    }

    public static void sendDataRequest() {
        try (OutputStream outputStream = serialPort.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

            writer.write('1');
            //System.out.println("data request sent\n"+
            //        "---------------------");
            System.out.println("------------------------------------------------");
            writer.flush();

        } catch (IOException e) {
            System.out.println("OBJECT REQUEST ERROR");
        }
    }

    public void startSerialCom() {
        serialPort = SerialPort.getCommPort("COM3");
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 2000, 0);


        if (serialPort.openPort()) {
            System.out.println("---------------------\n"+"PORT OPENED");
        } else {
            System.out.println("FAILED TO OPEN PORT");
        }
    }

    public void close_serial_port(){
        serialPort.closePort();
    }
        
    

}
