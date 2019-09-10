package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.commands.CommandRegistry;
import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.hooks.AACHook;
import com.benzoft.gravitytubes.hooks.AntiCheatHook;
import com.benzoft.gravitytubes.hooks.MatrixHook;
import com.benzoft.gravitytubes.hooks.NoCheatPlusHook;
import com.benzoft.gravitytubes.listeners.BlockBreakListener;
import com.benzoft.gravitytubes.listeners.PlayerQuitListener;
import com.benzoft.gravitytubes.listeners.PlayerToggleFlightListener;
import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GravityTubes extends JavaPlugin {

    @Getter
    private UpdateChecker updateChecker;
    private AntiCheatHook antiCheatHook;

    @Override
    public void onEnable() {
        updateChecker = new UpdateChecker(this);
        updateChecker.checkForUpdate();
        new Metrics(this);
        getDataFolder().mkdirs();
        ConfigFile.getInstance();
        MessagesFile.getInstance();
        CommandRegistry.registerCommands(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GravityTask(this), 0, 1);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> GravityTubesFile.getInstance().getTubes().forEach(GravityTube::spawnParticles), 0, 5);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> !gravityTube.hasSourceBlock()).collect(Collectors.toList()).forEach(gravityTube -> GravityTubesFile.getInstance().removeTube(gravityTube)), 10, 40);
        antiCheatHook = Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus") ? new NoCheatPlusHook(this) : null;
        antiCheatHook = Bukkit.getPluginManager().isPluginEnabled("Matrix") ? new MatrixHook() : null;
        antiCheatHook = Bukkit.getPluginManager().isPluginEnabled("AAC") ? new AACHook() : null;
        Stream.of(new BlockBreakListener(), new PlayerQuitListener(), new PlayerToggleFlightListener(), antiCheatHook).filter(Objects::nonNull).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
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
        getAntiCheatHook().ifPresent(AntiCheatHook::onDisable);
    }

    Optional<AntiCheatHook> getAntiCheatHook() {
        return Optional.ofNullable(antiCheatHook);
    }
}
