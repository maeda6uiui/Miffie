package com.github.maeda6uiui.miffie;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dabasan.jxm.mif.MissionInfo;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 * Model to handle copy and paste in the main view
 *
 * @author maeda6uiui
 */
public class MainClipboardModel {
    private static final Logger logger = LoggerFactory.getLogger(MainClipboardModel.class);

    public static class WrappedMissionInfo {
        public String appName;
        public MissionInfo missionInfo;

        public WrappedMissionInfo(String appName, MissionInfo missionInfo) {
            this.appName = appName;
            this.missionInfo = missionInfo;
        }
    }

    public boolean copyToClipboard(MissionInfo missionInfo) {
        var wMissionInfo = new WrappedMissionInfo("Miffie", missionInfo);

        String json;
        try {
            json = new ObjectMapper().writeValueAsString(wMissionInfo);
        } catch (IOException e) {
            logger.error("Failed to jsonify mission info", e);
            return false;
        }

        Clipboard cb = Clipboard.getSystemClipboard();

        var content = new HashMap<DataFormat, Object>();
        content.put(DataFormat.PLAIN_TEXT, json);

        return cb.setContent(content);
    }

    public Optional<MissionInfo> parseClipboard() {
        Clipboard cb = Clipboard.getSystemClipboard();
        if (!cb.hasString()) {
            logger.info("No string available in clipboard");
            return Optional.empty();
        }

        WrappedMissionInfo wMissionInfo;
        try {
            wMissionInfo = new ObjectMapper().readValue(cb.getString(), WrappedMissionInfo.class);
        } catch (JsonMappingException e) {
            logger.info("Current content of clipboard is likely to be not from this application");
            return Optional.empty();
        } catch (IOException e) {
            logger.error("Failed to parse the content of the clipboard", e);
            return Optional.empty();
        }

        return Optional.ofNullable(wMissionInfo.missionInfo);
    }
}
