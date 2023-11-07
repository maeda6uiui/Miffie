package com.github.maeda6uiui.miffie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.dabasan.jxm.mif.MIFManipulator;
import com.github.dabasan.jxm.mif.MissionInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Model to handle MIF data
 *
 * @author maeda6uiui
 */
public class MiffieMIFModel {
    public MissionInfo loadMIF(File file, String encoding) throws IOException {
        var manipulator = new MIFManipulator(file, encoding);
        return manipulator.getMissionInfo();
    }

    public void saveMIF(MissionInfo missionInfo, File file, String encoding) throws IOException {
        var manipulator = new MIFManipulator();
        manipulator.setMissionInfo(missionInfo);
        manipulator.saveAsMIF(file, encoding);
    }

    public void saveMIFAsJSON(MissionInfo missionInfo, File file) throws IOException {
        var mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(missionInfo);
        Files.writeString(file.toPath(), json);
    }

    public void saveMIFAsYAML(MissionInfo missionInfo, File file) throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());
        String yaml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(missionInfo);
        Files.writeString(file.toPath(), yaml);
    }
}
