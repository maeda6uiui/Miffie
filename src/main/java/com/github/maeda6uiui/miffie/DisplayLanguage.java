package com.github.maeda6uiui.miffie;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class for display language info
 *
 * @author maeda6uiui
 */
public class DisplayLanguage {
    public static class GeneralAvailable {
        public String name;
        public String code;
    }

    public static class Beta {
        public String name;
        public String code;
    }

    @JsonProperty("ga")
    public List<GeneralAvailable> gaLanguages;
    @JsonProperty("beta")
    public List<Beta> betaLanguages;

    public DisplayLanguage() {
        gaLanguages = new ArrayList<>();
        betaLanguages = new ArrayList<>();
    }
}
