package com.benzoft.gravitytubes.listeners;

import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event == null || event.getBlock() == null || event.getPlayer() == null) return;
        GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> gravityTube.getSourceLocation().equals(event.getBlock().getLocation())).findFirst().ifPresent(gravityTube -> {
            GravityTubesFile.getInstance().removeTube(gravityTube);
            MessageUtil.send(event.getPlayer(), MessagesFile.getInstance().getTubeRemoved());
        });
    }
}
