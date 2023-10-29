package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.SkyType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
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
    private ComboBox<Pair<SkyType, String>> cbSkyType;
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
    public void initialize(URL location, ResourceBundle resources) {
        var skyTypes = new ArrayList<Pair<SkyType, String>>();
        skyTypes.add(new Pair<>(SkyType.NONE, resources.getString("cbSkyType.text.none")));
        skyTypes.add(new Pair<>(SkyType.SUNNY, resources.getString("cbSkyType.text.sunny")));
        skyTypes.add(new Pair<>(SkyType.CLOUDY, resources.getString("cbSkyType.text.cloudy")));
        skyTypes.add(new Pair<>(SkyType.NIGHT, resources.getString("cbSkyType.text.night")));
        skyTypes.add(new Pair<>(SkyType.EVENING, resources.getString("cbSkyType.text.evening")));
        skyTypes.add(new Pair<>(SkyType.WILDERNESS, resources.getString("cbSkyType.text.wilderness")));

        cbSkyType.getItems().addAll(skyTypes);
        cbSkyType.setValue(skyTypes.get(0));

        Callback<ListView<Pair<SkyType, String>>, ListCell<Pair<SkyType, String>>> factory
                = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Pair<SkyType, String> item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText("");
                } else {
                    setText(item.getValue());
                }
            }
        };
        cbSkyType.setCellFactory(factory);
        cbSkyType.setButtonCell(factory.call(null));
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
    protected void onActionBtnPreviewMissionBriefing(ActionEvent event) {

    }
}
