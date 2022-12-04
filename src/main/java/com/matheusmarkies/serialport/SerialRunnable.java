package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.manager.DataManager;
import com.matheusmarkies.manager.analysis.SampleAnalysis;
import com.matheusmarkies.manager.utilities.Vector2D;
import com.matheusmarkies.objects.ColorStats;
import javafx.scene.chart.XYChart;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

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
        None,R,G,B,DT,SC
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

                //System.out.println(readingString);
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
                    case "R:":
                        //System.out.println("R:");
                        readType = ReadType.R;
                        getReadType = false;
                        break;
                    case "G:":
                        //System.out.println("G:");
                        readType = ReadType.G;
                        getReadType = false;
                    break;
                    case "B:":
                        //System.out.println("B:");
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
                                case R:
                                    //System.out.println(readingSensorIndex+" R: "+Double.parseDouble(readingString));
                                        if(controller.getDataManager().setColorStats(readingSensorIndex,1,Double.parseDouble(readingString)))
                                        {
                                            List<ColorStats> colorHistory = controller.getDataManager().getSensorA().getSensorSampleHistory();
                                            ColorStats colorStats = controller.getDataManager().getSensorA().getColorStats();
                                            if(readingSensorIndex == 1) {
                                                colorHistory = controller.getDataManager().getSensorB().getSensorSampleHistory();
                                                colorStats = controller.getDataManager().getSensorB().getColorStats();
                                            }
                                            Vector2D Parameters = SampleAnalysis.getSensorParameters(
                                                   colorHistory,colorStats
                                            );
                                            controller.getDataManager().changeSensorStats(
                                                    readingSensorIndex,(int)Parameters.x(),Parameters.y()
                                                    );
                                        }
                                    getReadType = true;
                                    break;
                                case G:
                                    //System.out.println(readingSensorIndex+" G: "+Double.parseDouble(readingString));
                                    if(controller.getDataManager().setColorStats(readingSensorIndex,2,Double.parseDouble(readingString)))
                                    {
                                        List<ColorStats> colorHistory = controller.getDataManager().getSensorA().getSensorSampleHistory();
                                        ColorStats colorStats = controller.getDataManager().getSensorA().getColorStats();
                                        if(readingSensorIndex == 1) {
                                            colorHistory = controller.getDataManager().getSensorB().getSensorSampleHistory();
                                            colorStats = controller.getDataManager().getSensorB().getColorStats();
                                        }
                                        Vector2D Parameters = SampleAnalysis.getSensorParameters(
                                                colorHistory,colorStats
                                        );
                                        controller.getDataManager().changeSensorStats(
                                                readingSensorIndex,(int)Parameters.x(),Parameters.y()
                                        );
                                    }
                                   getReadType = true;
                                break;
                                case B:
                                    //System.out.println(readingSensorIndex+" B: "+Double.parseDouble(readingString));
                                    if(controller.getDataManager().setColorStats(readingSensorIndex,0,Double.parseDouble(readingString)))
                                    {
                                        List<ColorStats> colorHistory = controller.getDataManager().getSensorA().getSensorSampleHistory();
                                        ColorStats colorStats = controller.getDataManager().getSensorA().getColorStats();
                                        if(readingSensorIndex == 1) {
                                            colorHistory = controller.getDataManager().getSensorB().getSensorSampleHistory();
                                            colorStats = controller.getDataManager().getSensorB().getColorStats();
                                        }
                                        Vector2D Parameters = SampleAnalysis.getSensorParameters(
                                                colorHistory,colorStats
                                        );
                                        controller.getDataManager().changeSensorStats(
                                                readingSensorIndex,(int)Parameters.x(),Parameters.y()
                                        );
                                    }
                                    getReadType = true;
                                    break;
                                case DT:
                                    controller.getDataManager().getSensorA().setDeltaTime(Double.parseDouble(readingString));
                                    if(readingSensorIndex == 1) {
                                        controller.getDataManager().getSensorB().setDeltaTime(Double.parseDouble(readingString));
                                    }
                                    getReadType = true;
                                    break;
                                case SC:
                                    if(controller.getDataManager().getSensorA().getStartColor() == DataManager.Colors.None)
                                    controller.getDataManager().getSensorA().setStartColor(DataManager.getColorByIndex((int)Double.parseDouble(readingString)));
                                    if(readingSensorIndex == 1) {
                                        if(controller.getDataManager().getSensorB().getStartColor() == DataManager.Colors.None)
                                        controller.getDataManager().getSensorB().setStartColor(DataManager.getColorByIndex((int)Double.parseDouble(readingString)));
                                    }
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
