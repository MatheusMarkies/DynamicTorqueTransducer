package com.matheusmarkies.objects;

import java.io.Serializable;

public class Transducer implements Serializable {

    public enum SensorTracks{m_9(9),m_18(18),m_30(30),m_90(90);
        private final int value;
        private SensorTracks(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private double axleDiameter = 19.05;//mm
    private SensorTracks tracksNumber = SensorTracks.m_9;
    private double searModulusOfElasticity = 75.8; //G (GPa)
    private double polarMomentOfInertia = 75.8; //G (GPa)

    public double getResolution(){
        return (double)Math.PI*axleDiameter/tracksNumber.getValue();
    }

    public double getAxleDiameter() {
        return axleDiameter;
    }

    public void setAxleDiameter(double axleDiameter) {
        this.axleDiameter = axleDiameter;
    }

    public SensorTracks getTracksNumber() {
        return tracksNumber;
    }

    public void setTracksNumber(SensorTracks tracksNumber) {
        this.tracksNumber = tracksNumber;
    }
}
