package com.github.maeda6uiui.miffie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.dabasan.jxm.mif.MIFManipulator;
import com.github.dabasan.jxm.mif.MissionInfo;

import java.io.File;
import java.io.IOException;

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

    public MissionInfo loadMIFFromJSON(File file) throws IOException {
        return new ObjectMapper().readValue(file, MissionInfo.class);
    }

    public MissionInfo loadMIFFromYAML(File file) throws IOException {
        return new ObjectMapper(new YAMLFactory()).readValue(file, MissionInfo.class);
    }

    public void saveMIF(MissionInfo missionInfo, File file, String encoding) throws IOException {
        var manipulator = new MIFManipulator();
        manipulator.setMissionInfo(missionInfo);
        manipulator.saveAsMIF(file, encoding);
    }

    public void saveMIFAsJSON(MissionInfo missionInfo, File file) throws IOException {
        new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValue(file, missionInfo);
    }

    public void saveMIFAsYAML(MissionInfo missionInfo, File file) throws IOException {
        new ObjectMapper(new YAMLFactory())
                .writerWithDefaultPrettyPrinter()
                .writeValue(file, missionInfo);
    }
}
