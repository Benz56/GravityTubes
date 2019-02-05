package com.benzoft.gravitytubes.utils;

import org.bukkit.Bukkit;

import java.util.Arrays;

public final class StringUtil {

    public static void debug(final Object... o) {
        Bukkit.getServer().getLogger().info(Arrays.toString(o));
    }
}