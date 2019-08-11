package com.benzoft.gravitytubes.hooks;

import me.rerere.matrix.api.HackType;
import me.rerere.matrix.api.MatrixAPI;
import org.bukkit.entity.Player;

public class MatrixHook extends AntiCheatHook {

    @Override
    public void setExempt(final Player player, final boolean exempt) {
        MatrixAPI.setEnable(HackType.FLY, exempt);
        if (exempt) {
            getExemptedPlayers().add(player.getUniqueId());
        } else getExemptedPlayers().remove(player.getUniqueId());
    }
}
