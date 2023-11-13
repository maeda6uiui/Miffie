package com.github.maeda6uiui.miffie;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * View model for the briefing preview view
 *
 * @author maeda6uiui
 */
public class BriefingPreviewViewModel {
    private static final Logger logger = LoggerFactory.getLogger(BriefingPreviewViewModel.class);

    private StringProperty briefingPreview;

    private MiffieSettings.MIFSettings mifSettings;

    public BriefingPreviewViewModel() {
        briefingPreview = new SimpleStringProperty();

        MiffieSettings.get().ifPresentOrElse(
                settings -> mifSettings = settings.mifSettings,
                () -> {
                    logger.warn("Settings are not available. Fall back to default settings");
                    mifSettings = new MiffieSettings.MIFSettings();
                }
        );
    }

    public boolean trimBriefingText(String briefingText) {
        List<String> lines = Objects.requireNonNullElse(briefingText, "")
                .lines()
                .toList();
        if (lines.size() > mifSettings.maxNumLines) {
            lines = lines.subList(0, mifSettings.maxNumLines);
        }

        Pattern p;
        try {
            p = Pattern.compile(mifSettings.halfWidthCharactersRegex);
        } catch (PatternSyntaxException e) {
            logger.error("Failed to compile the given regex", e);
            return false;
        }

        var trimmedLines = new ArrayList<String>();
        lines.forEach(line -> {
            var halfWidthCharIndices = new HashSet<Integer>();

            Matcher m = p.matcher(line);
            while (m.find()) {
                for (int i = m.start(); i < m.end(); i++) {
                    halfWidthCharIndices.add(i);
                }
            }

            int accumWidthCount = 0;
            var sb = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                int charWidth;
                if (halfWidthCharIndices.contains(i)) {
                    charWidth = 1;
                } else {
                    charWidth = 2;
                }

                if (accumWidthCount + charWidth > mifSettings.maxNumHalfWidthCharactersInLine) {
                    break;
                }

                char c = line.charAt(i);
                sb.append(c);

                accumWidthCount += charWidth;
            }

            trimmedLines.add(sb.toString());
        });

        var sb = new StringBuilder();
        final String LINE_SEPARATOR = System.lineSeparator();
        trimmedLines.forEach(line -> sb.append(line).append(LINE_SEPARATOR));

        this.setBriefingPreview(sb.toString());

        return true;
    }

    public String getBriefingPreview() {
        return briefingPreview.get();
    }

    public StringProperty briefingPreviewProperty() {
        return briefingPreview;
    }

    public void setBriefingPreview(String briefingPreview) {
        this.briefingPreview.set(briefingPreview);
    }
}
