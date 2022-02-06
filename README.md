# waste_assistant

It is an Android mobile application that displays septic tank level detected using an ultrasonic sensor.
it incorporates firebase for authentication, realtime database which stores data from the sensor
and retrieved by the mobile app and cloud messaging to notify user when level is high and needs to be emptied.
Arduino IDE is used to develop the IoT side where we have the ultrasonic sensor being assembled and connected
as well as ESP 8266 Wi-Fi module for connectivity to the internet to facilitate the retrival of data from sensor to database.
