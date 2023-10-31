package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.MissionInfo;
import com.github.dabasan.jxm.mif.SkyType;
import javafx.beans.property.*;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public MainViewModel(List<Pair<SkyType, String>> cbSkyTypeItems) {
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

        this.cbSkyTypeItems = cbSkyTypeItems;

        errorMessageLoad = new SimpleStringProperty();
        errorMessageSave = new SimpleStringProperty();

        mifModel = new MiffieMIFModel();

        MiffieSettings.get().ifPresent(settings -> mifSettings = settings.mifSettings);
    }

    public void loadMIF(File file) {
        MissionInfo missionInfo;
        try {
            missionInfo = mifModel.loadMIF(file, mifSettings.readEncoding);
        } catch (IOException e) {
            logger.error("Failed to load MIF file", e);
            this.setErrorMessageLoad(e.toString());

            return;
        }

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

        this.setErrorMessageLoad("");
    }

    public void saveMIF(File file) {
        var missionInfo = new MissionInfo()
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
        try {
            mifModel.saveMIF(missionInfo, file, mifSettings.writeEncoding);
        } catch (IOException e) {
            logger.error("Failed to save MIF file", e);
            this.setErrorMessageSave(e.toString());

            return;
        }

        this.setErrorMessageSave("");
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
