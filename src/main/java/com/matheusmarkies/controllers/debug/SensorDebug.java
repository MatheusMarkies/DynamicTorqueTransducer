package com.matheusmarkies.controllers.debug;

import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.manager.DataManager;
import com.matheusmarkies.manager.analysis.SampleAnalysis;
import com.matheusmarkies.manager.utilities.Vector3D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class SensorDebug implements Initializable {

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();

    @FXML
    private BarChart<String, Double> sensor_colorstats_chart = new BarChart(xAxis, yAxis);

    @FXML
    private ChoiceBox<String> sensors_chosebox;

    private MainFrameController controller;

    @FXML
    private Pane debug_composition;

    @FXML
    private Label sensor_info;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sensor_info.setText("");
        sensors_chosebox.getItems().add("Primario");
        sensors_chosebox.getItems().add("Secundario");
        sensors_chosebox.getItems().add("Sobreposicao");

        sensors_chosebox.setValue("Sobreposicao");

        sensor_colorstats_chart.setAnimated(false);

        //chartManagementSchedule();
        sensors_chosebox.setOnAction(event -> {
            ScheduledExecutorService scheduledExecutorService;
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

            scheduledExecutorService.scheduleAtFixedRate(() -> {
                Platform.runLater(() -> {
                    chartManagementSchedule();
                });
            }, 0, 20, TimeUnit.MILLISECONDS);
        });

    }

    void chartManagementSchedule(){
        reset();

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Azul");
        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Vermelho");
        XYChart.Series dataSeries3 = new XYChart.Series();
        dataSeries3.setName("Verde");

        if(sensors_chosebox.getValue().equals("Primario")) {
            dataSeries1.getData().add(new XYChart.Data("", (double) controller
                    .getDataManager().getSensorA().getColorStats().getBlue()));
            dataSeries2.getData().add(new XYChart.Data("", (double) controller
                    .getDataManager().getSensorA().getColorStats().getRed()));
            dataSeries3.getData().add(new XYChart.Data("", (double) controller
                    .getDataManager().getSensorA().getColorStats().getGreen()));
        }else if(sensors_chosebox.getValue().equals("Secundario")) {
            dataSeries1.getData().add(new XYChart.Data("", (double) controller
                    .getDataManager().getSensorB().getColorStats().getBlue()));
            dataSeries2.getData().add(new XYChart.Data("", (double) controller
                    .getDataManager().getSensorB().getColorStats().getRed()));
            dataSeries3.getData().add(new XYChart.Data("", (double) controller
                    .getDataManager().getSensorB().getColorStats().getGreen()));
        }else{
            dataSeries1.getData().add(new XYChart.Data("p", (double) controller
                    .getDataManager().getSensorA().getColorStats().getBlue()));
            dataSeries2.getData().add(new XYChart.Data("p", (double) controller
                    .getDataManager().getSensorA().getColorStats().getRed()));
            dataSeries3.getData().add(new XYChart.Data("p", (double) controller
                    .getDataManager().getSensorA().getColorStats().getGreen()));
            dataSeries1.getData().add(new XYChart.Data("s", (double) controller
                    .getDataManager().getSensorB().getColorStats().getBlue()));
            dataSeries2.getData().add(new XYChart.Data("s", (double) controller
                    .getDataManager().getSensorB().getColorStats().getRed()));
            dataSeries3.getData().add(new XYChart.Data("s", (double) controller
                    .getDataManager().getSensorB().getColorStats().getGreen()));
        }

        String sensorInfo = "Sensor A" + "\n" + "Pulse: " +
                controller
                        .getDataManager().getSensorA().getPulseCounter()
                + "\n" + "Current Color: " + controller
                .getDataManager().getSensorA().getCurrentColor()
                + "\n" + "Delta Time: " + controller
                .getDataManager().getSensorA().getDeltaTime()
                + "\n" +  "Start Color: " + controller
                .getDataManager().getSensorA().getStartColor()
                + "\n" + "Discrepancia: " + Math.round(controller
                .getDataManager().getSensorA().getDiscrepancy() * 100)+ "\n"
                + "\n" + "Sensor B" + "\n" + "Pulse: " +
                controller
                        .getDataManager().getSensorB().getPulseCounter()
                + "\n" + "Current Color: " + controller
                .getDataManager().getSensorB().getCurrentColor()
                + "\n" + "Delta Time: " + controller
                .getDataManager().getSensorB().getDeltaTime()
                + "\n" +  "Start Color: " + controller
                .getDataManager().getSensorB().getStartColor()
                + "\n" + "Discrepancia: " + Math.round(controller
                .getDataManager().getSensorB().getDiscrepancy() * 100)+ "\n";

        String axleInfo = "";

        int dataSize = controller
                .getDataManager().getAxleDataList().size() -1;

        if(dataSize > 0) {
            DataManager.AxleData axle = controller
                    .getDataManager().getAxleDataList().get(dataSize);

            axleInfo = "Axle"
                    + "\n" + "Torque: " + (Math.floor(axle.getTorque()*1000)/1000) +"º"
                    + "\n" + "Velocity: " + (Math.floor(axle.getVelocity()*100)/100)
                    + "\n" + "Angular Velocity: " + (Math.floor(axle.getAngularVelocity()*100)/100) +"rad/s"
                    + "\n" + "Direction: " + axle.getRotationDirection();
        }

        sensor_info.setText(sensorInfo + axleInfo);

        sensor_colorstats_chart.getData().add(dataSeries1);
        sensor_colorstats_chart.getData().add(dataSeries2);
        sensor_colorstats_chart.getData().add(dataSeries3);
    }

    @FXML
    void onClickInMoreChartsButton(ActionEvent event) {
        openDebugChartsWindow();
    }

    void openDebugChartsWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/com/matheusmarkies/dynamictorquetransducer/SensorDebugCharts.fxml"));
            Parent root = fxmlLoader.load();

            //fxmlLoader.setController(sensorDebug);
            SensorDebugCharts sensorDebug = (SensorDebugCharts)fxmlLoader.getController();

            sensorDebug.setController(controller);
            sensorDebug.setDebugSensor(sensors_chosebox.getValue());

            String css = this.getClass()
                    .getResource("/com/matheusmarkies/dynamictorquetransducer/SensorDebugCharts.css").toExternalForm();
            root.getStylesheets().add(css);

            Stage stage = new Stage();
            stage.setTitle("Debug");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException ignored) {
            System.err.println(ignored);
        }
    }

    @FXML
    void onClickInResetStartColorButton(ActionEvent event) {
        controller.getDataManager().reset();
    }

    public void reset() {
        sensor_colorstats_chart.getData().clear();
    }

    public void setController(MainFrameController controller) {
        this.controller = controller;
    }

    public MainFrameController getController() {
        return controller;
    }

    public BarChart<String, Double> getSensor_colorstats_chart() {
        return sensor_colorstats_chart;
    }

    public void setSensor_colorstats_chart(BarChart<String, Double> sensor_colorstats_chart) {
        this.sensor_colorstats_chart = sensor_colorstats_chart;
    }

    public ChoiceBox<String> getSensors_chosebox() {
        return sensors_chosebox;
    }

    public void setSensors_chosebox(ChoiceBox<String> sensors_chosebox) {
        this.sensors_chosebox = sensors_chosebox;
    }
}
