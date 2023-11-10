package com.github.maeda6uiui.miffie;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the briefing preview view
 *
 * @author maeda6uiui
 */
public class BriefingPreviewController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(BriefingPreviewController.class);

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
                logger.error("Failed to trim the briefing text for preview. This window will be closed");

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
