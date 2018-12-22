                        #define CUSTOM_SETTINGS
                        #define INCLUDE_INTERNET_SHIELD
                        #define INCLUDE_GPS_SHIELD
                        #define INCLUDE_TERMINAL_SHIELD
                        #define INCLUDE_CLOCK_SHIELD
                        
                        
                        #include <OneSheeld.h>
                        char CAR_ID[25] = "\"-LSU82U4mAjAfdXBzYQ9\"";
                        char URL[100] = "https://group-project-22afb.firebaseio.com/CARS/-LSU82U4mAjAfdXBzYQ9/position/";
                        int entry = 0;
                        unsigned long currentTime;
                        
                        
                        void setup() {
                          OneSheeld.begin();
                          Clock.queryDateAndTime();
                        
                        }
                         
                        void loop() {
                          OneSheeld.delay(10000);
                          
                          char strLon[20];
                          char strLat[20];
                          
                          dtostrf(GPS.getLongitude(), 4, 6, strLon);
                          dtostrf(GPS.getLatitude(), 4, 6, strLat);

                          char strMin[10];
                          char strHour[10];
                          char strSecond[10];
                 
                          itoa(Clock.getMinutes(), strMin, 10);
                          itoa(Clock.getHours(), strHour, 10);
                          itoa(Clock.getSeconds(), strSecond, 10);
                          
                          char timecode[30] = "";
                          strcat(timecode, strHour); 
                          strcat(strcat(timecode, ":"), strMin); 
                          strcat(strcat(timecode, ":"), strSecond);
                          
                          char data[180] = "{";
                          strcat(strcat(data, "\"longitude\":"), strLon);
                          strcat(strcat(data, ", \"latitude\":"), strLat);
                          strcat(strcat(data, ", \"time\":\""), timecode);
                          strcat(data, "\"}");

                          char newURL[100] = "";
                          
                          char entryBuf[10] = "";
                          itoa(entry, entryBuf, 10);
                          entry = entry + 1;
                          
                          strcat(strcat(newURL, URL), strcat(entryBuf, ".json"));
                           
                          Terminal.println(data);
                          Terminal.println(newURL);
                          
                          HttpRequest request(newURL);
                          request.addRawData(data);
                          Internet.performPut(request);
                          
                        }
