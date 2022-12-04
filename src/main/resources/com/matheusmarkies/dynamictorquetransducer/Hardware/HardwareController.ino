#include <Wire.h>
#include "Adafruit_TCS34725.h"

Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_24MS, TCS34725_GAIN_1X);
Adafruit_TCS34725 tcsB = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_24MS, TCS34725_GAIN_1X);

#define tcaSensorAChannel 4
#define tcaSensorBChannel 7

int motorASpeed = 3;
int L293D_AA = 4;
int L293D_AB = 5;

int motorBSpeed = 8;
int L293D_BA = 9;
int L293D_BB = 10;

#define potAPin A0
#define potBPin A1

void setup()
{
  Serial.begin(9600);

  pinMode(motorASpeed, OUTPUT);
  pinMode(L293D_AA, OUTPUT);
  pinMode(L293D_AB, OUTPUT);

  pinMode(motorBSpeed, OUTPUT);
  pinMode(L293D_BA, OUTPUT);
  pinMode(L293D_BB, OUTPUT);

  Wire.begin();

  tcaSelect(tcaSensorAChannel);
  if (tcs.begin())
    Serial.println("Sensor A encontrado");

  tcaSelect(tcaSensorBChannel);
  if (tcsB.begin())
    Serial.println("Sensor B encontrado");
}

void loop()
{
    motorController();

    tcaSelect(tcaSensorAChannel);
      if (tcs.begin())
        printSensor("a", tcs);

    tcaSelect(tcaSensorBChannel);
      if (tcsB.begin())
        printSensor("b", tcsB);
}

void motorController() {
  int speedA = analogRead(potAPin);
  int motorADirection = 0;

  if(analogRead(potAPin) < 850){
     motorADirection = 1;
     speedA = map(speedA, 0, 850, 1023, 0);
  }else
    speedA = map(speedA, 850, 1023, 0, 1023);

  int speedB = analogRead(potBPin);
  int motorBDirection = 0;

  if(analogRead(potBPin) < 850){
    motorBDirection = 1;
    speedB = map(speedB, 0, 850, 1023, 0);
  }else
    speedB = map(speedB, 850, 1023, 0, 1023);

  analogWrite(motorASpeed, speedA);
  digitalWrite(L293D_AA, motorADirection);
  digitalWrite(L293D_AB, 1 - motorADirection);
  analogWrite(motorBSpeed, speedB);
  digitalWrite(L293D_BA, motorBDirection);
  digitalWrite(L293D_BB, 1 - motorBDirection);
}

void printSensor(String sensor, Adafruit_TCS34725 tcs) {
  float r, g, b;

  static uint32_t prev_ms = millis();

  uint16_t red, green, blue, c;

  tcs.getRawData(&red, &green, &blue, &c);

  float lux = c;

  r = red/lux;
  g = green/lux;
  b = blue/lux;
  r *= 256; g *= 256; b *= 256;

  Serial.println(sensor);
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