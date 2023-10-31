package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.SkyType;
import javafx.beans.property.*;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Pair;

import java.util.List;

/**
 * View model for the main view
 *
 * @author maeda6uiui
 */
public class MiffieMainViewModel {
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

    private MiffieMIFModel mifModel;

    public MiffieMainViewModel(List<Pair<SkyType, String>> cbSkyTypeItems) {
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

        mifModel = new MiffieMIFModel();
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

    public SingleSelectionModel<Pair<SkyType, String>> getSkyType() {
        return skyType.get();
    }

    public ObjectProperty<SingleSelectionModel<Pair<SkyType, String>>> skyTypeProperty() {
        return skyType;
    }

    public void setSkyType(SingleSelectionModel<Pair<SkyType, String>> skyType) {
        this.skyType.set(skyType);
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

    public String getMissionBriefing() {
        return missionBriefing.get();
    }

    public StringProperty missionBriefingProperty() {
        return missionBriefing;
    }

    public void setMissionBriefing(String missionBriefing) {
        this.missionBriefing.set(missionBriefing);
    }
}
