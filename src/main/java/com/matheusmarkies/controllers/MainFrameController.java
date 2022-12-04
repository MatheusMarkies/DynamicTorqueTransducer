package com.matheusmarkies.controllers;

import com.matheusmarkies.controllers.debug.SensorDebug;
import com.matheusmarkies.controllers.popup.ConnectPopUpController;
import com.matheusmarkies.manager.DataManager;
import com.matheusmarkies.manager.analysis.ChartIntegration;
import com.matheusmarkies.objects.Transducer;
import com.matheusmarkies.serialport.SerialReadder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainFrameController implements Initializable {

    @FXML
    private MenuItem connect_menu_button;

    @FXML
    private MenuBar menu_bar;

    @FXML
    private LineChart<String, Double> torque_velocity_chart;

    private SerialReadder serialReadder;
    private Transducer transducer;
    private DataManager dataManager;
    private ChartIntegration chartIntegration = new ChartIntegration();

    @FXML
    void onClickInConnectButton(ActionEvent event) {
        openConnectPopUp();
    }

    @FXML
    void onClickInDebugButton(ActionEvent event) { openDebugWindow(); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serialReadder = new SerialReadder();
        dataManager = new DataManager(this);
        serialReadder.setDataManager(dataManager);
        chartIntegration = new ChartIntegration(this);

        chartIntegration.getTorqueVSeries().setName("Torque");

        torque_velocity_chart.getXAxis().setLabel("Velocidade");
        torque_velocity_chart.getYAxis().setLabel("Torque");

        torque_velocity_chart.getData().addAll(chartIntegration.getTorqueVSeries());

        torque_velocity_chart.setCreateSymbols(false);

        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                if(getDataManager().getSensorA().getSensorSampleHistory().size() > 0)
                chartIntegration.setAnalysisToChart();
            });
        }, 0, 20, TimeUnit.MILLISECONDS);

    }

    void openConnectPopUp(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/com/matheusmarkies/dynamictorquetransducer/ConnectPopUp.fxml"));
            Parent root = fxmlLoader.load();

            ConnectPopUpController connectPopUpController = (ConnectPopUpController)fxmlLoader.getController();

            connectPopUpController.setController(this);
            connectPopUpController.setSerialReadder(this.serialReadder);

            Stage stage = new Stage();
            stage.setTitle("Conectar");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException ignored) {
            System.err.println(ignored);
        }
    }

    private SensorDebug sensorDebug = new SensorDebug();

    void openDebugWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/com/matheusmarkies/dynamictorquetransducer/SensorDebug.fxml"));
            Parent root = fxmlLoader.load();

            //fxmlLoader.setController(sensorDebug);
            SensorDebug sensorDebug = (SensorDebug)fxmlLoader.getController();

            sensorDebug.setController(this);

            String css = this.getClass()
                    .getResource("/com/matheusmarkies/dynamictorquetransducer/SensorDebug.css").toExternalForm();
            root.getStylesheets().add(css);

            Stage stage = new Stage();
            stage.setTitle("Debug");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException ignored) {
            System.err.println(ignored);
        }
    }

    public void chartRefresh(){
        //chartIntegration.setAnalysisToChart();
    }

    public Transducer getTransducer() {
        return transducer;
    }

    public void setTransducer(Transducer transducer) {
        this.transducer = transducer;
    }

    public SerialReadder getSerialReadder() {
        return serialReadder;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public SensorDebug getSensorDebug() {
        return sensorDebug;
    }

    public void setSensorDebug(SensorDebug sensorDebug) {
        this.sensorDebug = sensorDebug;
    }
}
