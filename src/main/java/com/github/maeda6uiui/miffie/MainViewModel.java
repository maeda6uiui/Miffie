package com.github.maeda6uiui.miffie;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dabasan.jxm.mif.MissionInfo;
import com.github.dabasan.jxm.mif.SkyType;
import javafx.beans.property.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.util.Callback;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * View model for the main view
 *
 * @author maeda6uiui
 */
public class MainViewModel {
    private static final Logger logger = LoggerFactory.getLogger(MainViewModel.class);

    private StringProperty missionShortName;
    private StringProperty missionLongName;
    private StringProperty bd1Filepath;
    private StringProperty pd1Filepath;
    private ObjectProperty<SingleSelectionModel<Pair<SkyType, String>>> skyType;
    private StringProperty image1Filepath;
    private StringProperty image2Filepath;
    private StringProperty articleDefinitionFilepath;
    private BooleanProperty extraHitcheck;
    private BooleanProperty darkScreen;
    private StringProperty missionBriefing;

    private List<Pair<SkyType, String>> cbSkyTypeItems;

    private StringProperty errorMessageLoad;
    private StringProperty errorMessageSave;

    private MiffieMIFModel mifModel;
    private MiffieSettings.MIFSettings mifSettings;

    public MainViewModel() {
        missionShortName = new SimpleStringProperty();
        missionLongName = new SimpleStringProperty();
        bd1Filepath = new SimpleStringProperty();
        pd1Filepath = new SimpleStringProperty();
        skyType = new SimpleObjectProperty<>();
        image1Filepath = new SimpleStringProperty();
        image2Filepath = new SimpleStringProperty();
        articleDefinitionFilepath = new SimpleStringProperty();
        extraHitcheck = new SimpleBooleanProperty();
        darkScreen = new SimpleBooleanProperty();
        missionBriefing = new SimpleStringProperty();

        errorMessageLoad = new SimpleStringProperty();
        errorMessageSave = new SimpleStringProperty();

        mifModel = new MiffieMIFModel();

        MiffieSettings.get().ifPresentOrElse(
                settings -> mifSettings = settings.mifSettings,
                () -> {
                    logger.warn("Settings is not available. Fall back to default settings");
                    mifSettings = new MiffieSettings.MIFSettings();
                }
        );
    }

    /**
     * Populate the view.
     * Call this method after binding is done.
     *
     * @param resources Resources for i18n
     * @param cbSkyType Combobox for sky type
     * @return {@code true} if success, {@code false} if error
     */
    public boolean populate(
            ResourceBundle resources,
            ComboBox<Pair<SkyType, String>> cbSkyType) {
        //Combobox
        cbSkyTypeItems = new ArrayList<>();
        cbSkyTypeItems.add(new Pair<>(SkyType.NONE, resources.getString("cbSkyType.text.none")));
        cbSkyTypeItems.add(new Pair<>(SkyType.SUNNY, resources.getString("cbSkyType.text.sunny")));
        cbSkyTypeItems.add(new Pair<>(SkyType.CLOUDY, resources.getString("cbSkyType.text.cloudy")));
        cbSkyTypeItems.add(new Pair<>(SkyType.NIGHT, resources.getString("cbSkyType.text.night")));
        cbSkyTypeItems.add(new Pair<>(SkyType.EVENING, resources.getString("cbSkyType.text.evening")));
        cbSkyTypeItems.add(new Pair<>(SkyType.WILDERNESS, resources.getString("cbSkyType.text.wilderness")));

        cbSkyType.getItems().addAll(cbSkyTypeItems);
        cbSkyType.setValue(cbSkyTypeItems.get(0));

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

        //Initial value
        if (MiffieSettings.get().isEmpty()) {
            logger.error("Cannot populate the view because settings is empty");
            return false;
        }

        MiffieSettings.InitialValue.MainView ivMainView = MiffieSettings.get().get().initialValue.mainView;
        this.setMissionShortName(ivMainView.tfMissionShortName);
        this.setMissionLongName(ivMainView.tfMissionLongName);
        this.setBd1Filepath(ivMainView.tfBD1Filepath);
        this.setPd1Filepath(ivMainView.tfPD1Filepath);
        this.setSkyType(ivMainView.cbSkyType);
        this.setImage1Filepath(ivMainView.tfImage1Filepath);
        this.setImage2Filepath(ivMainView.tfImage2Filepath);
        this.setArticleDefinitionFilepath(ivMainView.tfArticleDefinitionFilepath);
        this.setExtraHitcheck(ivMainView.ckbExtraHitcheck);
        this.setDarkScreen(ivMainView.ckbDarkScreen);
        this.setMissionBriefing(ivMainView.taMissionBriefing);

        return true;
    }

