package com.benzoft.gravitytubes.hooks;

import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class NoCheatPlus implements IHook, Listener {

    private final GravityTubes gravityTubes;
    private final Map<UUID, Set<PermissionAttachment>> exemptedPlayers = new HashMap<>();

    public NoCheatPlus(final GravityTubes gravityTubes) {
        this.gravityTubes = gravityTubes;
    }

    @Override
    public void onDisable() {
        exemptedPlayers.keySet().stream().map(Bukkit::getPlayer).forEach(player -> setExemptPlayer(player, false));
    }

    public void setExemptPlayer(final Player player, final boolean exempt) {
        if (player != null) {
            if (!exempt) {
                Optional.ofNullable(exemptedPlayers.remove(player.getUniqueId())).ifPresent(permissionAttachments -> permissionAttachments.forEach(PermissionAttachment::remove));
            } else exemptedPlayers.put(player.getUniqueId(), new HashSet<>(Arrays.asList(
                    player.addAttachment(gravityTubes, "nocheatplus.checks.moving.survivalfly", true),
                    player.addAttachment(gravityTubes, "nocheatplus.checks.moving.creativefly", true)
            )));
        }
    }

    /**
     * Un-exempts the player once they start falling again after being launched at the top of the tube to avoid rubber-banding.
     */
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        PlayerDataManager.getPlayerData(event.getPlayer(), false).ifPresent(playerData -> {
            if (playerData.getGravityTube() == null && exemptedPlayers.containsKey(event.getPlayer().getUniqueId()) && event.getTo().getY() < event.getFrom().getY()) {
                setExemptPlayer(event.getPlayer(), false);
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        setExemptPlayer(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerToggleFlight(final PlayerToggleFlightEvent event) {
        PlayerDataManager.getPlayerData(event.getPlayer(), false).ifPresent(playerData -> {
            if (playerData.getGravityTube() == null && exemptedPlayers.containsKey(event.getPlayer().getUniqueId())) {
                setExemptPlayer(event.getPlayer(), false);
            }
        });
    }
}
