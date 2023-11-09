package com.github.maeda6uiui.miffie;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class works as a reservoir of data that may be shared by multiple view models.
 *
 * @author maeda6uiui
 */
public class MiffieReservoir {
    private static Map<String, String> data = new HashMap<>();

    public static Optional<String> get(String key) {
        return Optional.ofNullable(data.get(key));
    }

    public static void put(String key, String value) {
        data.put(key, value);
    }

    public static boolean remove(String key) {
        boolean b = data.containsKey(key);
        if (b) {
            data.remove(key);
        }

        return b;
    }

    public static void clear() {
        data.clear();
    }
}