    private String getFileExtension(File file) {
        String filename = file.getName();
        int lastDotPos = filename.lastIndexOf('.');

        //There is no '.' in the filename
        if (lastDotPos == -1) {
            return "";
        }
        //'.' is the last character of the filename
        if (lastDotPos + 1 >= filename.length()) {
            return "";
        }

        return filename.substring(lastDotPos + 1).toLowerCase();
    }

    private void fromMissionInfo(MissionInfo missionInfo) {
        this.setMissionShortName(missionInfo.missionTitle);
        this.setMissionLongName(missionInfo.missionFullname);
        this.setBd1Filepath(missionInfo.pathnameOfBlock);
        this.setPd1Filepath(missionInfo.pathnameOfPoint);
        this.setSkyType(missionInfo.skyType);
        this.setImage1Filepath(missionInfo.pathnameOfImage1);
        this.setImage2Filepath(missionInfo.pathnameOfImage2);
        this.setArticleDefinitionFilepath(missionInfo.pathnameOfObj);
        this.setExtraHitcheck(missionInfo.extraCollision);
        this.setDarkScreen(missionInfo.darkScreen);
        this.setMissionBriefing(missionInfo.briefingText);
    }

    private MissionInfo toMissionInfo() {
        return new MissionInfo()
                .setMissionTitle(this.getMissionShortName())
                .setMissionFullname(this.getMissionLongName())
                .setPathnameOfBlock(this.getBd1Filepath())
                .setPathnameOfPoint(this.getPd1Filepath())
                .setSkyType(this.getSkyType())
                .setPathnameOfImage1(this.getImage1Filepath())
                .setPathnameOfImage2(this.getImage2Filepath())
                .setPathnameOfObj(this.getArticleDefinitionFilepath())
                .setExtraCollision(this.isExtraHitcheck())
                .setDarkScreen(this.isDarkScreen())
                .setBriefingText(this.getMissionBriefing());
    }

