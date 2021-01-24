package com.benzoft.gravitytubes.commands.gravitytubes;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTube;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.utils.MessageUtil;
import com.benzoft.gravitytubes.utils.ParticleUtil;
import lombok.Getter;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

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
            final Attribute attribute = Attribute.fromString(args[1]);
            if (attribute != null) {
                if (!GTPerm.COMMANDS_SETTINGS.checkPermission(player) && !GTPerm.COMMANDS_SETTINGS.checkPermission(player, attribute.getFullName())) {
                    MessageUtil.send(player, MessagesFile.getInstance().getInvalidPermission());
                    return;
                }
                switch (attribute) {
                    case HEIGHT:
                        try {
                            final int permissibleHeight = player.getEffectivePermissions().stream()
                                    .map(PermissionAttachmentInfo::getPermission)
                                    .filter(permission -> permission.startsWith(GTPerm.COMMANDS_SETTINGS.getPermissionString() + ".height."))
                                    .flatMap(permission -> {
                                        try {
                                            return Stream.of(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)));
                                        } catch (final NumberFormatException e) {
                                            return Stream.empty();
                                        }
                                    }).max(Integer::compareTo).orElse(Integer.MAX_VALUE);
                            targetTube.setHeight(reset ? Math.min(permissibleHeight, 20) : Math.max(1, Math.min(Integer.parseInt(args[2]), permissibleHeight)));
                            success = true;
                        } catch (final NumberFormatException ignored) {}
                        break;
                    case POWER:
                        try {
                            if (!reset) {
                                targetTube.setPower(Math.min(127, Math.max(1, Integer.parseInt(args[2]))));
                            } else targetTube.setPower(10);
                            success = true;
                        } catch (final NumberFormatException ignored) {}
                        break;
                    case COLOR:
                        final ParticleUtil.GTParticleColor color = reset ? ParticleUtil.GTParticleColor.WHITE : ParticleUtil.getFromColor(args[2]);
                        if (ParticleUtil.isColorable(targetTube.getType())) {
                            if (color != null) {
                                targetTube.setColor(reset ? "white" : args[2]);
                                success = true;
                            }
                        } else {
                            MessageUtil.send(player, MessagesFile.getInstance().getSettingInvalid());
                            return;
                        }
                        break; //TODO stop at top, particle density, pass through blocks.
                    case TYPE:
                        try {
                            final Particle type = reset ? Particle.SPELL_MOB : Particle.valueOf(args[2].toUpperCase());
                            if (ParticleUtil.isSupported(type)) {
                                targetTube.setType(reset ? Particle.SPELL_MOB : Particle.valueOf(args[2].toUpperCase()));
                                success = true;
                            } else {
                                MessageUtil.send(player, MessagesFile.getInstance().getSettingInvalid());
                                return;
                            }
                        } catch (final IllegalArgumentException ignored) {}
                        break;
                }
            }
            MessageUtil.send(player, success ? reset ? MessagesFile.getInstance().getSettingReset() : MessagesFile.getInstance().getSettingSet() : MessagesFile.getInstance().getInvalidArguments());
        } else MessageUtil.send(player, MessagesFile.getInstance().getNoTube());
    }

    @Override
    public List<String> onTabComplete(final Player player, final String[] args) {
        if (args.length == 1) {
            return Stream.of(Attribute.values()).map(Attribute::getFullName).filter(attribute -> attribute.toLowerCase().startsWith(args[0].toLowerCase()) && (GTPerm.COMMANDS_SETTINGS.checkPermission(player) || GTPerm.COMMANDS_SETTINGS.checkPermission(player, attribute))).collect(Collectors.toList());
        } else if (args.length > 1) {
            final Attribute attribute = Attribute.fromString(args[0]);
            if (attribute != null && (GTPerm.COMMANDS_SETTINGS.checkPermission(player) || GTPerm.COMMANDS_SETTINGS.checkPermission(player, attribute.getFullName()))) {
                switch (attribute) {
                    case HEIGHT:
                        return args.length == 2 ? Collections.singletonList("20") : Collections.emptyList();
                    case POWER:
                        return args.length == 2 ? Collections.singletonList("10") : Collections.emptyList();
                    case COLOR:
                        return args.length == 2 ? Stream.of(ParticleUtil.GTParticleColor.values()).map(ParticleUtil.GTParticleColor::getName).filter(color -> color.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList()) : Collections.emptyList();
                    case TYPE:
                        return args.length == 2 ? Stream.of(Particle.values()).filter(ParticleUtil::isSupported).map(Particle::name).filter(type -> type.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList()) : Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }

    private enum Attribute {
        HEIGHT("height", "h"),
        POWER("power", "p"),
        COLOR("color", "c"),
        TYPE("type", "t");

        @Getter
        private final String fullName;
        private final List<String> identifiers;

        Attribute(final String fullName, final String... aliases) {
            this.fullName = fullName;
            identifiers = Arrays.asList(aliases);
        }

        private static Attribute fromString(final String s) {
            return Stream.of(Attribute.values()).filter(attribute -> attribute.fullName.equalsIgnoreCase(s) || attribute.identifiers.contains(s.toLowerCase())).findFirst().orElse(null);
        }
    }
}
