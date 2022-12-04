package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.manager.DataManager;
import com.matheusmarkies.manager.utilities.Vector2D;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChartIntegration implements Runnable{

    private XYChart.Series<String, Double> torqueVSeries = new XYChart.Series<String, Double>();

    private MainFrameController mainFrameController;

    public ChartIntegration(){ }

    public ChartIntegration(MainFrameController mainFrameController){
        this.mainFrameController = mainFrameController;
    }

    @Override
    public void run() {

    }

    boolean seriesAdded = false;

    public void setAnalysisToChart() {
        reset();
        try {
                addEntityToDebugChart();
        }catch (Exception exception){System.err.println("debug "+exception);}
        try {
        torqueVSeries.getData().addAll(addEntityToAverageChart());
       }catch (Exception exception){System.err.println("addEntityToRotationChart "+exception);}
    }

    public void reset() {
        torqueVSeries.getData().clear();
    }

    public Collection<XYChart.Data<String, Double>> addEntityToRotationChart() {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();

            //List<RotationManager.Rotations> averageRotations = SampleAnalysis.averageSampleFilter(
                  //  mainFrameController.getMouseTrapCarManager().getRotationsHistory(), 4
            //);

            //if(dataManager!=null)
            for (DataManager.AxleData axleData : mainFrameController.getDataManager().getAxleDataList()) {
                XYChart.Data data = new XYChart.Data<String, Double>();
                data = new XYChart.Data<String, Double>(axleData.getAddedTime()+ "", (double)axleData.getTorque());
                dataList.add(data);
            }

        return dataList;
    }

    public Collection<XYChart.Data<String, Double>> addEntityToAverageChart() {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();
        List<Vector2D> dataVector = new ArrayList<>();

        for (DataManager.AxleData axleData : mainFrameController.getDataManager().getAxleDataList()) {
            dataVector.add(new Vector2D(axleData.getAddedTime() , axleData.getTorque()));
        }

        List<Vector2D> averageRotations = SampleAnalysis.averageSampleFilter(
                dataVector, 5
        );

        for (Vector2D axleData : averageRotations) {
            XYChart.Data data = new XYChart.Data<String, Double>();
            data = new XYChart.Data<String, Double>(axleData.x()+ "", axleData.y());
            dataList.add(data);
        }

        return dataList;
    }

    public void addEntityToDebugChart() {
        mainFrameController.getSensorDebug().reset();
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Sensor A");

        dataSeries1.getData().add(new XYChart.Data("0", (double) mainFrameController
                .getDataManager().getSensorA().getColorStats().getBlue()));
        dataSeries1.getData().add(new XYChart.Data("1", (double) mainFrameController
                .getDataManager().getSensorA().getColorStats().getRed()));
        dataSeries1.getData().add(new XYChart.Data("2"  , (double) mainFrameController
                .getDataManager().getSensorA().getColorStats().getGreen()));


        mainFrameController.getSensorDebug().getSensor_colorstats_chart().getData().add(dataSeries1);
    }

    public XYChart.Series<String, Double> getTorqueVSeries() {
        return torqueVSeries;
    }

    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

}
