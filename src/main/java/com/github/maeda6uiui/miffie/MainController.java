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
import java.util.Optional;
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

    private ResourceBundle resources;

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

        CommandLineArguments.get()
                .flatMap(cla -> cla.getArgs()
                        .stream()
                        .findFirst())
                .ifPresent(filepath -> {
                    var file = new File(filepath);
                    this.loadMIFWithErrorAlert(file);
                });

        this.resources = resources;

        Platform.runLater(() -> {
            lblMissionShortName.getScene().getWindow().setOnCloseRequest(event -> {
                boolean b2 = this.handleSaveBeforeContinue(new ActionEvent());
                if (!b2) {
                    event.consume();
                }

                logger.info("Exiting the Miffie app...");
            });
        });
    }

    private void loadMIFWithErrorAlert(File file) {
        String errorMessage = viewModel.loadMIF(file);
        if (errorMessage.isEmpty()) {
            currentFile = file;
            return;
        }

        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("alt.title.error.text"));
        alert.setHeaderText(resources.getString("alt.loadMIF.error.header.text"));

        String msgTemplate = resources.getString("alt.loadMIF.error.content.text");
        String msg = new StringReplacer(msgTemplate).replace("${errorMessage}", errorMessage).toString();
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void saveMIFWithErrorAlert(File file) {
        String errorMessage = viewModel.saveMIF(file);
        if (errorMessage.isEmpty()) {
            currentFile = file;
            return;
        }

        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("alt.title.error.text"));
        alert.setHeaderText(resources.getString("alt.saveMIF.error.header.text"));

        String msgTemplate = resources.getString("alt.saveMIF.error.content.text");
        String msg = new StringReplacer(msgTemplate).replace("${errorMessage}", errorMessage).toString();
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private Optional<ButtonType> showAndWaitSaveConfirmationAlert() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alt.title.confirm.text"));
        alert.setHeaderText(resources.getString("alt.shouldSave.confirm.header.text"));
        alert.setContentText(resources.getString("alt.shouldSave.confirm.content.text"));
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        return alert.showAndWait();
    }

    /**
     * This method runs the boilerplate procedure to ask the user
     * whether to save the changes before continuing to the next step.
     * This method is supposed to be called from one of the event handlers
     * such as {@link #onActionMiQuit(ActionEvent)}.
     *
     * @param event Event
     * @return {@code true} if you can continue the procedure, otherwise {@code false}
     */
    private boolean handleSaveBeforeContinue(ActionEvent event) {
        if (!viewModel.hasContentChanged()) {
            return true;
        }

        ButtonType buttonType = this.showAndWaitSaveConfirmationAlert().orElse(null);
        if (buttonType == null || buttonType == ButtonType.CANCEL) {
            return false;
        } else if (buttonType == ButtonType.YES) {
            this.onActionMiSave(event);
            return false;
        }

        return true;
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
            this.loadMIFWithErrorAlert(file);
            ok = true;
        }

        event.setDropCompleted(ok);
        event.consume();
    }

    @FXML
    protected void onActionMiNew(ActionEvent event) {
        boolean b = this.handleSaveBeforeContinue(event);
        if (!b) {
            return;
        }

        viewModel.setInitialValues();
        currentFile = null;
    }

    @FXML
    protected void onActionMiOpen(ActionEvent event) {
        boolean b = this.handleSaveBeforeContinue(event);
        if (!b) {
            return;
        }

        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MIF (*.mif)", "*.mif"),
                new FileChooser.ExtensionFilter("JSON (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("YAML (*.yaml, *.yml)", "*.yaml", "*.yml")
        );
        File file = fileChooser.showOpenDialog(lblMissionShortName.getScene().getWindow());
        if (file == null) {
            return;
        }

        this.loadMIFWithErrorAlert(file);
    }

    @FXML
    protected void onActionMiSave(ActionEvent event) {
        if (currentFile != null) {
            this.saveMIFWithErrorAlert(currentFile);
        } else {
            this.onActionMiSaveAs(event);
        }
    }

    @FXML
    protected void onActionMiSaveAs(ActionEvent event) {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MIF (*.mif)", "*.mif"),
                new FileChooser.ExtensionFilter("JSON (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("YAML (*.yaml, *.yml)", "*.yaml", "*.yml")
        );
        File file = fileChooser.showSaveDialog(lblMissionShortName.getScene().getWindow());
        if (file == null) {
            return;
        }

        this.saveMIFWithErrorAlert(file);
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
                    logger.warn("Settings are not available. Fall back to default locale 'en'");
                    this.openPreferencesDialog("en");
                }
        );
    }

    @FXML
    protected void onActionMiQuit(ActionEvent event) {
        boolean b = this.handleSaveBeforeContinue(event);
        if (!b) {
            return;
        }

        Platform.exit();
    }

    @FXML
    protected void onActionMiCopy(ActionEvent event) {
        boolean b = viewModel.copyToClipboard();
        if (!b) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("alt.title.error.text"));
            alert.setHeaderText(resources.getString("alt.copyToClipboard.error.header.text"));
            alert.setContentText(resources.getString("alt.copyToClipboard.error.content.text"));
            alert.showAndWait();
        }
    }

    @FXML
    protected void onActionMiPaste(ActionEvent event) {
        viewModel.pasteFromClipboard();
    }

    private void openAboutDialog(String locale) {
        try {
            Path propertiesDir = Paths.get("./Data/Properties");
            var loader = new URLClassLoader(new URL[]{propertiesDir.toUri().toURL()});
            ResourceBundle rb = ResourceBundle.getBundle(
                    "about_view",
                    Locale.of(locale),
                    loader
            );

            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(this.getClass().getResource("about_view.fxml")),
                    rb
            );
            var scene = new Scene(root, 600, 400);
            var aboutDialog = new Stage();
            aboutDialog.setScene(scene);
            aboutDialog.initOwner(lblMissionShortName.getScene().getWindow());
            aboutDialog.initModality(Modality.WINDOW_MODAL);
            aboutDialog.setTitle(rb.getString("title.text"));
            aboutDialog.showAndWait();
        } catch (IOException e) {
            logger.error("Failed to open about dialog", e);
        }
    }

    @FXML
    protected void onActionMiAbout(ActionEvent event) {
        MiffieSettings.get().ifPresentOrElse(
                settings -> this.openAboutDialog(settings.languageSettings.code),
                () -> {
                    logger.warn("Settings are not available. Fall back to default locale 'en'");
                    this.openAboutDialog("en");
                }
        );
    }

    private void openBriefingPreviewDialog(String locale) {
        try {
            Path propertiesDir = Paths.get("./Data/Properties");
            var loader = new URLClassLoader(new URL[]{propertiesDir.toUri().toURL()});
            ResourceBundle rb = ResourceBundle.getBundle(
                    "briefing_preview_view",
                    Locale.of(locale),
                    loader
            );

            var fxmlLoader = new FXMLLoader(
                    Objects.requireNonNull(this.getClass().getResource("briefing_preview_view.fxml")),
                    rb
            );

            Parent root = fxmlLoader.load();

            BriefingPreviewController controller = fxmlLoader.getController();
            controller.setOriginalBriefingText(taMissionBriefing.getText());

            var scene = new Scene(root, 600, 400);
            var preferencesDialog = new Stage();
            preferencesDialog.setScene(scene);
            preferencesDialog.initOwner(lblMissionShortName.getScene().getWindow());
            preferencesDialog.initModality(Modality.WINDOW_MODAL);
            preferencesDialog.setTitle(rb.getString("title.text"));
            preferencesDialog.showAndWait();
        } catch (IOException e) {
            logger.error("Failed to open briefing preview dialog", e);
        }
    }

    @FXML
    protected void onActionBtnPreviewText(ActionEvent event) {
        MiffieSettings.get().ifPresentOrElse(
                settings -> this.openBriefingPreviewDialog(settings.languageSettings.code),
                () -> {
                    logger.warn("Settings are not available. Fall back to default locale 'en'");
                    this.openBriefingPreviewDialog("en");
                }
        );
    }
}