    public void loadMIF(File file) {
        this.setErrorMessageLoad(null);

        MissionInfo missionInfo;
        try {
            String extension = this.getFileExtension(file);
            switch (extension) {
                case "mif" -> missionInfo = mifModel.loadMIF(file, mifSettings.readEncoding);
                case "json" -> missionInfo = mifModel.loadMIFFromJSON(file);
                case "yaml", "yml" -> missionInfo = mifModel.loadMIFFromYAML(file);
                default -> {
                    logger.warn("Unknown extension '{}' specified, default to MIF format", extension);
                    missionInfo = mifModel.loadMIF(file, mifSettings.readEncoding);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load MIF file", e);
            this.setErrorMessageLoad(e.toString());

            return;
        }

        this.fromMissionInfo(missionInfo);

        this.setErrorMessageLoad("");
    }

    public void saveMIF(File file) {
        this.setErrorMessageSave(null);

        MissionInfo missionInfo = this.toMissionInfo();
        try {
            String extension = this.getFileExtension(file);
            switch (extension) {
                case "mif" -> mifModel.saveMIF(missionInfo, file, mifSettings.writeEncoding);
                case "json" -> mifModel.saveMIFAsJSON(missionInfo, file);
                case "yaml", "yml" -> mifModel.saveMIFAsYAML(missionInfo, file);
                default -> {
                    logger.warn("Unknown extension '{}' specified, default to MIF format", extension);
                    mifModel.saveMIF(missionInfo, file, mifSettings.writeEncoding);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to save MIF file", e);
            this.setErrorMessageSave(e.toString());

            return;
        }

        this.setErrorMessageSave("");
    }

    public boolean copyToClipboard() {
        MissionInfo missionInfo = this.toMissionInfo();
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(missionInfo);
        } catch (IOException e) {
            logger.error("Failed to jsonify mission info", e);
            return false;
        }

        Clipboard cb = Clipboard.getSystemClipboard();

        var content = new HashMap<DataFormat, Object>();
        content.put(DataFormat.PLAIN_TEXT, json);

        return cb.setContent(content);
    }

    public void pasteFromClipboard() {
        Clipboard cb = Clipboard.getSystemClipboard();
        if (!cb.hasString()) {
            logger.info("Attempted to paste from clipboard, but no string available in clipboard");
            return;
        }

        MissionInfo missionInfo;
        try {
            missionInfo = new ObjectMapper().readValue(cb.getString(), MissionInfo.class);
        } catch (JsonParseException | JsonMappingException e) {
            logger.info(
                    "Attempted to paste from clipboard, " +
                            "but current content of clipboard is likely to be not from this application");
            return;
        } catch (IOException e) {
            logger.error("Failed to parse the content of the clipboard", e);
            return;
        }

        this.fromMissionInfo(missionInfo);
    }

    public String getMissionShortName() {
        return missionShortName.get();
    }

    public StringProperty missionShortNameProperty() {
        return missionShortName;
    }

    public void setMissionShortName(String missionShortName) {
        this.missionShortName.set(missionShortName);
    }

    public String getMissionLongName() {
        return missionLongName.get();
    }

    public StringProperty missionLongNameProperty() {
        return missionLongName;
    }

    public void setMissionLongName(String missionLongName) {
        this.missionLongName.set(missionLongName);
    }

    public String getBd1Filepath() {
        return bd1Filepath.get();
    }

    public StringProperty bd1FilepathProperty() {
        return bd1Filepath;
    }

    public void setBd1Filepath(String bd1Filepath) {
        this.bd1Filepath.set(bd1Filepath);
    }

    public String getPd1Filepath() {
        return pd1Filepath.get();
    }

    public StringProperty pd1FilepathProperty() {
        return pd1Filepath;
    }

    public void setPd1Filepath(String pd1Filepath) {
        this.pd1Filepath.set(pd1Filepath);
    }

    public SkyType getSkyType() {
        return skyType.get().getSelectedItem().getKey();
    }

    public ObjectProperty<SingleSelectionModel<Pair<SkyType, String>>> skyTypeProperty() {
        return skyType;
    }

    public void setSkyType(SkyType skyType) {
        cbSkyTypeItems
                .stream()
                .filter(p -> p.getKey() == skyType)
                .findFirst()
                .ifPresent(p -> this.skyType.get().select(p));
    }

    public String getImage1Filepath() {
        return image1Filepath.get();
    }

    public StringProperty image1FilepathProperty() {
        return image1Filepath;
    }

    public void setImage1Filepath(String image1Filepath) {
        this.image1Filepath.set(image1Filepath);
    }

    public String getImage2Filepath() {
        return image2Filepath.get();
    }

    public StringProperty image2FilepathProperty() {
        return image2Filepath;
    }

    public void setImage2Filepath(String image2Filepath) {
        this.image2Filepath.set(image2Filepath);
    }

    public String getArticleDefinitionFilepath() {
        return articleDefinitionFilepath.get();
    }

    public StringProperty articleDefinitionFilepathProperty() {
        return articleDefinitionFilepath;
    }

    public void setArticleDefinitionFilepath(String articleDefinitionFilepath) {
        this.articleDefinitionFilepath.set(articleDefinitionFilepath);
    }

    public boolean isExtraHitcheck() {
        return extraHitcheck.get();
    }

    public BooleanProperty extraHitcheckProperty() {
        return extraHitcheck;
    }

    public void setExtraHitcheck(boolean extraHitcheck) {
        this.extraHitcheck.set(extraHitcheck);
    }

    public boolean isDarkScreen() {
        return darkScreen.get();
    }

    public BooleanProperty darkScreenProperty() {
        return darkScreen;
    }

    public void setDarkScreen(boolean darkScreen) {
        this.darkScreen.set(darkScreen);
    }

    public List<String> getMissionBriefing() {
        return missionBriefing.get().lines().toList();
    }

    public StringProperty missionBriefingProperty() {
        return missionBriefing;
    }

    public void setMissionBriefing(List<String> missionBriefing) {
        final String LINE_SEPARATOR = System.lineSeparator();
        var sb = new StringBuilder();
        missionBriefing.forEach(line -> sb.append(line + LINE_SEPARATOR));

        this.missionBriefing.set(sb.toString());
    }

    public void setMissionBriefing(String missionBriefing) {
        this.missionBriefing.set(missionBriefing);
    }

    public String getErrorMessageLoad() {
        return errorMessageLoad.get();
    }

    public StringProperty errorMessageLoadProperty() {
        return errorMessageLoad;
    }

    public void setErrorMessageLoad(String errorMessageLoad) {
        this.errorMessageLoad.set(errorMessageLoad);
    }

    public String getErrorMessageSave() {
        return errorMessageSave.get();
    }

    public StringProperty errorMessageSaveProperty() {
        return errorMessageSave;
    }

    public void setErrorMessageSave(String errorMessageSave) {
        this.errorMessageSave.set(errorMessageSave);
    }
}
