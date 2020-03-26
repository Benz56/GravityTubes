package com.benzoft.gravitytubes.commands;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractCommand implements TabExecutor {

    @Getter(AccessLevel.NONE)
    private final GravityTubes gravityTubes;
    private final String commandName;
    private final Set<AbstractSubCommand> subCommands;

    protected AbstractCommand(final GravityTubes gravityTubes, final String commandName, final AbstractSubCommand... subCommands) {
        this.gravityTubes = gravityTubes;
        this.commandName = commandName;
        this.subCommands = new HashSet<>(Arrays.asList(subCommands));
    }

    protected abstract void onCommand(final Player player, final String[] args);

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
                if (subCommand.getPermission() != null && !subCommand.getPermission().checkPermission(player)) {
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

    @Override
    public java.util.List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (args.length > 1) {
            return subCommands.stream().filter(abstractSubCommand -> abstractSubCommand.getPermission() == null || abstractSubCommand.getPermission().checkPermission(sender) && (abstractSubCommand.getName().equalsIgnoreCase(args[0]) || abstractSubCommand.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(args[0])))).findFirst().map(abstractSubCommand -> abstractSubCommand.onTabComplete(sender instanceof Player ? (Player) sender : null, Arrays.copyOfRange(args, 1, args.length))).orElse(null);
        } else
            return GTPerm.COMMANDS.checkPermission(sender) ? subCommands.stream().filter(abstractSubCommand -> abstractSubCommand.getPermission() == null || abstractSubCommand.getPermission().checkPermission(sender)).map(AbstractSubCommand::getName).filter(name -> name.startsWith(args[0].toLowerCase())).collect(Collectors.toList()) : null;
    }
}
