package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.runtimedata.PlayerData;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class GravityBar {

    private final PlayerData playerData;
    private final Player player;
    private BossBar bossBar;

    public GravityBar(final PlayerData playerData) {
        this.playerData = playerData;
        player = Bukkit.getPlayer(playerData.getUniqueId());
    }

    void update() {
        if (!ConfigFile.getInstance().isBossBarEnabled() || playerData.getGravityTube() == null) return;
        bossBar = bossBar != null ? bossBar : Bukkit.createBossBar("", ConfigFile.getInstance().getBossBarColor(), ConfigFile.getInstance().getBossBarStyle());
        final double yLow = playerData.getGravityTube().getSourceLocation().getY();
        final double yHigh = playerData.getGravityTube().getSourceLocation().getY() + playerData.getGravityTube().getHeight();
        double progress = (player.getLocation().getY() - yLow) / (yHigh - yLow);
        progress = progress < 0D || player.isOnGround() ? 0 : progress > 1D ? 1 : progress;
        bossBar.setProgress(progress);
        bossBar.setTitle(MessageUtil.translate(ConfigFile.getInstance().getBossBarTitle().replaceAll("%percentage%", String.valueOf(Math.round(progress * 100)))));
        bossBar.addPlayer(player);
    }

    void remove() {
        if (bossBar != null) {
            if (player != null && player.isOnline()) bossBar.removePlayer(player);
            bossBar = null;
        }
    }
}
