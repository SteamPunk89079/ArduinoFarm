package org.example;


public class Reading {


    private String date, time, temp1, hum1, temp2, hum2, temp3, hum3;


    
    public Reading(String date, String time, String temp1, String hum1,
                   String temp2, String hum2, String temp3, String hum3) {
        if (date != null && !date.isEmpty() && 
            time != null && !time.isEmpty() && 
            temp1 != null && !temp1.isEmpty() && 
            hum1 != null && !hum1.isEmpty() && 
            temp2 != null && !temp2.isEmpty() && 
            hum2 != null && !hum2.isEmpty() && 
            temp3 != null && !temp3.isEmpty() && 
            hum3 != null && !hum3.isEmpty()) {
            
            this.date = date;
            this.time = time;
            this.temp1 = temp1;
            this.hum1 = hum1;
            this.temp2 = temp2;
            this.hum2 = hum2;
            this.temp3 = temp3;
            this.hum3 = hum3;    
        } else {
            System.out.println("PACKAGING ERROR");
        }
    }
    public Reading(){
    }
    

    @Override
    public String toString() {
        
        String degreeSymbol = "\u00B0";
        String formatted_string = "|" + date + "|" + time + "| SENSOR_1 temp1:" + temp1 + degreeSymbol +
                " hum1:" + hum1 + "% | SENSOR_2 temp2:" + temp2 + degreeSymbol + " hum2:" + hum2 + "% | SENSOR_3 temp3:" +
                temp3 + degreeSymbol + " hum3:" + hum3 + "%";

        return formatted_string;
    }


    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getTemp1() {
        return temp1;
    }
    public String getHum1() {
        return hum1;
    }
    public String getTemp2() {
        return temp2;
    }
    public String getHum2() {
        return hum2;
    }
    public String getTemp3() {
        return temp3;
    }
    public String getHum3() {
        return hum3;
    }
}
