package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.utils.LocationUtil;
import com.benzoft.gravitytubes.utils.ParticleUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.concurrent.ThreadLocalRandom;

public class GravityTube {

    @Getter
    private final Location sourceLocation;
    private final ConfigurationSection configurationSection;
    @Getter
    private int height;
    @Getter
    private int power;
    private ParticleUtil.GTParticleColor color;


    public GravityTube(final Location sourceLocation, final ConfigurationSection configurationSection) {
        this.sourceLocation = sourceLocation;
        this.configurationSection = configurationSection;
        height = configurationSection.getInt("height", 256 - sourceLocation.getBlockY());
        power = configurationSection.getInt("power", 10);
        color = ParticleUtil.getFromColor(configurationSection.getString("color", "white"));
        color = color == null ? ParticleUtil.GTParticleColor.WHITE : color;
    }

    boolean isInTube(final Entity entity) {
        return entity.getWorld().equals(sourceLocation.getWorld()) &&
                entity.getLocation().getBlockX() == sourceLocation.getBlockX() &&
                entity.getLocation().getBlockZ() == sourceLocation.getBlockZ() &&
                entity.getLocation().getY() >= sourceLocation.getBlockY() && entity.getLocation().getY() <= sourceLocation.getBlockY() + height;
    }

    void spawnParticles() {
        for (int y = sourceLocation.getBlockY(); y < sourceLocation.getBlockY() + height; y += 2) {
            final double xOffset, zOffset;
            xOffset = ThreadLocalRandom.current().nextDouble(1);
            zOffset = ThreadLocalRandom.current().nextDouble(1);
            if (ThreadLocalRandom.current().nextDouble(1) >= 0.2) //TODO Particle density setting.
                ParticleUtil.spawnParticle(new Location(sourceLocation.getWorld(), sourceLocation.getBlockX() + xOffset, y, sourceLocation.getBlockZ() + zOffset), color);
        }
    }

    boolean hasSourceBlock() {
        return !sourceLocation.getBlock().isEmpty();
    }

    public void setPower(final int power) {
        this.power = power;
        configurationSection.set("power", power);
    }

    public void setHeight(final int height) {
        this.height = height;
        configurationSection.set("height", height);
    }

    public void setColor(final String color) {
        this.color = ParticleUtil.getFromColor(color);
        configurationSection.set("color", color);
    }

    public String getInfo() {
        return String.join("&r\n",
                "&9&m&l---&e Gravity Tube Info &9&m&l--------",
                " &7- &eLocation: &a" + LocationUtil.locationToString(sourceLocation),
                " &7- &eHeight: &a" + height,
                " &7- &ePower: &a" + power,
                " &7- &eColor: &a" + color.getName()
        );
    }
}
