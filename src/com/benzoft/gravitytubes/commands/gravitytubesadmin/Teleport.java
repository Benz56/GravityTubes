package com.benzoft.gravitytubes.commands.gravitytubesadmin;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.LocationUtil;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Comparator;

public class Teleport extends AbstractSubCommand {

    Teleport(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        if (args.length != 5) {
            MessageUtil.send(player, MessagesFile.getInstance().getInvalidArguments());
            return;
        }

        final Location inputLocation;
        try {
            inputLocation = LocationUtil.stringToLocation(args[1] + " " + args[2].split("\\.")[0] + " " + args[3].split("\\.")[0] + " " + args[4].split("\\.")[0]).add(0.5, 0, 0.5);
        } catch (final NumberFormatException e) {
            MessageUtil.send(player, MessagesFile.getInstance().getInvalidArguments());
            return;
        }

        final Location targetTube = GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> gravityTube.getLocation().distance(inputLocation) < 1).map(gravityTube -> gravityTube.getLocation().clone().add(0.5, 0, 0.5)).min(Comparator.comparingDouble(loc -> loc.distance(inputLocation))).orElse(null);
        if (targetTube != null) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    final Location clone = targetTube.clone().add(i, 0, j);
                    if (clone.getWorld().getBlockAt(clone).getType().isSolid() && clone.getWorld().getBlockAt(clone.add(0, 1, 0)).isEmpty() && clone.getWorld().getBlockAt(clone.add(0, 1, 0)).isEmpty()) {
                        final Location destination = clone.subtract(0, 1, 0);
                        destination.setDirection(targetTube.toVector().subtract(destination.toVector().add(new Vector(0, 1, 0))));
                        player.teleport(destination);
                        return;
                    }
                }
            }
            MessageUtil.send(player, MessagesFile.getInstance().getUnsafeTeleport());
        } else MessageUtil.send(player, MessagesFile.getInstance().getInvalidArguments());
    }
}
