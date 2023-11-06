package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.SkyType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the main view
 *
 * @author maeda6uiui
 */
public class MainController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

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
    private MenuItem miUndo;
    @FXML
    private MenuItem miRedo;
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
    private Button btnPreviewText;

    private MainViewModel viewModel;

    private File currentFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new MainViewModel();
        viewModel.missionShortNameProperty().bindBidirectional(tfMissionShortName.textProperty());
        viewModel.missionLongNameProperty().bindBidirectional(tfMissionLongName.textProperty());
        viewModel.bd1FilepathProperty().bindBidirectional(tfBD1Filepath.textProperty());
        viewModel.pd1FilepathProperty().bindBidirectional(tfPD1Filepath.textProperty());
        viewModel.skyTypeProperty().bindBidirectional(cbSkyType.selectionModelProperty());
        viewModel.image1FilepathProperty().bindBidirectional(tfImage1Filepath.textProperty());
        viewModel.image2FilepathProperty().bindBidirectional(tfImage2Filepath.textProperty());
        viewModel.articleDefinitionFilepathProperty().bindBidirectional(tfArticleDefinitionFilepath.textProperty());
        viewModel.extraHitcheckProperty().bindBidirectional(ckbExtraHitcheck.selectedProperty());
        viewModel.darkScreenProperty().bindBidirectional(ckbDarkScreen.selectedProperty());
        viewModel.missionBriefingProperty().bindBidirectional(taMissionBriefing.textProperty());

        boolean b = viewModel.populate(resources, cbSkyType);
        if (!b) {
            logger.error("Failed to initialize the main view, this application will be terminated");
            Platform.exit();
        }
    }

    @FXML
    protected void onDragOverMainView(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }

        event.consume();
    }

    @FXML
    protected void onDragDroppedMainView(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean ok = false;
        if (db.hasFiles()) {
            //Open the first file
            File file = db.getFiles().get(0);
            viewModel.loadMIF(file);
            currentFile = file;

            ok = true;
        }

        event.setDropCompleted(ok);
        event.consume();
    }

    @FXML
    protected void onActionMiNew(ActionEvent event) {

    }

    @FXML
    protected void onActionMiOpen(ActionEvent event) {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MIF (*.mif)", "*.mif")
        );
        File file = fileChooser.showOpenDialog(lblMissionShortName.getScene().getWindow());
        if (file == null) {
            return;
        }

        viewModel.loadMIF(file);
        currentFile = file;
    }

    @FXML
    protected void onActionMiSave(ActionEvent event) {
        if (currentFile != null) {
            viewModel.saveMIF(currentFile);
        } else {
            this.onActionMiSaveAs(event);
        }
    }

    @FXML
    protected void onActionMiSaveAs(ActionEvent event) {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MIF (*.mif)", "*.mif")
        );
        File file = fileChooser.showSaveDialog(lblMissionShortName.getScene().getWindow());
        if (file == null) {
            return;
        }

        viewModel.saveMIF(file);
        currentFile = file;
    }

    private void openPreferencesDialog(String locale) {
        try {
            Path propertiesDir = Paths.get("./Data/Properties");
            var loader = new URLClassLoader(new URL[]{propertiesDir.toUri().toURL()});
            ResourceBundle rb = ResourceBundle.getBundle(
                    "preferences_view",
                    Locale.of(locale),
                    loader
            );

            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(this.getClass().getResource("preferences_view.fxml")),
                    rb
            );
            var scene = new Scene(root, 600, 400);
            var preferencesDialog = new Stage();
            preferencesDialog.setScene(scene);
            preferencesDialog.initOwner(lblMissionShortName.getScene().getWindow());
            preferencesDialog.initModality(Modality.WINDOW_MODAL);
            preferencesDialog.setTitle(rb.getString("title.text"));
            preferencesDialog.showAndWait();
        } catch (IOException e) {
            logger.error("Failed to open preferences dialog", e);
        }
    }

    @FXML
    protected void onActionMiPreferences(ActionEvent event) {
        MiffieSettings.get().ifPresentOrElse(
                settings -> this.openPreferencesDialog(settings.languageSettings.code),
                () -> {
                    logger.error("Settings is not available. Fall back to default locale 'en'");
                    this.openPreferencesDialog("en");
                }
        );
    }

    @FXML
    protected void onActionMiQuit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void onActionMiUndo(ActionEvent event) {

    }

    @FXML
    protected void onActionMiRedo(ActionEvent event) {

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
    protected void onActionBtnPreviewText(ActionEvent event) {

    }
}
