package com.github.maeda6uiui.miffie;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the briefing preview view
 *
 * @author maeda6uiui
 */
public class BriefingPreviewController implements Initializable {
    @FXML
    private TextArea taBriefingPreview;

    private String originalBriefingText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setOriginalBriefingText(String originalBriefingText) {
        this.originalBriefingText = originalBriefingText;
    }
}
