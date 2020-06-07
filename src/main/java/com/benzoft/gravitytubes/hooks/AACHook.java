package com.benzoft.gravitytubes.hooks;

import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationEvent;
import org.bukkit.event.EventHandler;

public class AACHook extends AntiCheatHook {

    @EventHandler
    public void onPlayerViolation(final PlayerViolationEvent event) {
        if (event.getHackType() == HackType.MOVE && getExemptedPlayers().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
