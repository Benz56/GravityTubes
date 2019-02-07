package com.benzoft.gravitytubes.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.stream.Stream;

public final class ParticleUtil {

    public enum GTParticleColor {
        BLACK(new float[]{0, 0, 0}, "&0"),
        DARK_GREEN(new float[]{0, 0.66667F, 0}, "&2"),
        DARK_AQUA(new float[]{0, 0.66667F, 0.66667F}, "&3"),
        GOLD(new float[]{1, 0.66667F, 0}, "&6"),
        GRAY(new float[]{0.66667F, 0.66667F, 0.66667F}, "&7"),
        DARK_GRAY(new float[]{0.33333F, 0.33333F, 0.33333F}, "&8"),
        BLUE(new float[]{0.33333F, 0.33333F, 1}, "&9"),
        GREEN(new float[]{0.33333F, 1, 0.33333F}, "&a"),
        AQUA(new float[]{0.33333F, 1, 1}, "&b"),
        RED(new float[]{1, 0.33333F, 0.33333F}, "&c"),
        LIGHT_PURPLE(new float[]{1, 0.33333F, 1}, "&d"),
        YELLOW(new float[]{1, 1, 0.33333F}, "&e"),
        WHITE(new float[]{1, 1, 1}, "&f");

        private final float[] rgb;
        private final String colorCode;

        GTParticleColor(final float[] rgb, final String colorCode) {
            this.rgb = rgb;
            this.colorCode = colorCode;
        }

        public String getName() {
            return toString().substring(0, 1).toUpperCase() + toString().substring(1).toLowerCase();
        }

        public float[] getRgb() {
            return rgb;
        }

        public String getColorCode() {
            return colorCode;
        }
    }

    public static GTParticleColor getFromColor(final String color) {
        return Stream.of(GTParticleColor.values()).filter(gtParticleColor -> gtParticleColor.getColorCode().equalsIgnoreCase(color) || gtParticleColor.toString().equalsIgnoreCase(color)).findFirst().orElse(null);
    }

    public static void spawnParticle(final Location location, final GTParticleColor color) {
        if (Bukkit.getVersion().contains("1.13")) {
            location.getWorld().getPlayers().stream().filter(player -> player.getLocation().distance(location) < 40).forEach(player -> player.spawnParticle(Particle.SPELL_MOB, location, 0, color.getRgb()[0], color.getRgb()[1], color.getRgb()[2], 10, null));
        } else location.getWorld().spawnParticle(Particle.SPELL_MOB, location, 0, color.getRgb()[0], color.getRgb()[1], color.getRgb()[2], 10, null);
    }
}
