package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.commands.CommandRegistry;
import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.listeners.BlockBreakListener;
import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class GravityTubes extends JavaPlugin {

    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        updateChecker = new UpdateChecker(this);
        updateChecker.checkForUpdate();
        new Metrics(this);
        getDataFolder().mkdirs();
        ConfigFile.getInstance();
        MessagesFile.getInstance();
        CommandRegistry.registerCommands(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GravityTask(), 0, 1);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> GravityTubesFile.getInstance().getTubes().forEach(GravityTube::spawnParticles), 0, 5);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuit(final PlayerQuitEvent event) {
                PlayerDataManager.removePlayerData(event.getPlayer());
            }
        }, this);
    }

    @Override
    public void onDisable() {
        GravityTubesFile.getInstance().save();
        PlayerDataManager.getPlayerData().values().forEach(playerData -> {
            if (playerData.getGravityTube() != null) {
                playerData.setGravityTube(null);
                playerData.getGravityBar().remove();
                Bukkit.getPlayer(playerData.getUniqueId()).removePotionEffect(PotionEffectType.LEVITATION);
            }
        });
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}
