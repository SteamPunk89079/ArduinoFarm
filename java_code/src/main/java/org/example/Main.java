package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Com com = new Com();


        com.startSerialCom();

        String line = com.readDataRequest();

        System.out.println(line);

        line = com.readDataRequest();

        JsonReading my_Reading = new JsonReading(line);
        Reading reading = my_Reading.interpretReading();
        System.out.println(reading.toString());

        com.close_serial_port();
    }
}