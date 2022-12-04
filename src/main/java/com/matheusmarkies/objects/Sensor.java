package com.matheusmarkies.objects;

import com.matheusmarkies.manager.DataManager;

import java.util.ArrayList;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class  Sensor {
    private int pulseCounter;

    private double deltaTime;
    private double globalDeltaTime;
    private double rotationDeltaTime;
    private double lightValue;
    List<Double> lightValueHistory = new ArrayList<>();
    private double sensorPosition;
    public Date previousAddedTime = new Date();
    public Date startTime = new Date();
    private Transducer transducer;

    public Sensor(Transducer transducer) {
        System.err.println("Create new Sensor!");

        this.transducer = transducer;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        LocalTime localTimeNow = LocalTime.now();

        LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());

        Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

        previousAddedTime = (Date.from(instant));
        startTime = (Date.from(instant));
    }

    public Sensor() {
    //System.err.println("Create new Sensor!");
    }

    double oldDiscrepancy = 0;
    public boolean pulseRegister(double lightValue){
        LocalTime localTimeNow = LocalTime.now();

        LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());

        Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

        deltaTime = Date.from(instant).getTime() - previousAddedTime.getTime();
/*
        System.out.println("deltaTime "+ deltaTime);
        System.out.println("Date "+ Date.from(instant).getTime());
        System.out.println("previousAddedTime "+ previousAddedTime.getTime());
        System.out.println();
*/
        globalDeltaTime = Date.from(instant).getTime()-startTime.getTime();
        previousAddedTime = (Date.from(instant));

        this.lightValue = lightValue;
        this.lightValueHistory.add(lightValue);
        return addPulse();
    }

    public int getPulseCounter() {
        return pulseCounter;
    }

    public void setPulseCounter(int pulseCounter) {
        this.pulseCounter = pulseCounter;
    }

    boolean lock = false;

    public boolean addPulse() {

        if (transducer != null) {
            int tracks = (int)transducer.getTracksNumber().getValue();
            if((pulseCounter >= tracks)) {
                this.pulseCounter = 0;
                sensorPosition = 0;
                //System.out.println("Volta Completa " + (getRotationDeltaTime()/1000));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                LocalTime localTimeNow = LocalTime.now();

                LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());

                Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

                rotationAddedTime = Date.from(instant);
                return true;
            }
        }
        return false;
    }
    public Date rotationAddedTime = new Date();
    public double getDeltaTime() {
        return deltaTime;
    }

    public double getGlobalDeltaTime() {
        return globalDeltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public Date getPreviousAddedTime() {
        return previousAddedTime;
    }

    public void setPreviousAddedTime(Date previousAddedTime) {
        this.previousAddedTime = previousAddedTime;
    }

    public double getLightValue() {
        return lightValue;
    }

    public void setLightValue(double lightValue) {
        this.lightValue = lightValue;
    }

    public double getSensorPosition() {
        return sensorPosition;
    }

    public void setSensorPosition(double sensorPosition) {
        this.sensorPosition = sensorPosition;
    }

    public Date getRotationAddedTime() {
        return rotationAddedTime;
    }

    public void setRotationAddedTime(Date rotationAddedTime) {
        this.rotationAddedTime = rotationAddedTime;
    }

    public double getRotationDeltaTime() {
        return rotationDeltaTime;
    }

    public void setRotationDeltaTime(double rotationDeltaTime) {
        this.rotationDeltaTime = rotationDeltaTime;
    }

    public List<Double> getLightValueHistory() {
        return lightValueHistory;
    }

    public void setLightValueHistory(List<Double> lightValueHistory) {
        this.lightValueHistory = lightValueHistory;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                ", pulseCounter=" + pulseCounter +
                ", deltaTime=" + deltaTime +
                ", globalDeltaTime=" + globalDeltaTime +
                ", lightValue=" + lightValue +
                ", previousAddedTime=" + previousAddedTime +
                ", startTime=" + startTime +
                ", transducer=" + transducer +
                '}';
    }
}