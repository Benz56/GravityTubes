package com.benzoft.gravitytubes.hooks;

import me.rerere.matrix.api.HackType;
import me.rerere.matrix.api.events.PlayerViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.util.HashSet;
import java.util.Set;

public class MatrixHook extends AntiCheatHook {

    private final Set<HackType> hackTypes = new HashSet<>();

    public MatrixHook() {
        // Circumventing the changes to the HackType enum in V6 and forward in Matrix.
        final boolean preV6 = Bukkit.getServer().getPluginManager().getPlugin("Matrix").getDescription().getVersion().charAt(0) < '6';
        hackTypes.add(preV6 ? HackType.FLY : HackType.valueOf("MOVE"));
        hackTypes.add(HackType.VELOCITY);
    }

    @EventHandler
    public void onViolation(final PlayerViolationEvent event) {
        if (hackTypes.contains(event.getHackType()) && getExemptedPlayers().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
