package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.SkyType;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Callback;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * View model for the preferences view
 *
 * @author maeda6uiui
 */
public class PreferencesViewModel {
    private static final Logger logger = LoggerFactory.getLogger(PreferencesViewModel.class);

    private ObjectProperty<SingleSelectionModel<Pair<SkyType, String>>> ivSkyType;
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
    private IntegerProperty mMaxNumHalfWidthCharactersInLine;
    private IntegerProperty mMaxNumLines;
    private StringProperty mReadEncoding;
    private StringProperty mWriteEncoding;
    private StringProperty mHalfWidthCharactersRegex;
    private StringProperty tCustomThemeFilepath;
    private IntegerProperty wWindowHeight;
    private IntegerProperty wWindowWidth;

    private List<Pair<SkyType, String>> cbIVSkyTypeItems;
    private List<DisplayLanguage> cbLDisplayLanguageItems;
    private List<MiffieTheme> cbTThemeItems;

    private String currentUserStylesheet;

    public PreferencesViewModel() {
        ivSkyType = new SimpleObjectProperty<>();
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
        mMaxNumHalfWidthCharactersInLine = new SimpleIntegerProperty();
        mMaxNumLines = new SimpleIntegerProperty();
        mReadEncoding = new SimpleStringProperty();
        mWriteEncoding = new SimpleStringProperty();
        mHalfWidthCharactersRegex = new SimpleStringProperty();
        wWindowHeight = new SimpleIntegerProperty();
        wWindowWidth = new SimpleIntegerProperty();

        currentUserStylesheet = Application.getUserAgentStylesheet();
    }

