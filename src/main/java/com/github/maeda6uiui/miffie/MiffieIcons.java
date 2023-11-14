package com.github.maeda6uiui.miffie;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class retains icons for the Miffie app.
 *
 * @author maeda6uiui
 */
public class MiffieIcons {
    private static final Logger logger = LoggerFactory.getLogger(MiffieIcons.class);

    public static final String DIRNAME = "./Data/Icon";
    public static final String DEFAULT_ICON_FILENAME = "miffie.png";

    public Map<String, Image> icons;

    private static MiffieIcons instance;

    private MiffieIcons() {
        icons = new HashMap<>();
    }

    public Optional<Image> getIcon(String filename) {
        return Optional.ofNullable(icons.get(filename));
    }

    public void addIconToStage(Stage stage, String filename) {
        this.getIcon(filename).ifPresent(icon -> stage.getIcons().add(icon));
    }

    /**
     * Loads all images in the {@code iconsDirname} directory.
     * If this method fails to load a certain icon image in the directory specified,
     * it will be absent from the resulting map of icon images.
     *
     * @param iconsDirname Directory name to look for icon images
     * @return Icons
     * @throws IOException If it fails to list files
     */
    public static MiffieIcons load(String iconsDirname) throws IOException {
        instance = new MiffieIcons();

        try (Stream<Path> stream = Files.list(Paths.get(iconsDirname))) {
            stream
                    .filter(Files::isRegularFile)
                    .forEach(iconFile -> {
                        var icon = new Image(iconFile.toUri().toString());
                        if (icon.isError()) {
                            logger.error("Failed to load an icon image", icon.getException());
                        } else {
                            instance.icons.put(iconFile.getFileName().toString(), icon);
                        }
                    });
        }

        return instance;
    }

    /**
     * Returns currently retained {@link MiffieIcons} instance.
     *
     * @return Icons
     */
    public static Optional<MiffieIcons> get() {
        return Optional.ofNullable(instance);
    }
}
