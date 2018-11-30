#define CUSTOM_SETTINGS
#define INCLUDE_INTERNET_SHIELD
#define INCLUDE_GPS_SHIELD
#define INCLUDE_TERMINAL_SHIELD

#include <OneSheeld.h>
char CAR_ID[25] = "\"-LSU82U4mAjAfdXBzYQ9\"";
char URL[100] = "https://group-project-22afb.firebaseio.com/CARS/-LSU82U4mAjAfdXBzYQ9/position.json";
HttpRequest request(URL);

void setup() {
  OneSheeld.begin();

}
 
void loop() {
  OneSheeld.delay(10000);
  
  char strLon[20];
  char strLat[20];
  
  dtostrf(GPS.getLongitude(), 4, 6, strLon);
  dtostrf(GPS.getLatitude(), 4, 6, strLat);
  
  Terminal.println(strLon);
  Terminal.println(strLat);

  char data[180] = "{";
  strcat(strcat(data, "\"longitude\":"), strLon);
  strcat(strcat(data, ", \"latitude\":"), strLat);
  strcat(data, "}");
  
  Terminal.println(data);
  
  request.addRawData(data);
  Internet.performPut(request);
  
}
