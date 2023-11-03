package com.github.maeda6uiui.miffie;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * View model for the preferences view
 *
 * @author maeda6uiui
 */
public class PreferencesViewModel {
    private static final Logger logger = LoggerFactory.getLogger(PreferencesViewModel.class);

    private ObjectProperty<SingleSelectionModel<DisplayLanguage>> lDisplayLanguage;
    private ObjectProperty<SingleSelectionModel<MiffieTheme>> tTheme;
    private BooleanProperty ivDarkScreen;
    private BooleanProperty ivExtraHitcheck;
    private StringProperty ivMissionBriefing;
    private StringProperty ivArticleDefinitionFilepath;
    private StringProperty ivBD1Filepath;
    private StringProperty ivImage1Filepath;
    private StringProperty ivImage2Filepath;
    private StringProperty ivMissionLongName;
    private StringProperty ivMissionShortName;
    private StringProperty ivPD1Filepath;
    private IntegerProperty ivSkyType;
    private IntegerProperty mMaxNumHalfWidthCharactersInLine;
    private IntegerProperty mMaxNumLines;
    private StringProperty mReadEncoding;
    private StringProperty mWriteEncoding;
    private StringProperty tCustomThemeFilepath;
    private IntegerProperty wWindowHeight;
    private IntegerProperty wWindowWidth;

    private List<DisplayLanguage> cbLDisplayLanguageItems;
    private List<MiffieTheme> cbTThemeItems;


    private BooleanProperty errorPreviewTheme;
    private String currentUserStylesheet;

    public PreferencesViewModel() {
        lDisplayLanguage = new SimpleObjectProperty<>();
        tTheme = new SimpleObjectProperty<>();
        tCustomThemeFilepath = new SimpleStringProperty();
        ivDarkScreen = new SimpleBooleanProperty();
        ivExtraHitcheck = new SimpleBooleanProperty();
        ivMissionBriefing = new SimpleStringProperty();
        ivArticleDefinitionFilepath = new SimpleStringProperty();
        ivBD1Filepath = new SimpleStringProperty();
        ivImage1Filepath = new SimpleStringProperty();
        ivImage2Filepath = new SimpleStringProperty();
        ivMissionLongName = new SimpleStringProperty();
        ivMissionShortName = new SimpleStringProperty();
        ivPD1Filepath = new SimpleStringProperty();
        ivSkyType = new SimpleIntegerProperty();
        mMaxNumHalfWidthCharactersInLine = new SimpleIntegerProperty();
        mMaxNumLines = new SimpleIntegerProperty();
        mReadEncoding = new SimpleStringProperty();
        mWriteEncoding = new SimpleStringProperty();
        wWindowHeight = new SimpleIntegerProperty();
        wWindowWidth = new SimpleIntegerProperty();

        errorPreviewTheme = new SimpleBooleanProperty();
        currentUserStylesheet = Application.getUserAgentStylesheet();
    }

    /**
     * Populate the view.
     * Call this method after binding is done.
     *
     * @param cbLDisplayLanguage Combobox for display language
     * @param cbTTheme           Combobox for window theme
     * @return {@code true} if success, {@code false} if error
     */
    public boolean populate(
            ComboBox<DisplayLanguage> cbLDisplayLanguage,
            ComboBox<MiffieTheme> cbTTheme) {
        //Combobox
        if (DisplayLanguages.get().isEmpty()) {
            logger.error("Cannot populate the preferences view because language list is empty");
            return false;
        }
        if (MiffieThemes.get().isEmpty()) {
            logger.error("Cannot populate the preferences view because theme list is empty");
            return false;
        }

        cbLDisplayLanguageItems = DisplayLanguages.get().get().getSortedList();
        cbLDisplayLanguage.getItems().addAll(cbLDisplayLanguageItems);
        cbLDisplayLanguage.setValue(cbLDisplayLanguageItems.get(0));
        Callback<ListView<DisplayLanguage>, ListCell<DisplayLanguage>> displayLanguageFactory
                = lv -> new ListCell<>() {
            @Override
            protected void updateItem(DisplayLanguage item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText("");
                } else {
                    setText(item.toString());
                }
            }
        };
        cbLDisplayLanguage.setCellFactory(displayLanguageFactory);
        cbLDisplayLanguage.setButtonCell(displayLanguageFactory.call(null));

