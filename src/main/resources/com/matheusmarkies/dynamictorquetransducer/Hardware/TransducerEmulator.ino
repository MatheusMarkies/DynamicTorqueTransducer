enum Color{
  Red,Green,Blue,None
};

Color currentColorA = None;
Color currentColorB = None;

Color startColorA = None;
Color startColorB = None;
//Blue Red Green
int counter;

void setup()
{
Serial.begin(9600);

startColorA = Red;
startColorB = Blue;

 Serial.println("A:");
 Serial.println(startColorA);

 Serial.println("B:");
 Serial.println(startColorB);

pinMode(2, INPUT);
pinMode(3, INPUT);
}

int colorCounterA =0;
int colorCounterB =0;
void loop()
{
int valA = digitalRead(2);
int valB = digitalRead(3);

if(valA == 1)
{
colorCounterA++;
if(colorCounterA >= 3)
colorCounterA =0;
}

if(valB == 1)
{
colorCounterB++;
if(colorCounterB >= 3)
colorCounterB =0;
}
 currentColorA = getColor(colorCounterA);
 currentColorB = getColor(colorCounterB);

if(valA == 1){
 Serial.println("A:");
 Serial.println(currentColorA);
}
if(valB == 1){
 Serial.println("B:");
 Serial.println(currentColorB);
}
}

Color getColor(int colorCounter){
  switch(colorCounter){
        case 0: return Blue;
    break;
        case 1: return Red;
    break;
        case 2: return Green;
    break;
    default: return None; break;
  }

}