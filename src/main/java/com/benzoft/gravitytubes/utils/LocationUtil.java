package com.benzoft.gravitytubes.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationUtil {

    public static String locationToString(final Location location) {
        return location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }

    public static Location stringToLocation(final String location) {
        final String[] split = location.split(", ");
        return new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }
}
