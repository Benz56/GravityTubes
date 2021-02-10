package com.benzoft.gravitytubes.hooks;

import me.rerere.matrix.api.HackType;
import me.rerere.matrix.api.MatrixAPIProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MatrixHook extends AntiCheatHook {

    private final HackType hackType;

    public MatrixHook() {
        // Circumventing the changes to the HackType enum in V6 and forward in Matrix.
        final boolean preV6 = Bukkit.getServer().getPluginManager().getPlugin("Matrix").getDescription().getVersion().charAt(0) < '6';
        hackType = preV6 ? HackType.FLY : HackType.valueOf("MOVE");
    }

    @Override
    public void setExempt(final Player player, final boolean exempt) {
        MatrixAPIProvider.getAPI().setEnable(hackType, exempt);
        if (exempt) {
            getExemptedPlayers().add(player.getUniqueId());
        } else getExemptedPlayers().remove(player.getUniqueId());
    }
}
