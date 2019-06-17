package com.benzoft.gravitytubes.commands.gravitytubesadmin;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.LocationUtil;
import com.benzoft.gravitytubes.utils.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GTList extends AbstractSubCommand {

    private static final int ENTRIES_PER_PAGE = 8;

    GTList(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        int pages = (int) Math.ceil(((double) GravityTubesFile.getInstance().getTubes().size()) / ((double) ENTRIES_PER_PAGE));
        pages = pages == 0 ? 1 : pages;
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
                if (page > pages || page < 1) throw new NumberFormatException();
            } catch (final NumberFormatException e) {
                MessageUtil.send(player, MessagesFile.getInstance().getInvalidArguments());
                return;
            }
        }

        MessageUtil.send(player, "&9&m&l-----&9 Page: &c" + page + "/" + pages + " &9&m&l--------------");
        final int startIndex = (page - 1) * ENTRIES_PER_PAGE;
        final int endIndex = startIndex + 8 > GravityTubesFile.getInstance().getTubes().size() ? GravityTubesFile.getInstance().getTubes().size() : startIndex + 8;
        GravityTubesFile.getInstance().getTubes().subList(startIndex, endIndex).forEach(gravityTube -> MessageUtil.sendJSON(player, " &7&l● &e" + LocationUtil.locationToString(gravityTube.getSourceLocation()), gravityTube.getInfo() + "\n\n&7Click to teleport", "/gravitytubesadmin teleport " + LocationUtil.locationToString(gravityTube.getSourceLocation())));
        if (page != pages) {
            MessageUtil.sendJSON(player, "&eUse &c/gta list " + (page + 1) + "&e to go to the next page.", "&7Click to go to the next page.", "/gta list " + (page + 1), ClickEvent.Action.RUN_COMMAND);
        }
    }

    @Override
    public java.util.List<String> onTabComplete(final Player player, final String[] args) {
        if (args.length == 1) {
            final int pages = (int) Math.ceil(((double) GravityTubesFile.getInstance().getTubes().size()) / ((double) ENTRIES_PER_PAGE));
            return IntStream.range(1, pages == 0 ? 2 : pages + 1).mapToObj(String::valueOf).collect(Collectors.toList());
        } else return Collections.emptyList();
    }
}
