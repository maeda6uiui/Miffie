package com.github.maeda6uiui.miffie;

import atlantafx.base.theme.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Settings
 *
 * @author maeda6uiui
 */
public class MiffieSettings {
    private static final Logger logger = LoggerFactory.getLogger(MiffieSettings.class);

    public static class LanguageSettings {
        public String code;

        public LanguageSettings() {
            code = "en";
        }
    }

    public static class WindowSettings {
        public int width;
        public int height;

        public WindowSettings() {
            width = 1000;
            height = 400;
        }
    }

    public static class ThemeSettings {
        public String name;
        public String fromFile;

        public ThemeSettings() {
            name = "primer_light";
        }

        private boolean isValidString(String s) {
            return s != null && !s.isEmpty();
        }

        /**
         * Returns CSS string.
         * This method first attempts to return built-in CSS if {@code name} is provided.
         * If it fails to look up built-in CSS or if {@code name} is not provided,
         * then it refers to {@code fromFile} and attempts to read CSS from file.
         * This method returns empty value if both attempts are failed or
         * if neither {@code name} nor {@code fromFile} is provided.
         *
         * @return CSS string
         */
        public Optional<String> getCSS() {
            if (!this.isValidString(name) && !this.isValidString(fromFile)) {
                return Optional.empty();
            }

            if (this.isValidString(name)) {
                String css = null;
                switch (name) {
                    case "primer_light":
                        css = new PrimerLight().getUserAgentStylesheet();
                        break;
                    case "primer_dark":
                        css = new PrimerDark().getUserAgentStylesheet();
                        break;
                    case "nord_light":
                        css = new NordLight().getUserAgentStylesheet();
                        break;
                    case "nord_dark":
                        css = new NordDark().getUserAgentStylesheet();
                        break;
                    case "cupertino_light":
                        css = new CupertinoLight().getUserAgentStylesheet();
                        break;
                    case "cupertino_dark":
                        css = new CupertinoDark().getUserAgentStylesheet();
                        break;
                    case "dracula":
                        css = new Dracula().getUserAgentStylesheet();
                        break;
                    default:
                        logger.warn("Unsupported theme '{}' specified", name);
                        break;
                }

                if (css != null) {
                    return Optional.of(css);
                }
            }
            if (this.isValidString(fromFile)) {
                String css = null;
                try {
                    Path cssFile = Paths.get(fromFile);
                    css = Files.readString(cssFile);
                } catch (IOException e) {
                    logger.warn("Failed to load CSS from file ({})", fromFile);
                }

                if (css != null) {
                    return Optional.of(css);
                }
            }

            logger.warn("Failed to get CSS string");
            return Optional.empty();
        }
    }

    public static class InitialValue {
        public static class MainView {
            public String tfMissionShortName;
            public String tfMissionLongName;
            public String tfBD1Filepath;
            public String tfPD1Filepath;
            public int cbSkyType;
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
            mainView.cbSkyType = 0;
            mainView.tfImage1Filepath = ".\\data\\briefing\\np.bmp";
            mainView.tfImage2Filepath = "!";
            mainView.tfArticleDefinitionFilepath = "!";
            mainView.ckbExtraHitcheck = false;
            mainView.ckbDarkScreen = false;
            mainView.taMissionBriefing = "";
        }
    }

    public static class MIFSettings {
        public int maxNumLines;
        public int maxNumHalfWidthCharactersInLine;
        public String readEncoding;
        public String writeEncoding;

        public MIFSettings() {
            maxNumLines = 10;
            maxNumHalfWidthCharactersInLine = 30;
            readEncoding = "shift-jis";
            writeEncoding = "shift-jis";
        }
    }

    @JsonProperty("language")
    public LanguageSettings languageSettings;
    @JsonProperty("window")
    public WindowSettings windowSettings;
    @JsonProperty("theme")
    public ThemeSettings themeSettings;
    public InitialValue initialValue;
    @JsonProperty("mif")
    public MIFSettings mifSettings;

    public static MiffieSettings instance;

    private MiffieSettings() {
        languageSettings = new LanguageSettings();
        windowSettings = new WindowSettings();
        themeSettings = new ThemeSettings();
        initialValue = new InitialValue();
        mifSettings = new MIFSettings();
    }

    /**
     * Loads settings from a YAML file.
     * This method returns default settings if the file specified does not exist.
     *
     * @param yamlFilepath Filepath of the YAML file
     * @return Settings
     * @throws IOException If it fails to load from the YAML file specified
     */
    public static MiffieSettings load(String yamlFilepath) throws IOException {
        Path settingsFile = Paths.get(yamlFilepath);
        if (!Files.exists(settingsFile)) {
            logger.warn("Setting file ({}) was not found, return default settings instead", settingsFile);

            instance = new MiffieSettings();
            return instance;
        }

        String yaml = Files.readString(settingsFile);

        var mapper = new ObjectMapper(new YAMLFactory());
        instance = mapper.readValue(yaml, MiffieSettings.class);

        return instance;
    }

    /**
     * Returns currently retained {@link MiffieSettings} instance.
     *
     * @return Settings
     */
    public static Optional<MiffieSettings> get() {
        return Optional.ofNullable(instance);
    }
}
