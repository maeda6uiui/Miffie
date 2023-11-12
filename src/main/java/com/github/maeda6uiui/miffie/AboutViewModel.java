package com.github.maeda6uiui.miffie;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * View model for the about view
 *
 * @author maeda6uiui
 */
public class AboutViewModel {
    private static final Logger logger = LoggerFactory.getLogger(AboutViewModel.class);

    private StringProperty appInfo;

    public AboutViewModel() {
        appInfo = new SimpleStringProperty();
    }

    /**
     * Populates the view.
     * Call this method after binding is done.
     *
     * @return {@code true} if success, {@code false} if error
     */
    public boolean populate() {
        try {
            Path appInfoFile = Paths.get(this.getClass().getResource("/app_info.yaml").toURI());
            String yaml = Files.readString(appInfoFile);
            this.setAppInfo(yaml);
        } catch (URISyntaxException | IOException e) {
            logger.error("Failed to load app info from file", e);
            return false;
        }

        return true;
    }

    public boolean copyToClipboard() {
        Clipboard cb = Clipboard.getSystemClipboard();

        var content = new HashMap<DataFormat, Object>();
        content.put(DataFormat.PLAIN_TEXT, this.getAppInfo());

        return cb.setContent(content);
    }

    public String getAppInfo() {
        return appInfo.get();
    }

    public StringProperty appInfoProperty() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo.set(appInfo);
    }
}
