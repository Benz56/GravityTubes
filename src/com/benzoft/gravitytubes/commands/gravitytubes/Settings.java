package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTube;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import com.benzoft.gravitytubes.utils.ParticleUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Settings extends AbstractSubCommand {

    Settings(final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        final GravityTube targetTube = GravityTubesFile.getInstance().getTubes().stream().filter(tube -> tube.getSourceLocation().equals(player.getTargetBlock(null, 20).getLocation())).findFirst().orElse(null);
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
                    final ParticleUtil.GTParticleColor color = reset ? ParticleUtil.GTParticleColor.WHITE : ParticleUtil.getFromColor(args[2]);
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

    @Override
    public List<String> onTabComplete(final Player player, final String[] args) {
        if (args.length == 1) {
            return Stream.of(Attribute.values()).map(Attribute::getPath).filter(attribute -> attribute.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length > 1) {
            final Attribute attribute = Attribute.fromString(args[0]);
            if (attribute != null) {
                switch (attribute) {
                    case HEIGHT:
                        return args.length == 2 ? Collections.singletonList("20") : Collections.emptyList();
                    case POWER:
                        return args.length == 2 ? Collections.singletonList("10") : Collections.emptyList();
                    case COLOR:
                        return args.length == 2 ? Stream.of(ParticleUtil.GTParticleColor.values()).map(ParticleUtil.GTParticleColor::getName).filter(color -> color.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList()) : Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }

    private enum Attribute {
        HEIGHT("height", "h"),
        POWER("power", "p"),
        COLOR("color", "c");

        private final String path;
        private final List<String> identifiers;

        Attribute(final String path, final String... aliases) {
            this.path = path;
            identifiers = Arrays.asList(aliases);
        }

        private static Attribute fromString(final String s) {
            return Stream.of(Attribute.values()).filter(attribute -> attribute.path.equalsIgnoreCase(s) || attribute.identifiers.contains(s.toLowerCase())).findFirst().orElse(null);
        }

        private String getPath() {
            return path;
        }
    }
}
