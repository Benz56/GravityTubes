package com.benzoft.gravitytubes.commands.gravitytubesadmin;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.commands.AbstractCommand;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.entity.Player;

public class GTACommand extends AbstractCommand {

    public GTACommand(final GravityTubes gravityTubes, final String commandName) {
        super(gravityTubes, commandName,
                new Help("help", GTPerm.ADMIN, false, "h"),
                new GTList("list", GTPerm.ADMIN, false, "li", "l"),
                new Reload(gravityTubes, "reload", GTPerm.ADMIN, false, "rel", "r")
        );
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        if (GTPerm.ADMIN.checkPermission(player)) {
            getSubCommands().stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase("help")).findFirst().ifPresent(subCommand -> subCommand.onCommand(player, args));
        } else MessageUtil.send(player, MessagesFile.getInstance().getNoCommands());
    }
}
