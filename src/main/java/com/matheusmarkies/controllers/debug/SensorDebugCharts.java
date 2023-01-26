package com.matheusmarkies.controllers.debug;

import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.manager.analysis.SampleAnalysis;
import com.matheusmarkies.manager.utilities.Vector2D;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.matheusmarkies.manager.analysis.SampleAnalysis.getSensorMaxValue;

public class SensorDebugCharts implements Initializable {

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();

    @FXML
    private AreaChart<String, Double> change_rate_areachart;

    @FXML
    private LineChart<String, Double> change_rate_linechart;

    @FXML
    private ChoiceBox<String> chart_selector;

    private String debugSensor = "";
    private MainFrameController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chart_selector.getItems().add("Line");
        chart_selector.getItems().add("Area");
        chart_selector.getItems().add("Line X Angle");

        change_rate_areachart.setVisible(false);
        change_rate_areachart.setAnimated(false);
        change_rate_linechart.setVisible(false);
        change_rate_linechart.setAnimated(false);

        change_rate_areachart.setCreateSymbols(false);
        change_rate_linechart.setCreateSymbols(false);

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Azul");
        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Vermelho");
        XYChart.Series dataSeries3 = new XYChart.Series();
        dataSeries3.setName("Verde");

        chart_selector.setOnAction(event -> {

            if(chart_selector.getValue().equals("Line")){
                change_rate_areachart.setVisible(false);
                change_rate_linechart.setVisible(true);
            }else if(chart_selector.getValue().equals("Line X Angle")){
                change_rate_areachart.setVisible(false);
                change_rate_linechart.setVisible(true);
            }else
            {
                change_rate_areachart.setVisible(true);
                change_rate_linechart.setVisible(false);
            }

            ScheduledExecutorService scheduledExecutorService;
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

            scheduledExecutorService.scheduleAtFixedRate(() -> {
                Platform.runLater(() -> {
                    reset();
                    int chartRange = 100;
                        XYChart.Series<String, Double> blueSeries = new XYChart.Series<String, Double>();
                        blueSeries.getData().addAll(addEntityBlueToChart(true,chartRange));
                        XYChart.Series<String, Double> redSeries = new XYChart.Series<String, Double>();
                        redSeries.getData().addAll(addEntityRedToChart(true,chartRange));


                        change_rate_linechart.getData().addAll(blueSeries);
                        change_rate_linechart.getData().addAll(redSeries);
                });
            }, 0, 1, TimeUnit.MILLISECONDS);

        });
    }

    public Collection<XYChart.Data<String, Double>> addEntityRedToChart(boolean A,int size) {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();
        int historySize = controller.getDataManager().getSensorA().getLightValueHistory().size();
        int startChart = Math.max(historySize-size,0);
        double maxValue = getSensorMaxValue(controller.getDataManager().getSensorA().getLightValueHistory(), 10);
        List<Vector2D> vectorList = new ArrayList<>();
        for (int i = startChart;i< historySize;i++) {
            Vector2D
                    data = new Vector2D(i
                    , controller.getDataManager().getSensorA().getLightValueHistory().get(i)/maxValue);
            vectorList.add(data);
        }

        //vectorList = SampleAnalysis.getReorderedList(vectorList);

        for (Vector2D vec : vectorList) {
            dataList.add(new XYChart.Data("" + vec.x(), (double) vec.y()));
        }

        return dataList;
    }

    public Collection<XYChart.Data<String, Double>> addEntityBlueToChart(boolean A,int size) {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();
        int historySize = controller.getDataManager().getSensorB().getLightValueHistory().size();
        int startChart = Math.max(historySize-size,0);

        List<Vector2D> vectorList = new ArrayList<>();
        for (int i = startChart;i< historySize;i++) {

            Vector2D
                    data = new Vector2D(i
                    , controller.getDataManager().getSensorB().getLightValueHistory().get(i));
            vectorList.add(data);

        }

        //vectorList = SampleAnalysis.getReorderedList(vectorList);

        for (Vector2D vec : vectorList) {
            dataList.add(new XYChart.Data("" + vec.x(), (double) vec.y()));
        }

        return dataList;
    }

    public void reset() {
        change_rate_areachart.getData().clear();
        change_rate_linechart.getData().clear();
    }

    public void setController(MainFrameController controller) {
        this.controller = controller;
    }

    public MainFrameController getController() {
        return controller;
    }

    public void setDebugSensor(String debugSensor) {
        this.debugSensor = debugSensor;
    }
}
