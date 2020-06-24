package com.benzoft.gravitytubes.hooks;

import me.rerere.matrix.api.HackType;
import me.rerere.matrix.api.MatrixAPIProvider;
import org.bukkit.entity.Player;

public class MatrixHook extends AntiCheatHook {

    @Override
    public void setExempt(final Player player, final boolean exempt) {
        MatrixAPIProvider.getAPI().setEnable(HackType.FLY, exempt);
        if (exempt) {
            getExemptedPlayers().add(player.getUniqueId());
        } else getExemptedPlayers().remove(player.getUniqueId());
    }
}
