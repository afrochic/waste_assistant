#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "vaulted-scholar-284900-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "HG8eu2PLzh0ZpfW3S0jaMLqLqxuF71DulTvg1d7W"
#define WIFI_SSID "Syoki-Amani"
#define WIFI_PASSWORD "CMusee@123"
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
  int secondCommaIndex = values.indexOf(',', fristCommaIndex+1);
  int thirdCommaIndex = values.indexOf(',', secondCommaIndex + 1);
  
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
