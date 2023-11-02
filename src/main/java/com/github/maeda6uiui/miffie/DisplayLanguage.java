package com.github.maeda6uiui.miffie;

/**
 * Data class for display language info
 *
 * @author maeda6uiui
 */
public class DisplayLanguage {
    public String code;
    public String localName;
    public String enName;
    public boolean beta = false;

    public String getEnName() {
        return enName;
    }

    @Override
    public String toString() {
        String ret = String.format("%s (%s)", localName, code);
        if (beta) {
            ret += " | beta";
        }

        return ret;
    }
}
