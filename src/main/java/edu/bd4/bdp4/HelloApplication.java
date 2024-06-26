package edu.bd4.bdp4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class    HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VisualInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 461);
        stage.setTitle("BaseDeDatosPractica4!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setMinHeight(461);
        stage.setMinWidth(900);
    }

    public static void main(String[] args) {
        launch();
    }
}