        cbTThemeItems = MiffieThemes.get().get().themes;
        cbTTheme.getItems().addAll(cbTThemeItems);
        cbTTheme.setValue(cbTThemeItems.get(0));
        Callback<ListView<MiffieTheme>, ListCell<MiffieTheme>> themeFactory
                = lv -> new ListCell<>() {
            @Override
            protected void updateItem(MiffieTheme item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText("");
                } else {
                    setText(item.displayName());
                }
            }
        };
        cbTTheme.setCellFactory(themeFactory);
        cbTTheme.setButtonCell(themeFactory.call(null));

        //Initial value
        MiffieSettings.get().ifPresent(settings -> {
            cbLDisplayLanguageItems
                    .stream()
                    .filter(p -> p.code.equals(settings.languageSettings.code))
                    .findFirst()
                    .ifPresent(this::setlDisplayLanguage);

            cbTThemeItems
                    .stream()
                    .filter(p -> p.name().equals(settings.themeSettings.name))
                    .findFirst()
                    .ifPresent(this::settTheme);
            this.settCustomThemeFilepath(settings.themeSettings.fromFile);

            MiffieSettings.InitialValue.MainView ivMainView = settings.initialValue.mainView;
            this.setIvDarkScreen(ivMainView.ckbDarkScreen);
            this.setIvExtraHitcheck(ivMainView.ckbExtraHitcheck);
            this.setIvMissionBriefing(ivMainView.taMissionBriefing);
            this.setIvArticleDefinitionFilepath(ivMainView.tfArticleDefinitionFilepath);
            this.setIvBD1Filepath(ivMainView.tfBD1Filepath);
            this.setIvImage1Filepath(ivMainView.tfImage1Filepath);
            this.setIvImage2Filepath(ivMainView.tfImage2Filepath);
            this.setIvMissionLongName(ivMainView.tfMissionLongName);
            this.setIvMissionShortName(ivMainView.tfMissionShortName);
            this.setIvPD1Filepath(ivMainView.tfPD1Filepath);
            this.setIvSkyType(ivMainView.cbSkyType);

            MiffieSettings.MIFSettings mifSettings = settings.mifSettings;
            this.setmMaxNumHalfWidthCharactersInLine(mifSettings.maxNumHalfWidthCharactersInLine);
            this.setmMaxNumLines(mifSettings.maxNumLines);
            this.setmReadEncoding(mifSettings.readEncoding);
            this.setmWriteEncoding(mifSettings.writeEncoding);

            this.setwWindowHeight(settings.windowSettings.height);
            this.setwWindowWidth(settings.windowSettings.width);
        });

        return true;
    }

    public void previewSelectedTheme() {
        MiffieTheme selectedTheme = this.gettTheme().getSelectedItem();

        var themeSettings = new MiffieSettings.ThemeSettings();
        themeSettings.name = selectedTheme.name();
        if (selectedTheme.name().equals("custom")) {
            themeSettings.fromFile = this.gettCustomThemeFilepath();
        }

        Optional<String> css = themeSettings.getCSS();
        css.ifPresentOrElse(
                s -> {
                    Application.setUserAgentStylesheet(s);
                    this.setErrorPreviewTheme(false);
                },
                () -> {
                    this.setErrorPreviewTheme(true);
                    logger.warn("Failed to load and apply CSS");
                }
        );
    }

    public void revertToPreviousTheme() {
        Application.setUserAgentStylesheet(currentUserStylesheet);
    }

    public boolean isMIFReadEncodingSupported() {
        return Charset.isSupported(mReadEncoding.get());
    }

    public boolean isMIFWriteEncodingSupported() {
        return Charset.isSupported(mWriteEncoding.get());
    }

    public SingleSelectionModel<DisplayLanguage> getlDisplayLanguage() {
        return lDisplayLanguage.get();
    }

    public ObjectProperty<SingleSelectionModel<DisplayLanguage>> lDisplayLanguageProperty() {
        return lDisplayLanguage;
    }

    public void setlDisplayLanguage(DisplayLanguage displayLanguage) {
        this.lDisplayLanguage.get().select(displayLanguage);
    }

    public SingleSelectionModel<MiffieTheme> gettTheme() {
        return tTheme.get();
    }

    public ObjectProperty<SingleSelectionModel<MiffieTheme>> tThemeProperty() {
        return tTheme;
    }

    public void settTheme(MiffieTheme theme) {
        this.tTheme.get().select(theme);
    }

    public boolean isIvDarkScreen() {
        return ivDarkScreen.get();
    }

    public BooleanProperty ivDarkScreenProperty() {
        return ivDarkScreen;
    }

    public void setIvDarkScreen(boolean ivDarkScreen) {
        this.ivDarkScreen.set(ivDarkScreen);
    }

    public boolean isIvExtraHitcheck() {
        return ivExtraHitcheck.get();
    }

    public BooleanProperty ivExtraHitcheckProperty() {
        return ivExtraHitcheck;
    }

    public void setIvExtraHitcheck(boolean ivExtraHitcheck) {
        this.ivExtraHitcheck.set(ivExtraHitcheck);
    }

    public String getIvMissionBriefing() {
        return ivMissionBriefing.get();
    }

    public StringProperty ivMissionBriefingProperty() {
        return ivMissionBriefing;
    }

    public void setIvMissionBriefing(String ivMissionBriefing) {
        this.ivMissionBriefing.set(ivMissionBriefing);
    }

    public String getIvArticleDefinitionFilepath() {
        return ivArticleDefinitionFilepath.get();
    }

    public StringProperty ivArticleDefinitionFilepathProperty() {
        return ivArticleDefinitionFilepath;
    }

    public void setIvArticleDefinitionFilepath(String ivArticleDefinitionFilepath) {
        this.ivArticleDefinitionFilepath.set(ivArticleDefinitionFilepath);
    }

    public String getIvBD1Filepath() {
        return ivBD1Filepath.get();
    }

    public StringProperty ivBD1FilepathProperty() {
        return ivBD1Filepath;
    }

    public void setIvBD1Filepath(String ivBD1Filepath) {
        this.ivBD1Filepath.set(ivBD1Filepath);
    }

    public String getIvImage1Filepath() {
        return ivImage1Filepath.get();
    }

    public StringProperty ivImage1FilepathProperty() {
        return ivImage1Filepath;
    }

    public void setIvImage1Filepath(String ivImage1Filepath) {
        this.ivImage1Filepath.set(ivImage1Filepath);
    }

    public String getIvImage2Filepath() {
        return ivImage2Filepath.get();
    }

    public StringProperty ivImage2FilepathProperty() {
        return ivImage2Filepath;
    }

    public void setIvImage2Filepath(String ivImage2Filepath) {
        this.ivImage2Filepath.set(ivImage2Filepath);
    }

    public String getIvMissionLongName() {
        return ivMissionLongName.get();
    }

    public StringProperty ivMissionLongNameProperty() {
        return ivMissionLongName;
    }

    public void setIvMissionLongName(String ivMissionLongName) {
        this.ivMissionLongName.set(ivMissionLongName);
    }

    public String getIvMissionShortName() {
        return ivMissionShortName.get();
    }

    public StringProperty ivMissionShortNameProperty() {
        return ivMissionShortName;
    }

    public void setIvMissionShortName(String ivMissionShortName) {
        this.ivMissionShortName.set(ivMissionShortName);
    }

    public String getIvPD1Filepath() {
        return ivPD1Filepath.get();
    }

    public StringProperty ivPD1FilepathProperty() {
        return ivPD1Filepath;
    }

    public void setIvPD1Filepath(String ivPD1Filepath) {
        this.ivPD1Filepath.set(ivPD1Filepath);
    }

    public int getIvSkyType() {
        return ivSkyType.get();
    }

    public IntegerProperty ivSkyTypeProperty() {
        return ivSkyType;
    }

    public void setIvSkyType(int ivSkyType) {
        this.ivSkyType.set(ivSkyType);
    }

    public int getmMaxNumHalfWidthCharactersInLine() {
        return mMaxNumHalfWidthCharactersInLine.get();
    }

    public IntegerProperty mMaxNumHalfWidthCharactersInLineProperty() {
        return mMaxNumHalfWidthCharactersInLine;
    }

    public void setmMaxNumHalfWidthCharactersInLine(int mMaxNumHalfWidthCharactersInLine) {
        this.mMaxNumHalfWidthCharactersInLine.set(mMaxNumHalfWidthCharactersInLine);
    }

    public int getmMaxNumLines() {
        return mMaxNumLines.get();
    }

    public IntegerProperty mMaxNumLinesProperty() {
        return mMaxNumLines;
    }

    public void setmMaxNumLines(int mMaxNumLines) {
        this.mMaxNumLines.set(mMaxNumLines);
    }

    public String getmReadEncoding() {
        return mReadEncoding.get();
    }

    public StringProperty mReadEncodingProperty() {
        return mReadEncoding;
    }

    public void setmReadEncoding(String mReadEncoding) {
        this.mReadEncoding.set(mReadEncoding);
    }

    public String getmWriteEncoding() {
        return mWriteEncoding.get();
    }

    public StringProperty mWriteEncodingProperty() {
        return mWriteEncoding;
    }

    public void setmWriteEncoding(String mWriteEncoding) {
        this.mWriteEncoding.set(mWriteEncoding);
    }

    public String gettCustomThemeFilepath() {
        return tCustomThemeFilepath.get();
    }

    public StringProperty tCustomThemeFilepathProperty() {
        return tCustomThemeFilepath;
    }

    public void settCustomThemeFilepath(String tCustomThemeFilepath) {
        this.tCustomThemeFilepath.set(tCustomThemeFilepath);
    }

    public int getwWindowHeight() {
        return wWindowHeight.get();
    }

    public IntegerProperty wWindowHeightProperty() {
        return wWindowHeight;
    }

    public void setwWindowHeight(int wWindowHeight) {
        this.wWindowHeight.set(wWindowHeight);
    }

    public int getwWindowWidth() {
        return wWindowWidth.get();
    }

    public IntegerProperty wWindowWidthProperty() {
        return wWindowWidth;
    }

    public void setwWindowWidth(int wWindowWidth) {
        this.wWindowWidth.set(wWindowWidth);
    }

    public boolean getErrorPreviewTheme() {
        return errorPreviewTheme.get();
    }

    public BooleanProperty errorPreviewThemeProperty() {
        return errorPreviewTheme;
    }

    public void setErrorPreviewTheme(boolean errorPreviewTheme) {
        this.errorPreviewTheme.set(errorPreviewTheme);
    }
}
