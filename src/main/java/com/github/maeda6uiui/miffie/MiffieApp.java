package com.github.maeda6uiui.miffie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Entry point for the Miffie app
 *
 * @author maeda6uiui
 */
public class MiffieApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MiffieApp.class);

    @Override
    public void start(Stage stage) throws IOException {
        MiffieSettings settings = MiffieSettings.load("./Data/settings.yaml");

        Path propertiesDir = Paths.get("./Data/Properties");
        var loader = new URLClassLoader(new URL[]{propertiesDir.toUri().toURL()});
        ResourceBundle rb = ResourceBundle.getBundle(
                "main_view",
                Locale.of(settings.languageSettings.code),
                loader
        );

        settings.themeSettings.getCSS().ifPresent(css -> Application.setUserAgentStylesheet(css));

        Parent root = FXMLLoader.load(
                Objects.requireNonNull(this.getClass().getResource("main_view.fxml")),
                rb
        );
        var scene = new Scene(
                root,
                settings.windowSettings.width,
                settings.windowSettings.height
        );

        stage.setTitle("Miffie - MIF Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        logger.info("Starting the Miffie app...");
        logger.info("args: {}", Arrays.toString(args));
        
        launch(args);
    }
}
