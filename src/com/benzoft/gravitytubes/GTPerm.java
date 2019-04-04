package com.benzoft.gravitytubes;

import org.bukkit.entity.Player;

public enum GTPerm {
    USE("gravitytubes.use"),
    COMMANDS("gravitytubes.commands"),
    COMMANDS_HELP("gravitytubes.commands.help"),
    COMMANDS_CREATE("gravitytubes.commands.create"),
    COMMANDS_DELETE("gravitytubes.commands.delete"),
    COMMANDS_SETTINGS("gravitytubes.commands.settings"),
    COMMANDS_INFO("gravitytubes.commands.info"),
    ADMIN("gravitytubes.admin"),
    UPDATE("gravitytubes.update");

    private final String permission;

    GTPerm(final String permission) {
        this.permission = permission;
    }

    public String getPermissionString() {
        return permission;
    }

    public boolean checkPermission(final Player player) {
        return player == null || player.isOp() || player.hasPermission(permission);
    }
}
