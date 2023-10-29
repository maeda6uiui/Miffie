package com.github.maeda6uiui.miffie;

import atlantafx.base.theme.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Settings
 *
 * @author maeda6uiui
 */
public class MiffieSettings {
    public static class LanguageSettings {
        public String name;

        public LanguageSettings() {
            name = "english";
        }
    }

    public static class WindowSettings {
        public String title;
        public int width;
        public int height;

        public WindowSettings() {
            title = "Miffie - MIF Editor";
            width = 1000;
            height = 400;
        }
    }

    public static class ThemeSettings {
        public String name;

        public ThemeSettings() {
            name = "system";
        }

        public String getCSS() {
            switch (name) {
                case "system":
                    return null;
                case "primer_light":
                    return new PrimerLight().getUserAgentStylesheet();
                case "primer_dark":
                    return new PrimerDark().getUserAgentStylesheet();
                case "nord_light":
                    return new NordLight().getUserAgentStylesheet();
                case "nord_dark":
                    return new NordDark().getUserAgentStylesheet();
                case "cupertino_light":
                    return new CupertinoLight().getUserAgentStylesheet();
                case "cupertino_dark":
                    return new CupertinoDark().getUserAgentStylesheet();
                case "dracula":
                    return new Dracula().getUserAgentStylesheet();
                default:
                    throw new IllegalArgumentException("Unsupported theme: " + name);
            }
        }
    }

    public static class InitialValue {
        public static class MainView {
            public String tfMissionShortName;
            public String tfMissionLongName;
            public String tfBD1Filepath;
            public String tfPD1Filepath;
            public int cbSkyBox;
            public String tfImage1Filepath;
            public String tfImage2Filepath;
            public String tfArticleDefinitionFilepath;
            public boolean ckbExtraHitcheck;
            public boolean ckbDarkScreen;
            public String taMissionBriefing;
        }

        @JsonProperty("main")
        public MainView mainView;

        public InitialValue() {
            mainView = new MainView();
            mainView.tfMissionShortName = "";
            mainView.tfMissionLongName = "";
            mainView.tfBD1Filepath = ".\\";
            mainView.tfPD1Filepath = ".\\addon\\";
            mainView.cbSkyBox = 0;
            mainView.tfImage1Filepath = ".\\data\\briefing\\np.bmp";
            mainView.tfImage2Filepath = "";
            mainView.tfArticleDefinitionFilepath = "";
            mainView.ckbExtraHitcheck = false;
            mainView.ckbDarkScreen = false;
            mainView.taMissionBriefing = "";
        }
    }

    @JsonProperty("language")
    public LanguageSettings languageSettings;
    @JsonProperty("window")
    public WindowSettings windowSettings;
    @JsonProperty("theme")
    public ThemeSettings themeSettings;
    public InitialValue initialValue;

    public static MiffieSettings instance;

    private MiffieSettings() {
        languageSettings = new LanguageSettings();
        windowSettings = new WindowSettings();
        themeSettings = new ThemeSettings();
        initialValue = new InitialValue();
    }

    /**
     * Loads settings from a YAML file.
     * This method returns previously loaded settings if {@code reload==false}
     * and the setting instance currently retained is not null.
     *
     * @param yamlFilepath Filepath of the YAML file
     * @param reload       Reloads settings if true
     * @return Settings
     * @throws IOException If it fails to load from the YAML file specified
     */
    public static MiffieSettings load(String yamlFilepath, boolean reload) throws IOException {
        if (!reload && instance != null) {
            return instance;
        }

        Path settingsFile = Paths.get(yamlFilepath);
        if (!Files.exists(settingsFile)) {
            instance = new MiffieSettings();
            return instance;
        }

        String yaml = Files.readString(settingsFile);

        var mapper = new ObjectMapper(new YAMLFactory());
        instance = mapper.readValue(yaml, MiffieSettings.class);

        return instance;
    }
}
