#include "HX711.h"

#define calibration_factor -7050.0 //This value is obtained using the SparkFun_HX711_Calibration sketch
#define MAX_WEIGHT_lb 6.61387
#define toKg 0.00667
#define error_rate 0.1

#define DOUT  3
#define CLK  2

double calibration = 0;
HX711 scale(DOUT, CLK);
void setup()
{
  calibration = 0;
  Serial.begin(9600);
  scale.set_scale(calibration_factor); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale.tare();  //Assuming there is no weight on the scale at start up, reset the scale to 0
}
void loop()
{
  double weight = 0;
  double W = 0;
  int flag = 0;
/*  Serial.print(scale.get_units());
  calibration = scale.get_units();
  Serial.print(" -> ");
  Serial.println(scale.get_units() - calibration);
*/  if (scale.get_units() - calibration>= error_rate){
      flag = 0;
      weight = scale.get_units() - calibration;
      Serial.print(123);
      delay(300);
      weight = scale.get_units() - calibration;
      while(1){
        W = scale.get_units() - calibration;
        if (W <= weight * 0.5) break;
        for (int i = 0; i <= 10 && flag == 0; i++)
          weight = weight * 0.80 + W * 0.20;
        flag = 1;
      }
      weight *= toKg;
      Serial.print("a");
      Serial.print(weight, 4);
      delay(100);
      setup();
  }
}


