package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.commands.AbstractCommand;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.entity.Player;

public class GTCommand extends AbstractCommand {

    public GTCommand(final GravityTubes gravityTubes, final String commandName) {
        super(gravityTubes, commandName,
                new Help("help", GTPerm.COMMANDS_HELP, false, "h"),
                new Create("create", GTPerm.COMMANDS_CREATE, true, "cr", "c"),
                new Delete("delete", GTPerm.COMMANDS_DELETE, true, "del", "d", "remove", "rem", "r"),
                new Settings("settings", GTPerm.COMMANDS_SETTINGS, true, "set", "s")
        );
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        if (GTPerm.COMMANDS_HELP.checkPermission(player)) {
            getSubCommands().stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase("help")).findFirst().ifPresent(subCommand -> subCommand.onCommand(player, args));
        } else MessageUtil.send(player, MessagesFile.getInstance().getInvalidPermission());
    }
}
