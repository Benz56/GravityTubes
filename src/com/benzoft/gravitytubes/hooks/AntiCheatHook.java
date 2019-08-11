package com.benzoft.gravitytubes.hooks;

import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public abstract class AntiCheatHook implements Listener {

    private final Set<UUID> exemptedPlayers = new HashSet<>();

    public abstract void setExempt(final Player player, final boolean exempt);

    public void onDisable() {
        exemptedPlayers.stream().map(Bukkit::getPlayer).forEach(player -> setExempt(player, false));
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        setExempt(event.getPlayer(), false);
    }

    /**
     * Un-exempts the player once they start falling again after being launched at the top of the tube to avoid rubber-banding.
     */
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        PlayerDataManager.getPlayerData(event.getPlayer(), false).ifPresent(playerData -> {
            if (playerData.getGravityTube() == null && exemptedPlayers.contains(event.getPlayer().getUniqueId()) && event.getTo().getY() < event.getFrom().getY()) {
                setExempt(event.getPlayer(), false);
            }
        });
    }

    @EventHandler
    public void onPlayerToggleFlight(final PlayerToggleFlightEvent event) {
        PlayerDataManager.getPlayerData(event.getPlayer(), false).ifPresent(playerData -> {
            if (playerData.getGravityTube() == null && exemptedPlayers.contains(event.getPlayer().getUniqueId())) {
                setExempt(event.getPlayer(), false);
            }
        });
    }
}
