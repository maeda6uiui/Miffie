package com.github.maeda6uiui.miffie;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the about view
 *
 * @author maeda6uiui
 */
public class AboutController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(AboutController.class);

    @FXML
    private Label lblHeader;
    @FXML
    private TextArea taAppInfo;
    @FXML
    private Button btnCopy;
    @FXML
    private Button btnOK;

    private ResourceBundle resources;

    private AboutViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new AboutViewModel();
        taAppInfo.textProperty().bind(viewModel.appInfoProperty());

        boolean b = viewModel.populate();
        if (!b) {
            logger.error("Failed to initialize the about view, this application will be terminated");
            Platform.exit();
        }

        this.resources = resources;
    }

    @FXML
    protected void onActionBtnCopy(ActionEvent event) {
        boolean b = viewModel.copyToClipboard();

        Alert alert;
        if (b) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("alt.title.info.text"));
            alert.setHeaderText(resources.getString("alt.copy.info.header.text"));
            alert.setContentText(resources.getString("alt.copy.info.content.text"));
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("alt.title.error.text"));
            alert.setHeaderText(resources.getString("alt.copy.error.header.text"));
            alert.setContentText(resources.getString("alt.copy.error.content.text"));
        }
        alert.showAndWait();
    }

    @FXML
    protected void onActionBtnOK(ActionEvent event) {
        Stage stage = (Stage) lblHeader.getScene().getWindow();
        stage.close();
    }
}
