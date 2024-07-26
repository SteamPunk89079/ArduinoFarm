package org.example;


public class Reading {


    private String date, time, temp1, hum1, temp2, hum2, temp3, hum3;

    public Reading (String date,String time, String temp1, String hum1,
                    String temp2, String hum2, String temp3, String hum3){

        this.date=date;
        this.time=time;
        this.temp1=temp1;this.hum1=hum1;
        this.temp2=temp2;this.hum2=hum2;
        this.temp3=temp3;this.hum3=hum3;

    }


    @Override
    public String toString() {

        String degreeSymbol = "\u00B0";
        String formatted_string = "|" + date + "|" + time + "| SENSOR_1 temp1:" + temp1 + degreeSymbol +
                " hum1:" + hum1 + "% | SENSOR_2 temp2:" + temp2 + degreeSymbol + " hum2:" + hum2 + "% | SENSOR_3 temp3:" +
                temp3 + degreeSymbol + " hum3:" + hum3 + "%";

        return formatted_string;
    }
}
