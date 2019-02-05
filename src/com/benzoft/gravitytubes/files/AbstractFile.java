package com.benzoft.gravitytubes.files;

import com.benzoft.gravitytubes.GravityTubes;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

abstract class AbstractFile {

    private final File file;
    private final YamlConfiguration config;

    AbstractFile(final String name) {
        file = new File(GravityTubes.getPlugin(GravityTubes.class).getDataFolder(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException ignored) {
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    Object add(final String path, final Object value) {
        if (config.contains(path)) return config.get(path);
        config.set(path, value);
        return value;
    }

    Object conditionalAdd(final String path, final Object value, final boolean canAdd) {
        if (config.contains(path) || !canAdd) return config.get(path);
        config.set(path, value);
        return value;
    }

    boolean containsSection(final String path) {
        return config.contains(path);
    }

    void setHeader(final String... lines) {
        config.options().header(String.join("\n", lines) + "\n");
    }

    File getFile() {
        return file;
    }

    YamlConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void setDefaults();
}
