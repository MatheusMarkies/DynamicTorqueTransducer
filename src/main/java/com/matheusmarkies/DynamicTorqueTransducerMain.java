package com.matheusmarkies;

import com.matheusmarkies.controllers.MainFrameController;
import com.matheusmarkies.objects.Transducer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class DynamicTorqueTransducerMain extends Application {
    static Scene scene;
    private Transducer transducer = new Transducer();

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        FXMLLoader fxmlMain = new FXMLLoader(Main.class.getResource(
                "/com/matheusmarkies/dynamictorquetransducer/MainFrame.fxml"));

        Parent root = fxmlMain.load();

        Scene scene = new Scene(root);

        MainFrameController controller = fxmlMain.getController();
        controller.setTransducer(transducer);

        stage.setTitle("Dynamic Torque Transducer");

        scene.getStylesheets().add("/com/matheusmarkies/mousetrapcar/MainFrameCSS.css");

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getScene() {
        return scene;
    }
}
