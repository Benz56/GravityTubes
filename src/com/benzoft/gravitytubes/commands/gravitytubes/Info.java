package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTube;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.LocationUtil;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Info extends AbstractSubCommand {

    Info(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        final GravityTube target = GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> gravityTube.getLocation().equals(player.getTargetBlock(null, 20).getLocation())).findFirst().orElse(null);

        if (target != null) {
            Arrays.asList(
                    "&9&m&l---&e Gravity Tube Info &9&m&l--------",
                    " &7- &eLocation: &a" + LocationUtil.locationToString(target.getLocation()),
                    " &7- &eHeight: &a" + target.getHeight(),
                    " &7- &ePower: &a" + target.getPower(),
                    " &7- &eColor: &a" + target.getColor().getName()
            ).forEach(line -> MessageUtil.send(player, line));
        } else MessageUtil.send(player, MessagesFile.getInstance().getNoTube());
    }
}
