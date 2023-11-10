package com.github.maeda6uiui.miffie;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

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

    private BriefingPreviewViewModel viewModel;

    private String originalBriefingText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new BriefingPreviewViewModel();
        taBriefingPreview.textProperty().bind(viewModel.briefingPreviewProperty());

        Platform.runLater(() -> {
            boolean b = viewModel.trimBriefingText(originalBriefingText);
            if (!b) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resources.getString("alt.title.error.text"));
                alert.setHeaderText(resources.getString("alt.trimBriefingText.error.header.text"));
                alert.setContentText(resources.getString("alt.trimBriefingText.error.content.text"));
                alert.showAndWait();

                Stage stage = (Stage) taBriefingPreview.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void setOriginalBriefingText(String originalBriefingText) {
        this.originalBriefingText = originalBriefingText;
    }
}
