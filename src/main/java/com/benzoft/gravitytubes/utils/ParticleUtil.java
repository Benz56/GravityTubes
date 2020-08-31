package com.benzoft.gravitytubes.utils;

import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

import lombok.Getter;

public final class ParticleUtil {

    public static GTParticleColor getFromColor(final String color) {
        return Stream.of(GTParticleColor.values()).filter(gtParticleColor -> gtParticleColor.getColorCode().equalsIgnoreCase(color) || gtParticleColor.toString().equalsIgnoreCase(color)).findFirst().orElse(null);
    }
    
    /**
     * @deprecated Use {@link #spawnParticle(Particle, Location, GTParticleColor)}
     */
    @Deprecated
    public static void spawnParticle(final Location location, final GTParticleColor color) {
        spawnParticle(Particle.SPELL_MOB, location, color);
    }
    
    public static void spawnParticle(final Particle type, final Location location, final GTParticleColor color) {
        if (!isSupported(type)) return;
        final float r = isColorable(type) ? color.getR() : 0;
        final float g = isColorable(type) ? color.getG() : 0;
        final float b = isColorable(type) ? color.getB() : 0;
        if (Stream.of("1.8", "1.9", "1.10", "1.11", "1.12").noneMatch(Bukkit.getVersion()::contains)) {
            if (type.getDataType().getSimpleName().equals("DustOptions")) {
                location.getWorld().spawnParticle(type, location, 0, 0, 0, 0, 1, 
                        new Particle.DustOptions(org.bukkit.Color.fromRGB((int)(r*255.999), (int)(g*255.999), (int)(b*255.999)), 1));
            } else location.getWorld().spawnParticle(type, location, 0, r, g, b, 10, null);
        } else location.getWorld().getPlayers().stream().filter(player -> player.getLocation().distance(location) < 40)
        .forEach(player -> player.spawnParticle(type, location, 0, r, g, b, 10, null));
    }
    
    public static boolean isSupported(final Particle type) {
        switch(type.getDataType().getSimpleName()) {
        case "Void":
        case "DustOptions":
            return true;
        default:
            return false;
        }
    }
    
    public static boolean isColorable(final Particle type) {
        switch(type) {
        case SPELL_MOB:
        case SPELL_MOB_AMBIENT:
        case REDSTONE:
            return true;
        default:
            return false;
        }
    }

    @Getter
    public enum GTParticleColor {
        BLACK("&0", 0, 0, 0),
        DARK_GREEN("&2", 0, 0.66667F, 0),
        DARK_AQUA("&3", 0, 0.66667F, 0.66667F),
        GOLD("&6", 1, 0.66667F, 0),
        GRAY("&7", 0.66667F, 0.66667F, 0.66667F),
        DARK_GRAY("&8", 0.33333F, 0.33333F, 0.33333F),
        BLUE("&9", 0.33333F, 0.33333F, 1),
        GREEN("&a", 0.33333F, 1, 0.33333F),
        AQUA("&b", 0.33333F, 1, 1),
        RED("&c", 1, 0.33333F, 0.33333F),
        LIGHT_PURPLE("&d", 1, 0.33333F, 1),
        YELLOW("&e", 1, 1, 0.33333F),
        WHITE("&f", 1, 1, 1);

        private final float[] rgb;
        private final String colorCode;

        GTParticleColor(final String colorCode, final float... rgb) {
            this.rgb = rgb;
            this.colorCode = colorCode;
        }

        public String getName() {
            return toString().substring(0, 1).toUpperCase() + toString().substring(1).toLowerCase();
        }

        public float getR() {
            return rgb[0];
        }

        public float getG() {
            return rgb[1];
        }

        public float getB() {
            return rgb[2];
        }
    }
}
