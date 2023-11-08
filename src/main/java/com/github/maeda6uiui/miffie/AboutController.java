package com.github.maeda6uiui.miffie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the about view
 *
 * @author maeda6uiui
 */
public class AboutController implements Initializable {
    @FXML
    private Label lblHeader;
    @FXML
    private TextArea taAppInfo;
    @FXML
    private Button btnCopy;
    @FXML
    private Button btnOK;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void onActionBtnCopy(ActionEvent event) {

    }

    @FXML
    protected void onActionBtnOK(ActionEvent event) {
        Stage stage = (Stage) lblHeader.getScene().getWindow();
        stage.close();
    }
}
