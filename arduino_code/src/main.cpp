#include <RTClib.h> //clock lib
#include <ArduinoJson.h>
#include <DHT.h> //DHT library
#include <Arduino.h>
#include <Adafruit_Sensor.h>
#include <SPI.h>

#define DHTPIN1 2 
#define DHTPIN2 3
#define DHTPIN3 4

#define DHTTYPE DHT11    

#define buzzer_pin 13

DHT dht1(DHTPIN1, DHTTYPE); DHT dht2(DHTPIN2, DHTTYPE);DHT dht3(DHTPIN3, DHTTYPE);

float temp_S1; float hum_S1;
float temp_S2; float hum_S2;
float temp_S3; float hum_S3;

RTC_DS1307 rtc;



//-------------------------------------------------------------------------------------
void setup() {
  Serial.begin(9600); 

  dht1.begin(); dht2.begin(); dht3.begin();

  //rtc.begin();

  RTC_DS1307 current_time;

  //DateTime time = (DateTime) current_time;
  //Serial.println(current_time);

   if (!rtc.begin()) {
    Serial.println("Couldn't find RTC");
    while (1);
  }

  // Check if the RTC is running
  if (!rtc.isrunning()) {
    Serial.println("RTC is NOT running, let's set the time!");
    // Uncomment the following line to set the date and time to the time when the sketch was compiled
    rtc.adjust(DateTime(F(__DATE__), F(__TIME__)));
    // Alternatively, you can set the date and time manually like this:
    // rtc.adjust(DateTime(2024, 6, 12, 15, 0, 0)); // Year, Month, Day, Hour, Minute, Second
  }

  pinMode(buzzer_pin, OUTPUT);
}
//-------------------------------------------------------------------------------------




String requestReading() {
  float temp_S1 = dht1.readTemperature();
  float hum_S1 = dht1.readHumidity();
  float temp_S2 = dht2.readTemperature();
  float hum_S2 = dht2.readHumidity();
  float temp_S3 = dht3.readTemperature();
  float hum_S3 = dht3.readHumidity();

  DateTime now = rtc.now();
  String formattedDate = String(now.month()) + "-" + String(now.day()) + "-" + String(now.year());
  String formattedTime = String(now.hour()) + ":" + String(now.minute()) + ":" + String(now.second());
  String formattedDateTime = formattedDate + " || " + formattedTime;

  StaticJsonDocument<500> sensors_read_object;

  JsonObject date = sensors_read_object.createNestedObject("date");
  date["date_time"] = formattedDateTime;


  JsonObject S1 = sensors_read_object.createNestedObject("sensor1");
  S1["S_nr"] = 1;
  S1["temp"] = temp_S1;
  S1["hum"] = hum_S1;


  JsonObject S2 = sensors_read_object.createNestedObject("sensor2");
  S2["S_nr"] = 2;
  S2["temp"] = temp_S2;
  S2["hum"] = hum_S2;

  JsonObject S3 = sensors_read_object.createNestedObject("sensor3");
  S3["S_nr"] = 3;
  S3["temp"] = temp_S3;
  S3["hum"] = hum_S3;

  String sensors_read_json;
  serializeJson(sensors_read_object, sensors_read_json);

  return sensors_read_json;
}


void beep() {

  for ( int i = 0 ; i < 50 ; i++){

    digitalWrite(buzzer_pin, HIGH);
    delay(70);

    digitalWrite(buzzer_pin, LOW);
    delay(70);

  }

}


void loop() {

  delay(100);

  /*if (Serial.available() > 0) {
    char command = Serial.read();
    


    if (command == '1') {

      requestReading();
      beep();

      Serial.println("test");
    }

  } else {
    Serial.println("Waiting for serial input...");
  }
*/
  String line = requestReading();
  Serial.println(line);
}




