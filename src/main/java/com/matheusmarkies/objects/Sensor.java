package com.matheusmarkies.objects;

import com.matheusmarkies.manager.DataManager;

import java.util.ArrayList;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Sensor {

    private DataManager.Colors startColor = DataManager.Colors.None;
    private DataManager.Colors currentColor = DataManager.Colors.None;
    private DataManager.Colors oldColor = DataManager.Colors.None;

    private int pulseStartColorCounter;
    private int pulseIntermediateColorCounter;
    private int pulseCounter;

    private double deltaTime;
    private double rotationDeltaTime;
    private double globalDeltaTime;
    private double discrepancy;

    private double sensorPosition;

    public Date previousAddedTime = new Date();
    public Date startTime = new Date();

    public ColorStats colorStats = new ColorStats();
    private List<ColorStats> sensorSampleHistory = new ArrayList<>();

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
    public boolean pulseRegister(double discrepancy){
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
        rotationDeltaTime += deltaTime;
        globalDeltaTime = Date.from(instant).getTime()-startTime.getTime();
        previousAddedTime = (Date.from(instant));

        oldDiscrepancy = this.discrepancy;
        this.discrepancy = discrepancy;
        return addPulse();
    }

    public DataManager.Colors getStartColor() {
        return startColor;
    }

    public void setStartColor(DataManager.Colors startColor) {
        this.startColor = startColor;
    }

    public DataManager.Colors getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(DataManager.Colors currentColor) {
        this.oldColor = this.currentColor;
        this.currentColor = currentColor;
    }

    public int getPulseCounter() {
        return pulseCounter;
    }

    public void setPulseCounter(int pulseCounter) {
        this.pulseCounter = pulseCounter;
    }

    boolean lock = false;

    public boolean addPulse() {
        if (this.oldColor != this.currentColor){
            this.pulseIntermediateColorCounter++;
            this.pulseCounter++;
            sensorPosition += 360/transducer.getTracksNumber().getValue();
            if (this.currentColor == this.startColor && !lock) {
                this.pulseIntermediateColorCounter = 0;
                this.pulseStartColorCounter++;
                //sensorPosition = 0;
                lock = true;
            }else if(this.currentColor != this.startColor)
                lock = false;
        }
            // else {
            //setRotationDeltaTime(0);
            //this.pulseIntermediateColorCounter = 0;
            //this.pulseStartColorCounter = 0;
            //this.pulseCounter = 0;
        //}
        if(sensorPosition >= 360)
            sensorPosition=0;
        //System.out.println(pulseCounter);
        if (transducer != null) {
            int tracks = (int)transducer.getTracksNumber().getValue()/3;
            if((pulseStartColorCounter >= tracks)) {
                this.pulseCounter = 0;
                this.pulseStartColorCounter = 0;
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

    public double getDiscrepancy() {
        return discrepancy;
    }

    public void setDiscrepancy(double discrepancy) {
        this.discrepancy = discrepancy;
    }

    public double getRotationDeltaTime() {
        return rotationDeltaTime;
    }

    public void setRotationDeltaTime(double rotationDeltaTime) {
        this.rotationDeltaTime = rotationDeltaTime;
    }

    public ColorStats getColorStats() {
        return colorStats;
    }

    public void setColorStats(ColorStats colorStats) {
        this.colorStats = colorStats;
    }

    public List<ColorStats> getSensorSampleHistory() {
        return sensorSampleHistory;
    }

    public DataManager.Colors getOldColor() {
        return oldColor;
    }

    public void setOldColor(DataManager.Colors oldColor) {
        this.oldColor = oldColor;
    }

    public void setSensorSampleHistory(List<ColorStats> sensorSampleHistory) {
        this.sensorSampleHistory = sensorSampleHistory;
    }

    public int getPulseStartColorCounter() {
        return pulseStartColorCounter;
    }

    public void setPulseStartColorCounter(int pulseStartColorCounter) {
        this.pulseStartColorCounter = pulseStartColorCounter;
    }

    public int getPulseIntermediateColorCounter() {
        return pulseIntermediateColorCounter;
    }

    public void setPulseIntermediateColorCounter(int pulseIntermediateColorCounter) {
        this.pulseIntermediateColorCounter = pulseIntermediateColorCounter;
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

    @Override
    public String toString() {
        return "Sensor{" +
                "startColor=" + startColor +
                ", currentColor=" + currentColor +
                ", pulseCounter=" + pulseCounter +
                ", deltaTime=" + deltaTime +
                ", rotationDeltaTime=" + rotationDeltaTime +
                ", globalDeltaTime=" + globalDeltaTime +
                ", discrepancy=" + discrepancy +
                ", previousAddedTime=" + previousAddedTime +
                ", colorStats=" + colorStats.toString() +
                ", startTime=" + startTime +
                ", transducer=" + transducer +
                '}';
    }
}