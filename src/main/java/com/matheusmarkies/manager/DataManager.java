package com.matheusmarkies.manager;

import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.objects.Sensor;
import com.matheusmarkies.objects.Transducer;

import java.util.ArrayList;
import java.util.List;

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

    private Transducer transducer;

    Sensor sensorA;
    Sensor sensorB;

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

    private List<AxleData> axleDataHistory = new ArrayList<>();

    public void changeSensorStats(int sensorIndex, int colorIndex, double discrepancy){
        switch (sensorIndex){
            case 0:

                break;
            case 1:

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

    public boolean addNewSensorData(Sensor sensor, double discrepancy){
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
        if(direction == AxleData.RotationDirection.Clockwise){

        }else if(direction == AxleData.RotationDirection.Counterclockwise){

        }else{

        }

    return 0;
    }

    public List<AxleData> getAxleDataHistory() {
        return axleDataHistory;
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
