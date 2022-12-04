package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.manager.DataManager;
import javafx.application.Platform;

public class SerialReadder{

    private SerialPort serialPort;
    private String serialPortName;

    static int PORT_RATE = 9600;
    public static int PACKET_SIZE_IN_BYTES = 8;
    private MainFrameController controller;

    public SerialReadder(String serialPortName, MainFrameController controller) {
        this.controller = controller;
        this.serialPortName = serialPortName;
    }

    public SerialReadder(){
        super();
    }
    private DataManager dataManager;
    public boolean connect(){
        SerialPort[] serialPorts = SerialManager.getSerialPortList();

        for (SerialPort port: serialPorts)
            if(port.getDescriptivePortName().equals(serialPortName)) {
                serialPort = port;
                break;
            }

        if (serialPort.isOpen())
            return false;
        else {
            serialPort.openPort();

            serialPort.setBaudRate(SerialReadder.PORT_RATE);
            Platform.runLater(
                    new SerialRunnable(serialPort,controller)
                );

            return serialPort.isOpen();
        }
    }

    public synchronized void close(){
        if(serialPort.isOpen())
            serialPort.closePort();
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}