    /**
     * Populates the view.
     * Call this method after binding is done.
     *
     * @param resources          Text resources
     * @param cbIVSkyType        Combobox for sky type
     * @param cbLDisplayLanguage Combobox for display language
     * @param cbTTheme           Combobox for window theme
     * @return {@code true} if success, {@code false} if error
     */
    public boolean populate(
            ResourceBundle resources,
            ComboBox<Pair<SkyType, String>> cbIVSkyType,
            ComboBox<DisplayLanguage> cbLDisplayLanguage,
            ComboBox<MiffieTheme> cbTTheme) {
        //Combobox
        cbIVSkyTypeItems = new ArrayList<>();
        cbIVSkyTypeItems.add(new Pair<>(SkyType.NONE, resources.getString("cbIVSkyType.text.none")));
        cbIVSkyTypeItems.add(new Pair<>(SkyType.SUNNY, resources.getString("cbIVSkyType.text.sunny")));
        cbIVSkyTypeItems.add(new Pair<>(SkyType.CLOUDY, resources.getString("cbIVSkyType.text.cloudy")));
        cbIVSkyTypeItems.add(new Pair<>(SkyType.NIGHT, resources.getString("cbIVSkyType.text.night")));
        cbIVSkyTypeItems.add(new Pair<>(SkyType.EVENING, resources.getString("cbIVSkyType.text.evening")));
        cbIVSkyTypeItems.add(new Pair<>(SkyType.WILDERNESS, resources.getString("cbIVSkyType.text.wilderness")));

        cbIVSkyType.getItems().addAll(cbIVSkyTypeItems);
        cbIVSkyType.setValue(cbIVSkyTypeItems.get(0));
        Callback<ListView<Pair<SkyType, String>>, ListCell<Pair<SkyType, String>>> skyTypeFactory
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
        cbIVSkyType.setCellFactory(skyTypeFactory);
        cbIVSkyType.setButtonCell(skyTypeFactory.call(null));

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
            this.setlDisplayLanguage(settings.languageSettings.code);

            this.settTheme(settings.themeSettings.name);
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
            this.setmHalfWidthCharactersRegex(mifSettings.halfWidthCharactersRegex);

            this.setwWindowHeight(settings.windowSettings.height);
            this.setwWindowWidth(settings.windowSettings.width);
        });

        return true;
    }

    /**
     * Applies selected theme for preview.
     *
     * @return {@code true} if success, {@code false} if error
     */
    public boolean previewSelectedTheme() {
        MiffieTheme selectedTheme = this.gettTheme();

        var themeSettings = new MiffieSettings.ThemeSettings();
        themeSettings.name = selectedTheme.name();
        if (selectedTheme.name().equals("custom")) {
            themeSettings.fromFile = this.gettCustomThemeFilepath();
        }

        Optional<String> css = themeSettings.getCSS();
        if (css.isPresent()) {
            Application.setUserAgentStylesheet(css.get());
            return true;
        } else {
            logger.warn("Failed to load and apply CSS");
            return false;
        }
    }

    public void revertToPreviousTheme() {
        Application.setUserAgentStylesheet(currentUserStylesheet);
    }

    public boolean isMIFReadEncodingSupported() {
        try {
            return Charset.isSupported(this.getmReadEncoding());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean isMIFWriteEncodingSupported() {
        try {
            return Charset.isSupported(this.getmWriteEncoding());
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Validates the given regex to detect half-width characters.
     *
     * @return Empty string if valid, otherwise error message
     */
    public String validateMIFHalfWidthCharactersRegex() {
        try {
            Pattern.compile(this.getmHalfWidthCharactersRegex());
        } catch (PatternSyntaxException e) {
            return e.getMessage();
        }

        return "";
    }

    public boolean saveSettings() {
        //Create a new instance of settings and populate it
        var settings = new MiffieSettings();

        settings.languageSettings.code = this.getlDisplayLanguage().code;

        settings.windowSettings.width = this.getwWindowWidth();
        settings.windowSettings.height = this.getwWindowHeight();

        settings.themeSettings.name = this.gettTheme().name();
        settings.themeSettings.fromFile = this.gettCustomThemeFilepath();

        MiffieSettings.InitialValue.MainView ivMainView = settings.initialValue.mainView;
        ivMainView.tfMissionShortName = this.getIvMissionShortName();
        ivMainView.tfMissionLongName = this.getIvMissionLongName();
        ivMainView.tfBD1Filepath = this.getIvBD1Filepath();
        ivMainView.tfPD1Filepath = this.getIvPD1Filepath();
        ivMainView.cbSkyType = this.getIvSkyType();
        ivMainView.tfImage1Filepath = this.getIvImage1Filepath();
        ivMainView.tfImage2Filepath = this.getIvImage2Filepath();
        ivMainView.tfArticleDefinitionFilepath = this.getIvArticleDefinitionFilepath();
        ivMainView.ckbExtraHitcheck = this.isIvExtraHitcheck();
        ivMainView.ckbDarkScreen = this.isIvDarkScreen();
        ivMainView.taMissionBriefing = this.getIvMissionBriefing();

        MiffieSettings.MIFSettings mifSettings = settings.mifSettings;
        mifSettings.maxNumLines = this.getmMaxNumLines();
        mifSettings.maxNumHalfWidthCharactersInLine = this.getmMaxNumHalfWidthCharactersInLine();
        mifSettings.readEncoding = this.getmReadEncoding();
        mifSettings.writeEncoding = this.getmWriteEncoding();
        mifSettings.halfWidthCharactersRegex = this.getmHalfWidthCharactersRegex();

        //Save the settings
        try {
            settings.save(MiffieSettings.FILEPATH);
        } catch (IOException e) {
            logger.error("Failed to save the settings", e);
            return false;
        }

        return true;
    }

    public SkyType getIvSkyType() {
        return ivSkyType.get().getSelectedItem().getKey();
    }

    public ObjectProperty<SingleSelectionModel<Pair<SkyType, String>>> ivSkyTypeProperty() {
        return ivSkyType;
    }

    public void setIvSkyType(SkyType skyType) {
        cbIVSkyTypeItems
                .stream()
                .filter(p -> p.getKey() == skyType)
                .findFirst()
                .ifPresent(p -> this.ivSkyType.get().select(p));
    }

    public DisplayLanguage getlDisplayLanguage() {
        return lDisplayLanguage.get().getSelectedItem();
    }

    public ObjectProperty<SingleSelectionModel<DisplayLanguage>> lDisplayLanguageProperty() {
        return lDisplayLanguage;
    }

    public void setlDisplayLanguage(DisplayLanguage language) {
        this.lDisplayLanguage.get().select(language);
    }

    public void setlDisplayLanguage(String languageCode) {
        cbLDisplayLanguageItems
                .stream()
                .filter(p -> p.code.equals(languageCode))
                .findFirst()
                .ifPresent(p -> this.lDisplayLanguage.get().select(p));
    }

    public MiffieTheme gettTheme() {
        return tTheme.get().getSelectedItem();
    }

    public ObjectProperty<SingleSelectionModel<MiffieTheme>> tThemeProperty() {
        return tTheme;
    }

    public void settTheme(MiffieTheme theme) {
        this.tTheme.get().select(theme);
    }

    public void settTheme(String themeName) {
        cbTThemeItems
                .stream()
                .filter(p -> p.name().equals(themeName))
                .findFirst()
                .ifPresent(p -> this.tTheme.get().select(p));
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

    public String getmHalfWidthCharactersRegex() {
        return mHalfWidthCharactersRegex.get();
    }

    public StringProperty mHalfWidthCharactersRegexProperty() {
        return mHalfWidthCharactersRegex;
    }

    public void setmHalfWidthCharactersRegex(String mHalfWidthCharactersRegex) {
        this.mHalfWidthCharactersRegex.set(mHalfWidthCharactersRegex);
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
}
