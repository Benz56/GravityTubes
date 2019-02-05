package com.benzoft.gravitytubes.commands.gravitytubesadmin;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.utils.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.entity.Player;

public class Help extends AbstractSubCommand {

    Help(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        MessageUtil.send(player, "&9&m&l----------------------------------");
        MessageUtil.send(player, " &7&oBelow is a list of all Gravity Tubes Admin commands:");
        MessageUtil.sendJSON(player, " &7&l● &e/gta [help]", "&eOpens this help page.\n\n&7Click to run.", "/gravitytubesadmin help", ClickEvent.Action.RUN_COMMAND);
        MessageUtil.sendJSON(player, " &7&l● &e/gta reload", "&eReload the configuration files.\\n\\n&7Click to run.", "/gravitytubesadmin reload", ClickEvent.Action.RUN_COMMAND);
        MessageUtil.send(player, "&9&m&l----------------------------------");
    }
}
