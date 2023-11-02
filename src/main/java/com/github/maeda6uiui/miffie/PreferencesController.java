package com.github.maeda6uiui.miffie;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the preferences view
 *
 * @author maeda6uiui
 */
public class PreferencesController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(PreferencesController.class);

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnTBrowseCustomTheme;
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
    private Label lblTCustomTheme;
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

    private PreferencesViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new PreferencesViewModel();
        viewModel.lDisplayLanguageProperty().bindBidirectional(cbLDisplayLanguage.selectionModelProperty());
        viewModel.tThemeProperty().bindBidirectional(cbTTheme.selectionModelProperty());
        viewModel.ivDarkScreenProperty().bindBidirectional(ckbIVDarkScreen.selectedProperty());
        viewModel.ivExtraHitcheckProperty().bindBidirectional(ckbIVExtraHitcheck.selectedProperty());
        viewModel.ivMissionBriefingProperty().bindBidirectional(taIVMissionBriefing.textProperty());
        viewModel.ivArticleDefinitionFilepathProperty().bindBidirectional(tfIVArticleDefinitionFilepath.textProperty());
        viewModel.ivBD1FilepathProperty().bindBidirectional(tfIVBD1Filepath.textProperty());
        viewModel.ivImage1FilepathProperty().bindBidirectional(tfIVImage1Filepath.textProperty());
        viewModel.ivImage2FilepathProperty().bindBidirectional(tfIVImage2Filepath.textProperty());
        viewModel.ivMissionLongNameProperty().bindBidirectional(tfIVMissionLongName.textProperty());
        viewModel.ivMissionShortNameProperty().bindBidirectional(tfIVMissionShortName.textProperty());
        viewModel.ivPD1FilepathProperty().bindBidirectional(tfIVPD1Filepath.textProperty());
        Bindings.bindBidirectional(
                tfIVSkyType.textProperty(),
                viewModel.ivSkyTypeProperty(),
                new NumberStringConverter()
        );
        Bindings.bindBidirectional(
                tfMMaxNumHalfWidthCharactersInLine.textProperty(),
                viewModel.mMaxNumHalfWidthCharactersInLineProperty(),
                new NumberStringConverter()
        );
        Bindings.bindBidirectional(
                tfMMaxNumLines.textProperty(),
                viewModel.mMaxNumLinesProperty(),
                new NumberStringConverter()
        );
        viewModel.mReadEncodingProperty().bindBidirectional(tfMReadEncoding.textProperty());
        viewModel.mWriteEncodingProperty().bindBidirectional(tfMWriteEncoding.textProperty());
        viewModel.tCustomThemeFilepathProperty().bindBidirectional(tfTCustomThemeFilepath.textProperty());
        Bindings.bindBidirectional(
                tfWWindowHeight.textProperty(),
                viewModel.wWindowHeightProperty(),
                new NumberStringConverter()
        );
        Bindings.bindBidirectional(
                tfWWindowWidth.textProperty(),
                viewModel.wWindowWidthProperty(),
                new NumberStringConverter()
        );

        boolean b = viewModel.populate(cbLDisplayLanguage, cbTTheme);
        if (!b) {
            logger.error("Failed to initialize the preferences view");

            Stage stage = (Stage) lblLDisplayLanguage.getScene().getWindow();
            stage.close();
        }
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

    @FXML
    protected void onActionBtnTBrowseCustomTheme(ActionEvent event) {

    }
}
