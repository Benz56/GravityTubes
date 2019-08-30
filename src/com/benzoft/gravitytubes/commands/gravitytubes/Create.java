package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Create extends AbstractSubCommand {

    Create(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        final Block target = player.getTargetBlock(null, 20);

        int height = 20;
        int power = 5;

        if (target.getType() == Material.AIR) {
            MessageUtil.send(player, MessagesFile.getInstance().getCantCreate());
            return;
        }

        try {
            if (args.length > 1) height = Integer.parseInt(args[1]);
            if (args.length > 2) power = Integer.parseInt(args[2]);
            power = power > 127 ? 127 : power;
            if (height < 1 || power < 1) throw new NumberFormatException();
        } catch (final NumberFormatException e) {
            MessageUtil.send(player, MessagesFile.getInstance().getInvalidArguments());
            return;
        }

        MessageUtil.send(player, MessagesFile.getInstance().getTubeCreated());
        GravityTubesFile.getInstance().addTube(player.getUniqueId(), target.getLocation(), height, power);
    }

    @Override
    public List<String> onTabComplete(final Player player, final String[] args) {
        return args.length == 1 ? Collections.singletonList("20") : args.length == 2 ? Collections.singletonList("5") : Collections.emptyList();
    }
}
