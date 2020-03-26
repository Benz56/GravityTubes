package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
            if (args.length > 1) {
                final Integer permissibleHeight = player.getEffectivePermissions().stream()
                        .map(PermissionAttachmentInfo::getPermission)
                        .filter(permission -> permission.startsWith(GTPerm.COMMANDS_SETTINGS.getPermissionString() + ".height."))
                        .flatMap(permission -> {
                            try {
                                return Stream.of(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)));
                            } catch (final NumberFormatException e) {
                                return Stream.empty();
                            }
                        }).max(Integer::compareTo).orElse(Integer.MAX_VALUE);

                height = Math.max(1, Math.min(Integer.parseInt(args[1]), permissibleHeight));
            }
            if (args.length > 2) power = Integer.parseInt(args[2]);
            power = Math.min(power, 127);
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
