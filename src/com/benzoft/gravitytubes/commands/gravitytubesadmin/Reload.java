package com.benzoft.gravitytubes.commands.gravitytubesadmin;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.entity.Player;

public class Reload extends AbstractSubCommand {

    private final GravityTubes gravityTubes;

    Reload(final GravityTubes gravityTubes, final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
        this.gravityTubes = gravityTubes;
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        ConfigFile.reload(gravityTubes);
        GravityTubesFile.getInstance().save();
        GravityTubesFile.reload();
        MessagesFile.reload();
        MessageUtil.send(player, MessagesFile.getInstance().getConfigReload());
    }
}
