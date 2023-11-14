package com.github.maeda6uiui.miffie;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * This class retains a list of {@link DisplayLanguage} loaded from a YAML file.
 *
 * @author maeda6uiui
 */
public class DisplayLanguages {
    private static final Logger logger = LoggerFactory.getLogger(DisplayLanguages.class);

    public static final String FILEPATH = "./Data/languages.yaml";

    @JsonProperty("languages")
    public final List<DisplayLanguage> displayLanguages;

    private static DisplayLanguages instance;

    private DisplayLanguages() {
        displayLanguages = new ArrayList<>();

        //Default value
        var en = new DisplayLanguage();
        en.code = "en";
        en.localName = "English";
        en.enName = "English";
        en.beta = false;
        displayLanguages.add(en);
    }

    public List<DisplayLanguage> getSortedList() {
        return displayLanguages
                .stream()
                .sorted(Comparator.comparing(DisplayLanguage::getEnName))
                .toList();
    }

    /**
     * Loads list of languages from a YAML file.
     * This method returns default value if the file specified does not exist.
     *
     * @param yamlFilepath Filepath of the YAML file
     * @return Display languages
     * @throws IOException If it fails to load from the YAML file specified
     */
    public static DisplayLanguages load(String yamlFilepath) throws IOException {
        Path languagesFile = Paths.get(yamlFilepath);
        if (!Files.exists(languagesFile)) {
            logger.warn("Language list ({}) was not found, return default list of languages instead", languagesFile);

            instance = new DisplayLanguages();
            return instance;
        }

        String yaml = Files.readString(languagesFile);

        var mapper = new ObjectMapper(new YAMLFactory());
        instance = mapper.readValue(yaml, DisplayLanguages.class);

        return instance;
    }

    /**
     * Returns currently retained {@link DisplayLanguages} instance.
     *
     * @return Display languages
     */
    public static Optional<DisplayLanguages> get() {
        return Optional.ofNullable(instance);
    }
}
