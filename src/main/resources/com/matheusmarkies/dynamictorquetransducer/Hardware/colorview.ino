#include <Wire.h>
#include "Adafruit_TCS34725.h"

Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_24MS, TCS34725_GAIN_1X);
Adafruit_TCS34725 tcsB = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_24MS, TCS34725_GAIN_1X);

float const integrationTime = 20;

int motorASpeed = 3;
int motorADir = 0;
int L293D_AA = 4;
int L293D_AB = 5;

int motorBSpeed = 8;
int motorBDir = 0;
int L293D_BA = 9;
int L293D_BB = 10;

#define Aright 44
#define Aleft 45

#define Bright 46
#define Bleft 47

#define potAPin A0
#define potBPin A1

bool sensorAIsSettedStartColor = false;
bool sensorBIsSettedStartColor = false;

int sensorAStartColor = 0;
int sensorBStartColor = 0;

void setup()
{
  Serial.begin(9600);

  pinMode(motorASpeed, OUTPUT);
  pinMode(L293D_AA, OUTPUT);
  pinMode(L293D_AB, OUTPUT);

  pinMode(motorBSpeed, OUTPUT);
  pinMode(L293D_BA, OUTPUT);
  pinMode(L293D_BB, OUTPUT);

  pinMode(Aright, INPUT);
  pinMode(Aleft, INPUT);
  pinMode(Bright, INPUT);
  pinMode(Bleft, INPUT);

  Wire.begin();
}

void loop()
{
  motorController();

  tcaSelect(7);
  if (tcs.begin())
  {
    printSensor("a", tcs);
  }

  tcaSelect(4);
  if (tcsB.begin())
  {
    printSensor("b", tcsB);
  }
}
int speedA = 0;
int speedB = 0;
void motorController() {

  if (analogRead(potAPin) < 512) {
    speedA = map(speedA, 512, 0, 850, 1023);
    motorADir = 1;
  } else {
    speedA = map(speedA, 512, 1023, 850, 1023);
    motorADir = 0;
  }
  analogWrite(motorASpeed, speedA);
  digitalWrite(L293D_AA, 1 - motorADir);
  digitalWrite(L293D_AB, motorADir);

  if (analogRead(potBPin) < 512) {
    speedB = map(speedB, 512, 0, 850, 1023);
    motorBDir = 1;
  } else {
    speedB = map(speedB, 512, 1023, 850, 1023);
    motorBDir = 0;
  }

  analogWrite(motorBSpeed, speedB);
  digitalWrite(L293D_BA, 1 - motorBDir);
  digitalWrite(L293D_BB, motorBDir);
}

void printSensor(String sensor, Adafruit_TCS34725 tcs) {
  uint16_t red, green, blue, c;
  tcs.getRawData(&red, &green, &blue, &c);

  static uint32_t prev_ms = millis();

  uint32_t sum = c;
  float r, g, b;
  r = red; r /= sum;
  g = green; g /= sum;
  b = blue; b /= sum;
  r *= 256; g *= 256; b *= 256;

  int startColor = 0;

  if (sensor == "a") {
    if (!sensorAIsSettedStartColor) {
      sensorAStartColor = getColor(r, g, b);
      startColor = sensorAStartColor;
      sensorAIsSettedStartColor = true;
    } else
      startColor = sensorAStartColor;
  } else if (sensor == "b")
    if (!sensorBIsSettedStartColor) {
      sensorBStartColor = getColor(r, g, b);
      startColor = sensorBStartColor;
      sensorBIsSettedStartColor = true;
    }
    else
      startColor = sensorBStartColor;

  Serial.println(sensor);
  Serial.println("SC:");
  Serial.println(startColor);
  Serial.println("DT:");
  Serial.println((millis() - prev_ms));
  Serial.println("R:");
  Serial.println(r);
  Serial.println("G:");
  Serial.println(g);
  Serial.println("B:");
  Serial.println(b);

  prev_ms = millis();
}

void tcaSelect(uint8_t addr)
{
  Wire.beginTransmission(0x70);
  Wire.write(1 << addr);
  Wire.endTransmission();
}

int getColor(float redValue, float greenValue, float blueValue)
{
  if (redValue > greenValue && redValue > blueValue)
    return 1;
  if (greenValue > redValue && greenValue > blueValue)
    return 2;
  if (blueValue > redValue && blueValue > greenValue)
    return 0;
}