package com.benzoft.gravitytubes.hooks;

import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.entity.Player;

public class SpartanHook extends AntiCheatHook {

    @Override
    public void setExempt(final Player player, final boolean exempt) {
        if (exempt) {
            getExemptedPlayers().add(player.getUniqueId());
            API.stopCheck(player, Enums.HackType.Fly);
        } else {
            getExemptedPlayers().remove(player.getUniqueId());
            API.startCheck(player, Enums.HackType.Fly);
        }
    }
}
