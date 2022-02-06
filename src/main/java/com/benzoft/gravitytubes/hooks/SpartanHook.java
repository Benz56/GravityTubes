package com.benzoft.gravitytubes.hooks;

import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class SpartanHook extends AntiCheatHook {

    private static final Enums.HackType HACK_TYPE = Stream.of(Enums.HackType.values()).anyMatch(ht -> ht.toString().equals("Fly")) ? Enums.HackType.Fly : Enums.HackType.IrregularMovements;

    @Override
    public void setExempt(final Player player, final boolean exempt) {
        if (exempt) {
            getExemptedPlayers().add(player.getUniqueId());
            API.stopCheck(player, HACK_TYPE);
        } else {
            getExemptedPlayers().remove(player.getUniqueId());
            API.startCheck(player, HACK_TYPE);
        }
    }
}
