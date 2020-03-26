package com.benzoft.gravitytubes.listeners;

import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerToggleFlightListener implements Listener {

    @EventHandler
    public void onToggleFlight(final PlayerToggleFlightEvent event) {
        PlayerDataManager.getPlayerData(event.getPlayer(), false).ifPresent(playerData -> {
            if (playerData.getGravityTube() != null) {
                event.setCancelled(true);
            }
        });
    }
}
