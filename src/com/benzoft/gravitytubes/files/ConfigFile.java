package com.benzoft.gravitytubes.files;

import com.benzoft.gravitytubes.GravityTubes;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigFile {

    private static ConfigFile file;

    private final boolean disableFallDamage;
    private final boolean sneakToFall;
    private final boolean updateCheckerEnabled;
    private final boolean updateCheckerPermissionOnly;

    private ConfigFile() {
        GravityTubes.getPlugin(GravityTubes.class).saveDefaultConfig();
        final FileConfiguration config = GravityTubes.getPlugin(GravityTubes.class).getConfig();
        sneakToFall = config.getBoolean("SneakToFall", true);
        disableFallDamage = config.getBoolean("DisableFallDamage", true);
        updateCheckerEnabled = config.getBoolean("UpdateCheckerEnabled", true);
        updateCheckerPermissionOnly = config.getBoolean("UpdateCheckerPermissionOnly", false);
    }


    public static ConfigFile getInstance() {
        file = file == null ? new ConfigFile() : file;
        return file;
    }

    public static void reload(final GravityTubes gravityTubes) {
        gravityTubes.reloadConfig();
        gravityTubes.saveDefaultConfig();
        file = new ConfigFile();
    }

    public boolean isDisableFallDamage() {
        return disableFallDamage;
    }

    public boolean isSneakToFall() {
        return sneakToFall;
    }

    public boolean isUpdateCheckerEnabled() {
        return updateCheckerEnabled;
    }

    public boolean isUpdateCheckerPermissionOnly() {
        return updateCheckerPermissionOnly;
    }
}
