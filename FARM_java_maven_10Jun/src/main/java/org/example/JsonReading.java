package org.example;

import java.util.ArrayList;

public class JsonReading {

    private static String reading;

    private Reading READING;

    public JsonReading(String reading){
        this.reading = reading;

        READING = interpretReading();

        //System.out.println(READING.toString());


    }

    public Reading interpretReading(){

        Reading READING = null;
        if (isValidReading(reading)) {
            System.out.println("Valid data string.");

            ArrayList<String> processed_data = processReading(reading);
            READING = package_data(processed_data);


            return READING;
        } else {
            System.out.println("Invalid data string");
        }
        return READING;
    }


    private static Reading package_data (ArrayList<String> processed_data){
        Reading packaged_reading = new Reading(processed_data.get(0),processed_data.get(1),
                processed_data.get(2),processed_data.get(3),processed_data.get(4),processed_data.get(5),
                processed_data.get(6),processed_data.get(7));

        return packaged_reading;
    }

    private static ArrayList<String> processReading(String reading) {
        char character;
        int quoteNumber = 0;
        StringBuilder date = new StringBuilder();
        StringBuilder time = new StringBuilder();
        StringBuilder temp1 = new StringBuilder();
        StringBuilder hum1 = new StringBuilder();
        StringBuilder temp2 = new StringBuilder();
        StringBuilder hum2 = new StringBuilder();
        StringBuilder temp3 = new StringBuilder();
        StringBuilder hum3 = new StringBuilder();
        int informations_collected = 0;

        //--------------------LOOP----------------------------------------------
        for (int i = 0; i < reading.length(); i++) {
            character = reading.charAt(i);

            if (character == '\"') {
                quoteNumber++;
            }

            switch (informations_collected) {
                case 0:
                    if (quoteNumber == 5) {
                        //---------------------DATE-TIME------------------------
                        int startDateIndex = i + 1;
                        int endDateIndex = reading.indexOf('\"', startDateIndex);

                        if (endDateIndex != -1) {
                            String dateTimeStr = reading.substring(startDateIndex, endDateIndex);
                            String[] dateTimeParts = dateTimeStr.split(" \\|\\| ");

                            if (dateTimeParts.length == 2) {
                                date.append(dateTimeParts[0]);
                                time.append(dateTimeParts[1]);
                            }
                            quoteNumber = 0;
                            informations_collected++;
                        }
                    }
                    break;
                case 1:
                    if (quoteNumber == 7) {
                        //----------------------TEMP-1---------------------------------
                        int startTempIndex = i + 2;
                        int endTempIndex = reading.indexOf('\"', startTempIndex);

                        if (endTempIndex != -1) {
                            temp1.append(reading.substring(startTempIndex, endTempIndex - 1));
                            quoteNumber = 0;
                            informations_collected++;
                        }
                    }
                    break;
                case 2:
                    if (quoteNumber == 2) {
                        //-----------------------HUM-1----------------------------------------
                        int startHumIndex = i + 2;
                        int endHumIndex = reading.indexOf('\"', startHumIndex);

                        if (endHumIndex != -1) {
                            hum1.append(reading.substring(startHumIndex, endHumIndex - 2));
                            quoteNumber = 0;
                            informations_collected++;
                        }
                    }
                    break;
                case 3:
                    if (quoteNumber == 6) {
                        //---------------------------TEMP-2-------------------------------------
                        int startTemp2Index = i + 2;
                        int endTemp2Index = reading.indexOf('\"', startTemp2Index);

                        if (endTemp2Index != -1) {
                            temp2.append(reading.substring(startTemp2Index, endTemp2Index - 1));
                            quoteNumber = 0;
                            informations_collected++;
                        }
                    }
                    break;
                case 4:
                    if (quoteNumber == 2) {
                        //----------------------------HUM-2----------------------------------
                        int startHum2Index = i + 2;
                        int endHum2Index = reading.indexOf('\"', startHum2Index);

                        if (endHum2Index != -1) {
                            hum2.append(reading.substring(startHum2Index, endHum2Index - 2));
                            quoteNumber = 0;
                            informations_collected++;
                        }
                    }
                    break;
                case 5:
                    if (quoteNumber == 6) {
                        //---------------------------TEMP-3-------------------------------------
                        int startTemp3Index = i + 2;
                        int endTemp3Index = reading.indexOf('\"', startTemp3Index);

                        if (endTemp3Index != -1) {
                            temp3.append(reading.substring(startTemp3Index, endTemp3Index - 1));
                            quoteNumber = 0;
                            informations_collected++;
                        }
                    }
                    break;
                case 6:
                    if (quoteNumber == 2) {
                        //----------------------------HUM-3----------------------------------
                        int startHum3Index = i + 2;
                        int endHum3Index = reading.indexOf('}', startHum3Index);

                        if (endHum3Index != -1) {
                            hum3.append(reading.substring(startHum3Index, endHum3Index));
                            quoteNumber = 0;
                            informations_collected++;
                        }
                    }
                    break;
            }
        }
/*
        System.out.println("Extracted date: " + date.toString());
        System.out.println("Extracted time: " + time.toString());
        System.out.println("Extracted temp1: " + temp1.toString());
        System.out.println("Extracted hum1: " + hum1.toString());
        System.out.println("Extracted temp2: " + temp2.toString());
        System.out.println("Extracted hum2: " + hum2.toString());
        System.out.println("Extracted temp3: " + temp3.toString());
        System.out.println("Extracted hum3: " + hum3.toString());
*/
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(date));
        data.add(String.valueOf(time));
        data.add(String.valueOf(temp1));
        data.add(String.valueOf(hum1));
        data.add(String.valueOf(temp2));
        data.add(String.valueOf(hum2));
        data.add(String.valueOf(temp3));
        data.add(String.valueOf(hum3));

/*
        for (String _data : data){
            System.out.println(_data);
        }
    }
*/
        return data;

    }
    private static boolean isValidReading(String reading) {
        return reading.contains("date") && reading.contains("sensor1") && reading.contains("sensor2")
                && reading.contains("sensor3") && reading.contains("temp") && reading.contains("temp2")
                && reading.contains("temp3") && reading.contains("hum") && reading.contains("hum2")
                && reading.contains("hum3");
    }

    public Reading getREADING() {
        return READING;
    }
}
