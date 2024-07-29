package org.example;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Com com = new Com();

        String line1 = Com.readDataRequest();
        String line2 = Com.readDataRequest();
        String line3 = Com.readDataRequest();

        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);

        JsonReading reading_raw_1 = new JsonReading(line1);
        JsonReading reading_raw_2 = new JsonReading(line2);
        JsonReading reading_raw_3 = new JsonReading(line3);

        Reading reading1 = reading_raw_1.getREADING();
        Reading reading2 = reading_raw_2.getREADING();
        Reading reading3 = reading_raw_3.getREADING();

        System.out.println("-----------------------------------");

        System.out.println("READING1 :"+reading1.toString());
        System.out.println("READING2 :"+reading2.toString());
        System.out.println("READING3 :"+reading3.toString());

        System.out.println("-----------------------------------");


        com.close_serial_port();
    }
}