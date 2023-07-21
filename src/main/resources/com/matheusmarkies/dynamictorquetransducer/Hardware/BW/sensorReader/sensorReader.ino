#include<ADS1115_WE.h>
#include<Wire.h>
#define I2C_ADDRESS 0x48

ADS1115_WE adc = ADS1115_WE(I2C_ADDRESS);

#include <FIR.h>

FIR<float, 8> fir;

void setup() {
  Wire.begin();
  Serial.begin(115200);
  if(!adc.init()){
    Serial.println("ADS1115 not connected!");
  }

  adc.setVoltageRange_mV(ADS1115_RANGE_6144); //comment line/change parameter to change range

  adc.setCompareChannels(ADS1115_COMP_0_GND); //comment line/change parameter to change channel

  adc.setMeasureMode(ADS1115_CONTINUOUS); //comment line/change parameter to change mode

  float coef[8] = { 1., 1., 1., 1., 1., 1., 1., 1.};

  // Set the coefficients
  fir.setFilterCoeffs(coef);

  Serial.print("Gain set: ");
  Serial.println(fir.getGain());

  Serial.println("ADS1115 Example Sketch - Continuous Mode");
  Serial.println("All values in volts");
  Serial.println();
}

void loop() {
  float voltage = 0.0;
  voltage = readChannel(ADS1115_COMP_1_GND);
  float pp = fir.processReading(voltage);
  Serial.println("B:");
  Serial.println(voltage,2);
}

float readChannel(ADS1115_MUX channel) {
  float voltage = 0.0;
  adc.setCompareChannels(channel);
  voltage = adc.getResult_V();
  return voltage;
}