package com.benzoft.gravitytubes.commands;

import com.benzoft.gravitytubes.GTPerm;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class AbstractSubCommand {

    private final String name;
    private final Set<String> aliases;
    private final GTPerm permission;
    private final boolean playerOnly;

    protected AbstractSubCommand(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        this.name = name.toLowerCase();
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.aliases = new HashSet<>(Arrays.asList(aliases));
    }

    public abstract void onCommand(final Player player, final String[] args);
}
