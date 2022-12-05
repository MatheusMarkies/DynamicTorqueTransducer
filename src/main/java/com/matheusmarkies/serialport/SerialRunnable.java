package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.manager.DataManager;
import com.matheusmarkies.manager.analysis.SampleAnalysis;
import com.matheusmarkies.manager.utilities.Vector2D;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class SerialRunnable implements SerialPortPacketListener, Runnable {

    private final SerialPort port;
    private MainFrameController controller;

    public SerialRunnable(SerialPort port, MainFrameController controller) {
        this.controller = controller;
        this.port = port;
    }

    @Override
    public int getPacketSize() {
        return SerialReadder.PACKET_SIZE_IN_BYTES;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void run() {
        port.addDataListener(this);
        //mouseTrapCarManager.getMainFrameController().getRotationSeries().getData().add(new XYChart.Data<String,Double>("0",0.));
    }

    enum ReadType{
        None,B,DT,SC
    }

    ReadType readType = null;
    int readingSensorIndex = 0;
    boolean getReadType = true;
    private final byte[] buffer = new byte[2048];

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
        byte[] buffer = new byte[port.bytesAvailable()];

        String inputString = new String(buffer, StandardCharsets.UTF_16LE);

        Scanner scanner_stream=  new Scanner( port.getInputStream());
        while(scanner_stream.hasNextLine()) {
            String received_string = scanner_stream.nextLine();

            int received_str_len = received_string.length();
            inputString = received_string;

            //System.out.println("Input data: " + inputString);
            String readingString = inputString;

            try {
                try {
                    //for (String a:
                            //readingString.split(" ")) {
                        //System.out.println(a);
                    //}
                    //if (readingString.split(" ").length > 0) {
                        //readingString = readingString.split(" ")[0];

                        //if (readingString.split(" ")[1] == "a")
                            //readingSensorIndex = 0;
                        //else if (readingString.split(" ")[1] == "b")
                            //readingSensorIndex = 1;
                    //}
                }catch (Exception exception){}

                System.out.println(readingString);
                switch (inputString) {

                    case "a":
                        readingSensorIndex = 0;
                        break;
                    case "b":
                        readingSensorIndex = 1;
                        break;

                    case "None:":
                        readType = ReadType.None;
                        getReadType = false;
                        break;
                    case "B:":
                        //System.out.println("R:");
                        readType = ReadType.B;
                        getReadType = false;
                        break;

                    case "DT:":
                        //System.out.println("B:");
                        readType = ReadType.DT;
                        getReadType = false;
                        break;
                    case "SC:":
                        //System.out.println("B:");
                        readType = ReadType.SC;
                        getReadType = false;
                        break;

                    default:
                        if (getReadType) {
                            readType = null;
                        }
                        if (readType != null)
                            switch (readType) {
                                case None:

                                    getReadType = true;
                                    break;
                                case B:

                                   getReadType = true;
                                break;

                                case DT:

                                    getReadType = true;
                                    break;
                                case SC:

                                    getReadType = true;
                                    break;
                            }
                        break;
                }
            }catch (Exception exception){//System.err.println(exception);
          }
            controller.chartRefresh();
        }

    }
    public void changeSensorColorStats(int sensorIndex, int color, double value){
        //controller.getDataManager().changeSensorStats(sensorIndex,color, discrepancy);
    }
    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}
