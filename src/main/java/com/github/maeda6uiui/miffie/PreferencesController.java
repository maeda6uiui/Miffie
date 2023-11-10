package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.SkyType;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

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
    private Button btnTPreviewTheme;
    @FXML
    private Button btnMValidateEncodings;
    @FXML
    private Button btnMValidateHalfWidthCharactersRegex;
    @FXML
    private ComboBox<Pair<SkyType, String>> cbIVSkyType;
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
    private Label lblMHalfWidthCharactersRegex;
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
    private TextField tfMMaxNumHalfWidthCharactersInLine;
    @FXML
    private TextField tfMMaxNumLines;
    @FXML
    private TextField tfMReadEncoding;
    @FXML
    private TextField tfMWriteEncoding;
    @FXML
    private TextField tfMHalfWidthCharactersRegex;
    @FXML
    private TextField tfTCustomThemeFilepath;
    @FXML
    private TextField tfWWindowHeight;
    @FXML
    private TextField tfWWindowWidth;

    private PreferencesViewModel viewModel;

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new PreferencesViewModel();
        viewModel.ivSkyTypeProperty().bindBidirectional(cbIVSkyType.selectionModelProperty());
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
        viewModel.mHalfWidthCharactersRegexProperty().bindBidirectional(tfMHalfWidthCharactersRegex.textProperty());
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

        this.resources = resources;

        Platform.runLater(() -> {
            boolean b = viewModel.populate(
                    resources,
                    cbIVSkyType,
                    cbLDisplayLanguage,
                    cbTTheme
            );
            if (!b) {
                logger.error("Failed to initialize the preferences view. This window will be closed");

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resources.getString("alt.title.error.text"));
                alert.setHeaderText(resources.getString("alt.initialize.error.header.text"));
                alert.setContentText(resources.getString("alt.initialize.error.content.text"));
                alert.showAndWait();

                Stage stage = (Stage) lblLDisplayLanguage.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) lblLDisplayLanguage.getScene().getWindow();
        stage.close();

        viewModel.revertToPreviousTheme();
    }

    @FXML
    public void onActionCbTTheme(ActionEvent event) {
        if (cbTTheme.getSelectionModel().getSelectedItem().name().equals("custom")) {
            tfTCustomThemeFilepath.setDisable(false);
            btnTBrowseCustomTheme.setDisable(false);
        } else {
            tfTCustomThemeFilepath.setDisable(true);
            btnTBrowseCustomTheme.setDisable(true);
            tfTCustomThemeFilepath.setText("");
        }

        if (cbTTheme.getSelectionModel().getSelectedItem().name().equals("system")) {
            btnTPreviewTheme.setDisable(true);
        } else {
            btnTPreviewTheme.setDisable(false);
        }
    }

    @FXML
    protected void onActionBtnCancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    protected void onActionBtnOK(ActionEvent event) {
        boolean b = viewModel.saveSettings();

        Alert alert;
        if (!b) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("alt.title.error.text"));
            alert.setHeaderText(resources.getString("alt.saveSettings.error.header.text"));
            alert.setContentText(resources.getString("alt.saveSettings.error.content.text"));
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("alt.title.info.text"));
            alert.setHeaderText(resources.getString("alt.saveSettings.info.header.text"));
            alert.setContentText(resources.getString("alt.saveSettings.info.content.text"));
        }
        alert.showAndWait();

        this.closeWindow();
    }

    @FXML
    protected void onActionBtnTBrowseCustomTheme(ActionEvent event) {
        var fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(lblLDisplayLanguage.getScene().getWindow());
        if (file == null) {
            return;
        }

        tfTCustomThemeFilepath.setText(file.toString());
    }

    @FXML
    protected void onActionBtnTPreviewTheme(ActionEvent event) {
        boolean b = viewModel.previewSelectedTheme();
        if (!b) {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resources.getString("alt.title.warn.text"));
            alert.setHeaderText(resources.getString("alt.previewTheme.warn.header.text"));
            alert.setContentText(resources.getString("alt.previewTheme.warn.content.text"));
            alert.showAndWait();
        }
    }

    @FXML
    protected void onActionBtnMValidateEncodings(ActionEvent event) {
        boolean bReadEncoding = viewModel.isMIFReadEncodingSupported();
        boolean bWriteEncoding = viewModel.isMIFWriteEncodingSupported();

        Alert alert;
        if (bReadEncoding && bWriteEncoding) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("alt.title.info.text"));
            alert.setHeaderText(resources.getString("alt.validateMIFEncodings.info.header.text"));
            alert.setContentText(resources.getString("alt.validateMIFEncodings.info.content.text"));
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resources.getString("alt.title.warn.text"));
            alert.setHeaderText(resources.getString("alt.validateMIFEncodings.warn.header.text"));

            Function<Boolean, String> bToS = b -> b ? "OK" : "NG";
            String msgTemplate = resources.getString("alt.validateMIFEncodings.warn.content.text");
            String msg = new StringReplacer(msgTemplate)
                    .replace("${readEncoding}", tfMReadEncoding.getText())
                    .replace("${isReadEncodingValid}", bToS.apply(bReadEncoding))
                    .replace("${writeEncoding}", tfMWriteEncoding.getText())
                    .replace("${isWriteEncodingValid}", bToS.apply(bWriteEncoding))
                    .toString();
            alert.setContentText(msg);
        }

        alert.showAndWait();
    }

    @FXML
    protected void onActionBtnMValidateHalfWidthCharactersRegex(ActionEvent event) {
        String validationResult = viewModel.validateMIFHalfWidthCharactersRegex();

        Alert alert;
        if (validationResult.isEmpty()) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("alt.title.info.text"));
            alert.setHeaderText(resources.getString("alt.validateMIFHalfWidthCharactersRegex.info.header.text"));
            alert.setContentText(resources.getString("alt.validateMIFHalfWidthCharactersRegex.info.content.text"));
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resources.getString("alt.title.warn.text"));
            alert.setHeaderText(resources.getString("alt.validateMIFHalfWidthCharactersRegex.warn.header.text"));

            String msgTemplate = resources.getString("alt.validateMIFHalfWidthCharactersRegex.warn.content.text");
            String msg = new StringReplacer(msgTemplate)
                    .replace("${validationResult}", validationResult)
                    .toString();
            alert.setContentText(msg);
        }

        alert.showAndWait();
    }
}
