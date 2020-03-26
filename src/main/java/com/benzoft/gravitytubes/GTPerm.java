package com.benzoft.gravitytubes;

import lombok.Getter;
import org.bukkit.permissions.Permissible;

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

    @Getter
    private final String permissionString;

    GTPerm(final String permissionString) {
        this.permissionString = permissionString;
    }

    public boolean checkPermission(final Permissible permissible) {
        return permissible == null || permissible.isOp() || permissible.hasPermission(permissionString);
    }

    public boolean checkPermission(final Permissible permissible, final String extension) {
        return permissible == null || permissible.isOp() || permissible.hasPermission(extension == null || extension.isEmpty() ? permissionString : permissionString + "." + extension);
    }
}
