package com.github.maeda6uiui.miffie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class retains a list of {@link MiffieTheme} loaded from a YAML file.
 *
 * @author maeda6uiui
 */
public class MiffieThemes {
    private static final Logger logger = LoggerFactory.getLogger(MiffieThemes.class);

    public List<MiffieTheme> themes;

    private static MiffieThemes instance;

    private MiffieThemes() {
        themes = new ArrayList<>();

        //Default value
        var sysTheme = new MiffieTheme("system", "System");
        themes.add(sysTheme);
    }

    /**
     * Loads list of themes from a YAML file.
     * This method returns default value if the file specified does not exist.
     *
     * @param yamlFilepath Filepath of the YAML file
     * @return Themes
     * @throws IOException If it fails to load from the YAML file specified
     */
    public static MiffieThemes load(String yamlFilepath) throws IOException {
        Path themesFile = Paths.get(yamlFilepath);
        if (!Files.exists(themesFile)) {
            logger.warn("Theme list ({}) was not found, return default list of themes instead", themesFile);

            instance = new MiffieThemes();
            return instance;
        }

        String yaml = Files.readString(themesFile);

        var mapper = new ObjectMapper(new YAMLFactory());
        instance = mapper.readValue(yaml, MiffieThemes.class);

        return instance;
    }

    /**
     * Returns currently retained {@link MiffieThemes} instance.
     *
     * @return Themes
     */
    public static Optional<MiffieThemes> get() {
        return Optional.ofNullable(instance);
    }
}
