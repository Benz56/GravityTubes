package com.benzoft.gravitytubes.commands.gravitytubes;

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
        MessageUtil.send(player, " &7&oBelow is a list of all Gravity Tubes commands:");
        MessageUtil.sendJSON(player, " &7&l● &e/gt [help]", "&eOpens this help page.\n\n&7Click to run.", "/gravitytubes help");
        MessageUtil.sendJSON(player, " &7&l● &e/gt create [height] [power]",
                "&eCreate a gravity tube at the\n&elocation your looking at.\n\n&7Click to suggest.", "/gravitytubes create ", ClickEvent.Action.SUGGEST_COMMAND);
        MessageUtil.sendJSON(player, " &7&l● &e/gt settings <setting> [value...]", "&eAlter a setting on Gravity Tube.\n&eRemove a setting by leaving the value empty.\n&eYou must target the source block.\n" +
                "\n&eSettings:\n" +
                "  &7&l● &aHeight\n" +
                "  &7&l● &aPower (1-127)\n" +
                "  &7&l● &aColor (E.g. yellow or colorcodes)\n" +
                "\n&7Click to suggest.", "/gravitytubes settings ", ClickEvent.Action.SUGGEST_COMMAND);
        MessageUtil.sendJSON(player, " &7&l● &e/gt delete", "&eDelete the gravity tube you're looking at.\n&eThe location has to be the source block.\n\n&7Click to run.", "/gravitytubes delete");
        MessageUtil.sendJSON(player, " &7&l● &e/gt info", "&eGet info about the gravity tube you're looking at.\n&eThe location has to be the source block.\n\n&7Click to run.", "/gravitytubes info");
        MessageUtil.send(player, "&9&m&l----------------------------------");
    }
}
