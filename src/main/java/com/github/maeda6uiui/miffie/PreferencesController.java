package com.github.maeda6uiui.miffie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the preferences view
 *
 * @author maeda6uiui
 */
public class PreferencesController implements Initializable {
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOK;
    @FXML
    private ComboBox<DisplayLanguage> cbLDisplayLanguage;
    @FXML
    private ComboBox<MiffieTheme> cbTTheme;
    @FXML
    private CheckBox ckbIVDarkScreen;
    @FXML
    private CheckBox ckbIVExtraHitcheck;
    @FXML
    private Label lblIVArticleDefinitionFilepath;
    @FXML
    private Label lblIVBD1Filepath;
    @FXML
    private Label lblIVDarkScreen;
    @FXML
    private Label lblIVExtraHitcheck;
    @FXML
    private Label lblIVImage1Filepath;
    @FXML
    private Label lblIVImage2Filepath;
    @FXML
    private Label lblIVMissionBriefing;
    @FXML
    private Label lblIVMissionLongName;
    @FXML
    private Label lblIVMissionShortName;
    @FXML
    private Label lblIVPD1Filepath;
    @FXML
    private Label lblIVSkyType;
    @FXML
    private Label lblLDisplayLanguage;
    @FXML
    private Label lblMMaxNumHalfWidthCharactersInLine;
    @FXML
    private Label lblMMaxNumLines;
    @FXML
    private Label lblMReadEncoding;
    @FXML
    private Label lblMWriteEncoding;
    @FXML
    private Label lblTTheme;
    @FXML
    private Label lblTUseCustomTheme;
    @FXML
    private Label lblWCross;
    @FXML
    private Label lblWWindowSize;
    @FXML
    private TextArea taIVMissionBriefing;
    @FXML
    private TextField tfIVArticleDefinitionFilepath;
    @FXML
    private TextField tfIVBD1Filepath;
    @FXML
    private TextField tfIVImage1Filepath;
    @FXML
    private TextField tfIVImage2Filepath;
    @FXML
    private TextField tfIVMissionLongName;
    @FXML
    private TextField tfIVMissionShortName;
    @FXML
    private TextField tfIVPD1Filepath;
    @FXML
    private TextField tfIVSkyType;
    @FXML
    private TextField tfMMaxNumHalfWidthCharactersInLine;
    @FXML
    private TextField tfMMaxNumLines;
    @FXML
    private TextField tfMReadEncoding;
    @FXML
    private TextField tfMWriteEncoding;
    @FXML
    private TextField tfTCustomThemeFilepath;
    @FXML
    private TextField tfWWindowHeight;
    @FXML
    private TextField tfWWindowWidth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onActionCbTTheme(ActionEvent event) {

    }

    @FXML
    protected void onActionBtnCancel(ActionEvent event) {

    }

    @FXML
    protected void onActionBtnOK(ActionEvent event) {

    }
}
