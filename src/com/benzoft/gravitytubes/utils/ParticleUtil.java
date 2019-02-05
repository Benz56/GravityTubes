package com.benzoft.gravitytubes.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

public final class ParticleUtil {

    public static void spawnParticle(final Location location, final float[] rgb) {
        if (Bukkit.getVersion().contains("1.13")) {
            location.getWorld().getPlayers().stream().filter(player -> player.getLocation().distance(location) < 40).forEach(player -> player.spawnParticle(Particle.SPELL_MOB, location, 0, rgb[0], rgb[1], rgb[2], 10, null));
        } else location.getWorld().spawnParticle(Particle.SPELL_MOB, location, 0, rgb[0], rgb[1], rgb[2], 10, null);
    }

    public static float[] parseColor(final String color) {
        switch (color.replaceAll(" ", "").toLowerCase()) {
            case ("black"):
            case ("&0"):
                return new float[]{0, 0, 0};
            case ("dark_blue"):
            case ("&1"):
                return null; //new float[]{0, 0, 0.66667F}; -- Doesn't work.
            case ("dark_green"):
            case ("&2"):
                return new float[]{0, 0.66667F, 0};
            case ("dark_aqua"):
            case ("&3"):
                return new float[]{0, 0.66667F, 0.66667F};
            case ("dark_red"):
            case ("&4"):
                return null; //new float[]{0.66667F, 0, 0}; -- Doesn't work.
            case ("dark_purple"):
            case ("&5"):
                return null; //float[]{0.66667F, 0, 0.66667F}; -- Doesn't work.
            case ("gold"):
            case ("&6"):
                return new float[]{1, 0.66667F, 0};
            case ("gray"):
            case ("&7"):
                return new float[]{0.66667F, 0.66667F, 0.66667F};
            case ("dark_gray"):
            case ("&8"):
                return new float[]{0.33333F, 0.33333F, 0.33333F};
            case ("blue"):
            case ("&9"):
                return new float[]{0.33333F, 0.33333F, 1};
            case ("green"):
            case ("&a"):
                return new float[]{0.33333F, 1, 0.33333F};
            case ("aqua"):
            case ("&b"):
                return new float[]{0.33333F, 1, 1};
            case ("red"):
            case ("&c"):
                return new float[]{1, 0.33333F, 0.33333F};
            case ("light_purple"):
            case ("&d"):
                return new float[]{1, 0.33333F, 1};
            case ("yellow"):
            case ("&e"):
                return new float[]{1, 1, 0.33333F};
            case ("white"):
            case ("&f"):
                return new float[]{1, 1, 1};
            default:
                return null; //incorrect colors
        }
    }
}
