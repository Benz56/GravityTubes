package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTube;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.entity.Player;

public class Delete extends AbstractSubCommand {

    Delete(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        final GravityTube target = GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> gravityTube.getLocation().equals(player.getTargetBlock(null, 20).getLocation())).findFirst().orElse(null);

        if (target != null) {
            GravityTubesFile.getInstance().removeTube(target);
            MessageUtil.send(player, MessagesFile.getInstance().getTubeRemoved());
        } else MessageUtil.send(player, MessagesFile.getInstance().getNoTube());
    }
}
