package com.github.maeda6uiui.miffie;

import com.github.dabasan.jxm.mif.MIFManipulator;
import com.github.dabasan.jxm.mif.MissionInfo;

import java.io.IOException;

/**
 * Model to handle MIF data
 *
 * @author maeda6uiui
 */
public class MiffieMIFModel {
    public MissionInfo loadMIF(String mifFilepath, String encoding) throws IOException {
        var manipulator = new MIFManipulator(mifFilepath, encoding);
        return manipulator.getMissionInfo();
    }

    public void saveMIF(MissionInfo missionInfo, String mifFilepath, String encoding) throws IOException {
        var manipulator = new MIFManipulator();
        manipulator.setMissionInfo(missionInfo);
        manipulator.saveAsMIF(mifFilepath, encoding);
    }
}
