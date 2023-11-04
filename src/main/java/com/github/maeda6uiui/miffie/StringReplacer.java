package com.github.maeda6uiui.miffie;

/**
 * This class provides functionality to replace items in the given string.
 *
 * @author maeda6uiui
 */
public class StringReplacer {
    private String src;

    public StringReplacer(String src) {
        this.src = src;
    }

    public StringReplacer replace(String ov, String nv) {
        src = src.replace(ov, nv);
        return this;
    }

    @Override
    public String toString() {
        return src;
    }
}
