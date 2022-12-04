package com.matheusmarkies.manager;

import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.manager.analysis.SampleAnalysis;
import com.matheusmarkies.objects.ColorStats;
import com.matheusmarkies.objects.Sensor;
import com.matheusmarkies.objects.Transducer;

import java.util.ArrayList;
import java.util.List;

import static com.matheusmarkies.manager.analysis.SampleAnalysis.hardShiftFilter;

public class DataManager {

    private MainFrameController controller;

    public DataManager(MainFrameController controller) {
        this.controller = controller;

        if(controller.getTransducer()!=null)
            this.transducer = controller.getTransducer();
        else
            this.transducer = new Transducer();

        sensorA = new Sensor(transducer);
        sensorB = new Sensor(transducer);
    }

    public enum Colors{
        Blue(0), Red(1), Green(2), None(3);
        Colors(int i) {
        }
    }

    private Transducer transducer;

    Sensor sensorA;
    Sensor sensorB;

    int addingSensorASampleIndex = 0;
    int addingSensorBSampleIndex = 0;

    public boolean setColorStats(int sensorIndex, int color, double value) {
        Sensor sensor = new Sensor();

        switch (sensorIndex) {
            case 0:
                sensor = sensorA;
                addingSensorASampleIndex+=1;
                break;
            case 1:
                sensor = sensorB;
                addingSensorBSampleIndex+=1;
                break;
            default:
                break;
        }
        switch (color) {
            case 0:
                sensor.getColorStats().setBlue(value);
                break;
            case 1:
                sensor.getColorStats().setRed(value);
                break;
            case 2:
                sensor.getColorStats().setGreen(value);
                break;
        }

        if(addingSensorASampleIndex >= 3){
            ColorStats stats = new ColorStats(sensor.getColorStats().getRed(),
                    sensor.getColorStats().getGreen(), sensor.getColorStats().getBlue());
            stats.setPosition(sensor.getSensorPosition());
            //sensor.getSensorSampleHistory().add(SampleAnalysis.hardShiftFilter(sensor.getSensorSampleHistory(), stats,30));
            sensor.getSensorSampleHistory().add(stats);
            addingSensorASampleIndex=0;
            return true;
        }
        if(addingSensorBSampleIndex >= 3){
            ColorStats stats = new ColorStats(sensor.getColorStats().getRed(),
                    sensor.getColorStats().getGreen(), sensor.getColorStats().getBlue());
            stats.setPosition(sensor.getSensorPosition());
            //sensor.getSensorSampleHistory().add(SampleAnalysis.hardShiftFilter(sensor.getSensorSampleHistory(), stats,30));
            sensor.getSensorSampleHistory().add(stats);
            addingSensorBSampleIndex=0;
            return true;
        }
        return false;
    }

    public static class AxleData{
        private double velocity;
        private double angularVelocity;
        private double torque;
        private double addedTime =0;

        public enum RotationDirection{
            Clockwise(1), Counterclockwise (-1), None (0);
            RotationDirection(int i) {
            }
        }

        private RotationDirection rotationDirection = RotationDirection.None;

        public AxleData() {
        }

        public AxleData(double velocity, double angularVelocity, double torque, double addedTime) {
            this.velocity = velocity;
            this.angularVelocity = angularVelocity;
            this.torque = torque;
            this.addedTime = addedTime;
        }

        public double getVelocity() {
            return velocity;
        }

        public void setVelocity(double velocity) {
            this.velocity = velocity;
        }

        public double getAngularVelocity() {
            return angularVelocity;
        }

        public void setAngularVelocity(double angularVelocity) {
            this.angularVelocity = angularVelocity;
        }

        public double getTorque() {
            return torque;
        }

        public void setTorque(double torque) {
            this.torque = torque;
        }

        public double getAddedTime() {
            return addedTime;
        }

        public void setAddedTime(double addedTime) {
            this.addedTime = addedTime;
        }

        public RotationDirection getRotationDirection() {
            return rotationDirection;
        }

        public void setRotationDirection(RotationDirection rotationDirection) {
            this.rotationDirection = rotationDirection;
        }

        @Override
        public String toString() {
            return "AxleData{" +
                    "velocity=" + velocity +
                    ", angularVelocity=" + angularVelocity +
                    ", torque=" + torque +
                    ", addedTime=" + addedTime +
                    ", rotationDirection=" + rotationDirection +
                    '}';
        }
    }

    private List<AxleData> axleDataLIst = new ArrayList<>();

