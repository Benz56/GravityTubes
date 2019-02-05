package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.commands.CommandRegistry;
import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.listeners.BlockBreakListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GravityTubes extends JavaPlugin {

    @Override
    public void onEnable() {
        //new UpdateChecker(this).checkForUpdate();
        //new Metrics(this);
        getDataFolder().mkdirs();
        ConfigFile.getInstance();
        MessagesFile.getInstance();
        CommandRegistry.registerCommands(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GravityTask(), 0, 1);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> GravityTubesFile.getInstance().getTubes().forEach(GravityTube::spawnParticles), 0, 5);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    @Override
    public void onDisable() {
        GravityTubesFile.getInstance().save();
    }
}
