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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSSSS");
        LocalTime localTimeNow = LocalTime.now();

        LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());

        Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

        previousAddedTime = (Date.from(instant));
        startTime = (Date.from(instant));
    }

    public Sensor() {
    //System.err.println("Create new Sensor!");
    }
    boolean lock = false;
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
        if(lock)
        sensorPosition += 360/3;
        //System.out.println("sensorPosition: "+sensorPosition);
        this.lightValue = lightValue;
        this.lightValueHistory.add(lightValue);
        return addPulse();
    }

    int peak = 0;
    int valley = 0;

    public void addPeak(){
        peak ++;
    }
    public void addValley(){
        valley ++;
    }

    public int getPulseCounter() {
        return pulseCounter;
    }

    public void setPulseCounter(int pulseCounter) {
        this.pulseCounter = pulseCounter;
    }

    public boolean addPulse() {

        if (transducer != null) {
            int tracks = 3;//(int)transducer.getTracksNumber().getValue();
            if((pulseCounter >= tracks)) {
                this.pulseCounter = 0;
                sensorPosition = 0;

                LocalTime localTimeNow = LocalTime.now();
                LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());
                Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

                double instatTime = Date.from(instant).getTime();

                setRotationDeltaTime(instatTime-rotationAddedTime.getTime());

                double rpm = (double) Math.round((60000 / getRotationDeltaTime()) * 100) /100;
                System.out.println("Volta Completa " + rpm+" RPM");

                rotationAddedTime = Date.from(instant);
                lock = true;
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