//#define SensorA A0
//#define SensorB A1
#define SensorA 2
#define SensorB 3

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

#define potAPin A2
#define potBPin A3

void setup()
{
  Serial.begin(9600);

  pinMode(motorASpeed, OUTPUT);
  pinMode(L293D_AA, OUTPUT);
  pinMode(L293D_AB, OUTPUT);

  pinMode(motorBSpeed, OUTPUT);
  pinMode(L293D_BA, OUTPUT);
  pinMode(L293D_BB, OUTPUT);

  pinMode(SensorA, INPUT);
  pinMode(SensorB, INPUT);

  attachInterrupt(digitalPinToInterrupt(SensorA), attachInterruptSensorA, CHANGE);
  attachInterrupt(digitalPinToInterrupt(SensorB), attachInterruptSensorB, CHANGE);

  pinMode(Aright, INPUT);
  pinMode(Aleft, INPUT);
  pinMode(Bright, INPUT);
  pinMode(Bleft, INPUT);

  Wire.begin();
}

void loop()
{
  motorController();
  float a,b;
  //a=analogRead(SensorA); 
  //b=analogRead(SensorB); 
  //printSensor("a",a);
  //printSensor("b",b);
}

void attachInterruptSensorA(){
    a=analogRead(SensorA); 
    printSensor("a",a);
}
void attachInterruptSensorB(){
    b=analogRead(SensorB); 
    printSensor("b",b);
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

void printSensor(String sensor, float value) {
  static uint32_t prev_ms = micros();
  Serial.println(sensor);
  Serial.println("SC:");
  Serial.println(startColor);
  Serial.println("DT:");
  Serial.println((micros() - prev_ms));
  Serial.println("B:");
  Serial.println(value);

  prev_ms = millis();
}
