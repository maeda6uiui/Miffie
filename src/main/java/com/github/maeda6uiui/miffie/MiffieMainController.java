package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.SkyType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the GUI components
 *
 * @author maeda6uiui
 */
public class MiffieMainController implements Initializable {
    @FXML
    private MenuItem miNew;
    @FXML
    private MenuItem miOpen;
    @FXML
    private MenuItem miSave;
    @FXML
    private MenuItem miSaveAs;
    @FXML
    private MenuItem miPreferences;
    @FXML
    private MenuItem miQuit;
    @FXML
    private MenuItem miCopy;
    @FXML
    private MenuItem miPaste;
    @FXML
    private MenuItem miAbout;

    @FXML
    private Label lblMissionShortName;
    @FXML
    private Label lblMissionLongName;
    @FXML
    private Label lblBD1Filepath;
    @FXML
    private Label lblPD1Filepath;
    @FXML
    private Label lblSkyType;
    @FXML
    private Label lblImage1Filepath;
    @FXML
    private Label lblImage2Filepath;
    @FXML
    private Label lblArticleDefinitionFilepath;
    @FXML
    private CheckBox ckbExtraHitcheck;
    @FXML
    private CheckBox ckbDarkScreen;
    @FXML
    private TextField tfMissionShortName;
    @FXML
    private TextField tfMissionLongName;
    @FXML
    private TextField tfBD1Filepath;
    @FXML
    private TextField tfPD1Filepath;
    @FXML
    private ComboBox<SkyType> cbSkyType;
    @FXML
    private TextField tfImage1Filepath;
    @FXML
    private TextField tfImage2Filepath;
    @FXML
    private TextField tfArticleDefinitionFilepath;

    @FXML
    private Label lblMissionBriefing;
    @FXML
    private TextArea taMissionBriefing;
    @FXML
    private Button btnPreviewMissionBriefing;

    @Override
    public void initialize(URL location,ResourceBundle resources){

    }

    @FXML
    protected void onActionMiNew(ActionEvent event) {

    }

    @FXML
    protected void onActionMiOpen(ActionEvent event) {

    }

    @FXML
    protected void onActionMiSave(ActionEvent event) {

    }

    @FXML
    protected void onActionMiSaveAs(ActionEvent event) {

    }

    @FXML
    protected void onActionMiPreferences(ActionEvent event) {

    }

    @FXML
    protected void onActionMiQuit(ActionEvent event) {

    }

    @FXML
    protected void onActionMiCopy(ActionEvent event) {

    }

    @FXML
    protected void onActionMiPaste(ActionEvent event) {

    }

    @FXML
    protected void onActionMiAbout(ActionEvent event) {

    }

    @FXML
    protected void onActionBtnPreviewMissionBriefing(ActionEvent event){

    }
}
