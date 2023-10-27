package com.github.maeda6uiui.miffie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for the Miffie app
 *
 * @author maeda6uiui
 */
public class MiffieApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("miffie_main_view.fxml"));
        var scene = new Scene(root, 1024, 768);

        stage.setTitle("Miffie - MIF Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
