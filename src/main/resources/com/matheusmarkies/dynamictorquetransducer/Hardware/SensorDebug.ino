#include <Wire.h>
#include "Adafruit_TCS34725.h"

/* Inicializa com os valores padrÃµes(int time = 2.4ms, gain = 1x) */
// Adafruit_TCS34725 tcs = Adafruit_TCS34725();

/* Initialise with specific int time and gain values */
Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_614MS, TCS34725_GAIN_1X);

int motorASpeed = 3;
int L293D_2 = 4;
int L293D_7 = 5;

int potPin = 0;
int Aright = 6;
int Aleft = 7;

void setup()
{
  Serial.begin(9600);

  pinMode(motorASpeed, OUTPUT);
  pinMode(L293D_2, OUTPUT);
  pinMode(L293D_7, OUTPUT);

  pinMode(Aright, INPUT);
  pinMode(Aleft, INPUT);

  //if (tcs.begin())//Se conseguirmos iniciar o sensor significa que ele estÃ¡ conectado
  //{
  //  Serial.println("Sensor encontrado");
  //}
  ////caso contrÃ¡rio ele nao estÃ¡ conectado
  //{
   // Serial.println("Sensor nÃ£o encontrado, cheque suas conecÃ§Ãµes.");
   // while (1);
  //}

}

void loop()
{
  uint16_t r, g, b, c;

int velocidade = analogRead(potPin);
  analogWrite(motorASpeed, velocidade);
  digitalWrite(L293D_2, digitalRead(Aright));
  digitalWrite(L293D_7, digitalRead(Aleft));   Serial.println(velocidade);
  Serial.println(digitalRead(Aright));
  Serial.println(digitalRead(Aleft));
  //tcs.getRawData(&r, &g, &b, &c);//Pega os valores "crus" do sensor referentes ao Vermelho(r), Verde(g), Azul(b) e da Claridade(c).

  //Agora vamos printar Os valores referentes a cada cor.
  //Serial.print("Vermelho : "); Serial.print(r, DEC); Serial.print(" ");
  //Serial.print("Verde    : "); Serial.print(g, DEC); Serial.print(" ");
  //Serial.print("Azul     : "); Serial.print(b, DEC); Serial.print(" ");
  //Serial.print("Claridade: "); Serial.print(c, DEC); Serial.print(" ");
  //Serial.println(" ");

    //Serial.println("R:");
    //Serial.println(r/100);
    //Serial.println("G:");
    //Serial.println(g/100);
    //Serial.println("B:");
    //Serial.println(b/100);
}