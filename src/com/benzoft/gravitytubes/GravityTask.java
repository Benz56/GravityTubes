package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GravityTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            final GravityTube tube = GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> gravityTube.isInTube(player)).findFirst().orElse(null);
            if (tube != null) {
                PlayerDataManager.getPlayerData(player, true).ifPresent(playerData -> {
                    playerData.setGravityTube(tube);
                    if (player.isSneaking() && ConfigFile.getInstance().isSneakToFall()) { //TODO slow falling effect or Levitation inverted at 128+.
                        player.removePotionEffect(PotionEffectType.LEVITATION);
                        if (ConfigFile.getInstance().isDisableFallDamage()) player.setFallDistance(0);
                    } else
                        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, Integer.MAX_VALUE, tube.getPower(), false, false)); //TODO Slow down at top (setting?).
                });
            } else {
                PlayerDataManager.getPlayerData(player).ifPresent(playerData -> {
                    if (playerData.getGravityTube() != null) {
                        playerData.setGravityTube(null);
                        player.removePotionEffect(PotionEffectType.LEVITATION);
                    }
                });
            }
        });
    }
}