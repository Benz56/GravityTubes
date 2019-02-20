package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTube;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import com.benzoft.gravitytubes.utils.ParticleUtil;
import org.bukkit.entity.Player;


public class Settings extends AbstractSubCommand {

    Settings(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        final GravityTube targetTube = GravityTubesFile.getInstance().getTubes().stream().filter(tube -> tube.getLocation().equals(player.getTargetBlock(null, 20).getLocation())).findFirst().orElse(null);
        if (targetTube != null) {
            if (args.length < 2) {
                MessageUtil.send(player, MessagesFile.getInstance().getInvalidArguments());
                return;
            }

            final boolean reset = args.length == 2;
            boolean success = false;
            switch (args[1]) {
                case "height":
                case "h":
                    try {
                        targetTube.setHeight(reset ? 20 : Integer.parseInt(args[2]));
                        success = true;
                    } catch (final NumberFormatException ignored) {}
                    break;
                case "power":
                case "p":
                    try {
                        if (!reset) {
                            final int power = Integer.parseInt(args[2]);
                            targetTube.setPower(power > 127 ? 127 : power < 1 ? 1 : power);
                        } else targetTube.setPower(10);
                        success = true;
                    } catch (final NumberFormatException ignored) {}
                    break;
                case "color":
                case "c":
                    final float[] color = reset ? ParticleUtil.GTParticleColor.WHITE.getRGB() : ParticleUtil.getFromColor(args[2]).getRGB();
                    if (color != null) {
                        targetTube.setColor(reset ? "white" : args[2]);
                        success = true;
                    }
                    break; //TODO stop at top, particle density, pass through blocks.
                default:
                    MessageUtil.send(player, MessagesFile.getInstance().getInvalidArguments());
            }
            MessageUtil.send(player, success ? reset ? MessagesFile.getInstance().getSettingReset() : MessagesFile.getInstance().getSettingSet() : MessagesFile.getInstance().getInvalidArguments());
        } else MessageUtil.send(player, MessagesFile.getInstance().getNoTube());
    }
}
