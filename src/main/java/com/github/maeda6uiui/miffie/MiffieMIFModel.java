package com.github.maeda6uiui.miffie;

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

    public void saveMIF(MissionInfo missionInfo, File file, String encoding) throws IOException {
        var manipulator = new MIFManipulator();
        manipulator.setMissionInfo(missionInfo);
        manipulator.saveAsMIF(file, encoding);
    }
}
