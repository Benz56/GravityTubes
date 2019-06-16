package com.benzoft.gravitytubes.commands;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractCommand implements CommandExecutor {

    private final GravityTubes gravityTubes;
    private final String commandName;
    private final Set<AbstractSubCommand> subCommands;

    protected AbstractCommand(final GravityTubes gravityTubes, final String commandName, final AbstractSubCommand... subCommands) {
        this.gravityTubes = gravityTubes;
        this.commandName = commandName;
        this.subCommands = new HashSet<>(Arrays.asList(subCommands));
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final Player player = commandSender instanceof Player ? (Player) commandSender : null;

        if (player != null && !GTPerm.COMMANDS.checkPermission(player) && !player.isOp()) {
            MessageUtil.send(player, MessagesFile.getInstance().getNoCommands());
            return true;
        }

        if (args.length != 0) {
            for (final AbstractSubCommand subCommand : subCommands) {
                if (!subCommand.getName().equalsIgnoreCase(args[0]) && subCommand.getAliases().stream().noneMatch(alias -> alias.equalsIgnoreCase(args[0])))
                    continue;
                if (!subCommand.getPermission().checkPermission(player)) {
                    MessageUtil.send(player, MessagesFile.getInstance().getInvalidPermission());
                    return true;
                }
                if (player == null && subCommand.isPlayerOnly()) {
                    MessageUtil.send(null, MessagesFile.getInstance().getPlayerOnly());
                } else subCommand.onCommand(player, args);
                return true;
            }
        }

        onCommand(player, args);
        return true;
    }

    protected abstract void onCommand(final Player player, final String[] args);
}
