#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "DATABASE HOSTNAME"
#define FIREBASE_AUTH "FIREBASE SECRET KEY"
#define WIFI_SSID "YOUR WIFI NAME"
#define WIFI_PASSWORD "WIFI PASSWORD"


String values,sensor_data;

void setup() {
  //Initializes the serial connection at 9600 get sensor data from arduino.
   Serial.begin(9600);
   
  delay(1000);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    
  }
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH); 
  
}
void loop() {

 bool Sr =false;
 
  while(Serial.available()){
    
        //get sensor data from serial put in sensor_data
        sensor_data=Serial.readString(); 
        Sr=true;    
        
    }
  
delay(1000);

  if(Sr==true){  
    
  values=sensor_data;
  
  //get comma indexes from values variable
  int fristCommaIndex = values.indexOf(',');
 
  
  //get sensors data from values variable by  spliting by commas and put in to variables  
  String ultrasonic_value = values.substring(0, fristCommaIndex);
 
  //store ultrasonic sensor data as string in firebase 
  Firebase.setString("ultrasonic_value",ultrasonic_value);
   delay(10);
  
  //store previous sensors data as string in firebase
  
  Firebase.pushString("previous_ultrasonic_value",ultrasonic_value);
 
  delay(1000);
  
  if (Firebase.failed()) {  
      return;
  }
  
}   
}
