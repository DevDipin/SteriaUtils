package net.helydev.com.utils;

import net.helydev.com.SteriaUtils;

public class ConfigEnd {
    public static SteriaUtils plugin;

    public static String getString(final String string) {
        return ConfigEnd.plugin.getConfig().getString("setend." + string);
    }
}
