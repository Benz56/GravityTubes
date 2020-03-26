package com.benzoft.gravitytubes.files;

import com.benzoft.gravitytubes.GravityTubes;
import lombok.Getter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public final class ConfigFile {

    private static ConfigFile file;

    private final boolean disableFallDamage;
    private final boolean sneakToFall;
    private final boolean updateCheckerEnabled;
    private final boolean updateCheckerPermissionOnly;
    private final boolean bossBarEnabled;
    private final String bossBarTitle;
    private final BarColor bossBarColor;
    private final BarStyle bossBarStyle;
    private final boolean disableInCombat;

    private ConfigFile() {
        GravityTubes.getPlugin(GravityTubes.class).saveDefaultConfig();
        final FileConfiguration config = GravityTubes.getPlugin(GravityTubes.class).getConfig();
        sneakToFall = config.getBoolean("SneakToFall", true);
        disableFallDamage = config.getBoolean("DisableFallDamage", true);
        updateCheckerEnabled = config.getBoolean("UpdateCheckerEnabled", true);
        updateCheckerPermissionOnly = config.getBoolean("UpdateCheckerPermissionOnly", false);
        bossBarEnabled = config.getBoolean("BossBarEnabled", true);
        bossBarColor = BarColor.valueOf(config.getString("BossBarColor", "RED"));
        bossBarStyle = BarStyle.valueOf(config.getString("BossBarStyle", "SOLID"));
        bossBarTitle = config.getString("BossBarTitle", "&l%percentage%%");
        disableInCombat = config.getBoolean("DisableInCombat", false);
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
}
