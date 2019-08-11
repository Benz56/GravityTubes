package com.benzoft.gravitytubes.hooks;

import com.benzoft.gravitytubes.GravityTubes;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class NoCheatPlusHook extends AntiCheatHook {

    private final Map<UUID, Set<PermissionAttachment>> permissionAttachmentMap = new HashMap<>();
    private final GravityTubes gravityTubes;

    public NoCheatPlusHook(final GravityTubes gravityTubes) {
        this.gravityTubes = gravityTubes;
    }

    @Override
    public void setExempt(final Player player, final boolean exempt) {
        if (player != null) {
            if (!exempt) {
                getExemptedPlayers().remove(player.getUniqueId());
                Optional.ofNullable(permissionAttachmentMap.remove(player.getUniqueId())).ifPresent(permissionAttachments -> permissionAttachments.forEach(PermissionAttachment::remove));
            } else {
                getExemptedPlayers().add(player.getUniqueId());
                permissionAttachmentMap.put(player.getUniqueId(), new HashSet<>(Arrays.asList(
                        player.addAttachment(gravityTubes, "nocheatplus.checks.moving.survivalfly", true),
                        player.addAttachment(gravityTubes, "nocheatplus.checks.moving.creativefly", true)
                )));
            }
        }
    }
}
