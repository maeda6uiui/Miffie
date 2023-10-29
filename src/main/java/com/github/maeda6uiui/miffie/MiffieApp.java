package com.github.maeda6uiui.miffie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Entry point for the Miffie app
 *
 * @author maeda6uiui
 */
public class MiffieApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Path settingDir = Paths.get("./Data/Setting");
        var loader = new URLClassLoader(new URL[]{settingDir.toUri().toURL()});
        ResourceBundle rb = ResourceBundle.getBundle("miffie_main_view", Locale.getDefault(), loader);

        Parent root = FXMLLoader.load(
                Objects.requireNonNull(this.getClass().getResource("miffie_main_view.fxml")),
                rb
        );
        var scene = new Scene(root, 1000, 400);

        stage.setTitle("Miffie - MIF Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