    public void changeSensorStats(int sensorIndex, int colorIndex, double discrepancy){
        switch (sensorIndex){
            case 0:
                //if(sensorA.getStartColor() == Colors.None)
                    //sensorA.setStartColor(getColorByIndex(colorIndex));
                //else {
                    if (addNewSensorData(sensorA, colorIndex, discrepancy)) {
                        //System.out.println(sensorA.getDeltaTime());
                        if (sensorA.getDeltaTime() > 0) {
                            AxleData data = new AxleData();

                            double angularVelocity = getAxleAngularVelocity(sensorA);
                            double velocity = Math.round(
                                    (angularVelocity * (controller.getTransducer().getAxleDiameter() / 200)
                                            * 1000) / 1000);

                            //if(velocity < 2000) {
                            data.setVelocity(velocity);
                            data.setAngularVelocity(angularVelocity);

                            if((sensorA.getOldColor() == Colors.Blue && colorIndex == 1)
                            || (sensorA.getOldColor() == Colors.Red && colorIndex == 2)
                            || (sensorA.getOldColor() == Colors.Green && colorIndex == 0))
                                data.setRotationDirection(AxleData.RotationDirection.Counterclockwise);
                            else
                                data.setRotationDirection(AxleData.RotationDirection.Clockwise);

                            data.setAddedTime(Math.round(sensorA.getGlobalDeltaTime() / 10) / 100);

                            data.setTorque(Double.parseDouble(getAngleTorsion(sensorA, sensorB, data.getRotationDirection())+""));

                            sensorA.setRotationDeltaTime(0);
                            addedSensorA = true;
                            getTorqueStatic();
                            axleDataLIst.add(data);
                            controller.chartRefresh();
                            //}
                        }
                    }
                //}
                break;
            case 1:
                //if(sensorB.getStartColor() == Colors.None)
                    //sensorB.setStartColor(getColorByIndex(colorIndex));
                //else {
                    if(addNewSensorData(sensorB,colorIndex, discrepancy)) {
                        addedSensorB = true;
                        sensorB.setRotationDeltaTime(0);
                        //getTorqueStatic();
                    }
                //}
                break;
            default:
                break;
        }

    }

    boolean addedSensorA = false;
    boolean addedSensorB = false;
    public void getTorqueStatic(){
        if(addedSensorA && addedSensorB) {
            float torqueAngle = Math.abs(sensorA.getRotationAddedTime().getTime() - sensorB.getRotationAddedTime().getTime());
            //System.out.println("torqueAngle: "+((torqueAngle/1000) * axleDataLIst.get(axleDataLIst.size()-1).getAngularVelocity()));
            addedSensorA = false;
            addedSensorB = false;
        }
    }

    public void reset(){
        sensorA.setStartColor(Colors.None);
        sensorB.setStartColor(Colors.None);
        //if(sensorA.getStartColor() == Colors.None)
            //sensorA.setStartColor(sensorA.getCurrentColor());
        //if(sensorB.getStartColor() == Colors.None)
            //sensorB.setStartColor(sensorB.getCurrentColor());
    }

    public static Colors getColorByIndex(int colorIndex){
        switch (colorIndex){
            case 0: return Colors.Blue;
            case 1: return Colors.Red;
            case 2: return Colors.Green;
            default: return Colors.None;
        }
    }

    public boolean addNewSensorData(Sensor sensor,int colorIndex, double discrepancy){
        //if(sensor.getStartColor() == Colors.None)
            //sensor.setStartColor(getColorByIndex(colorIndex));
        sensor.setCurrentColor(getColorByIndex(colorIndex));
        return sensor.pulseRegister(discrepancy);
    }

    public double getAxleAngularVelocity(Sensor sensor){
        double angularVelocity = 0;
        double deltaTime = (sensor.getRotationDeltaTime()/1000);
        if(sensor.getRotationDeltaTime() != 0) {
            //System.out.println("sensor.getRotationDeltaTime(): "+deltaTime + " | deltaTime" +((sensor.getDeltaTime()/1000) * 30));
            //System.out.println((2 * Math.PI)/deltaTime);
            //if(deltaTime > 0)
            //if(sensor.getPulseCounter() > 0)
            //return ((transducer.getTracksNumber().getValue()/(2 * Math.PI)) * sensor.getPulseCounter())/deltaTime;
            //else
            //return (2 * Math.PI)/deltaTime;
            //else
            angularVelocity = (2 * Math.PI) / (sensor.getRotationDeltaTime() / 1000);
        }

        return angularVelocity;
    }
//Red Blue Green
    public double getAngleTorsion(Sensor axleBase, Sensor axleTip, AxleData.RotationDirection direction){
        int delayStartColorTracks = -axleBase.getPulseStartColorCounter()
                + axleTip.getPulseStartColorCounter() - axleTip.getPulseIntermediateColorCounter();
        double discrepancyAngle = Math.abs(axleBase.getDiscrepancy() - axleTip.getDiscrepancy());

        float anglePerDiscrepancyTracks = (360/transducer.getTracksNumber().getValue());// * (transducer.getTracksNumber().getValue()/3);

        anglePerDiscrepancyTracks*=discrepancyAngle;

        float angleOfStartColorTracks = anglePerDiscrepancyTracks;
                //+ Math.abs((360/transducer.getTracksNumber().getValue()) * (delayStartColorTracks / 2));

        System.out.println("anglePerTracks: "+ discrepancyAngle);
        //System.out.println("angleOfStartColorTracks: "+ angleOfStartColorTracks);

        if(direction == AxleData.RotationDirection.Clockwise){

        }else if(direction == AxleData.RotationDirection.Counterclockwise){

        }else{

        }

    return angleOfStartColorTracks;
    }

    public List<AxleData> getAxleDataList() {
        return axleDataLIst;
    }

    public MainFrameController getController() {
        return controller;
    }

    public Sensor getSensorA() {
        return sensorA;
    }

    public void setSensorA(Sensor sensorA) {
        this.sensorA = sensorA;
    }

    public Sensor getSensorB() {
        return sensorB;
    }

    public void setSensorB(Sensor sensorB) {
        this.sensorB = sensorB;
    }
}
