package com.benzoft.gravitytubes.listeners;

import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        PlayerDataManager.getPlayerData(player).ifPresent(playerData -> {
            if (playerData.getGravityTube() != null) {
                playerData.getGravityBar().remove();
                player.removePotionEffect(PotionEffectType.LEVITATION);
            }
            PlayerDataManager.removePlayerData(player);
        });
    }
}
