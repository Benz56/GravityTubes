package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GravityTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            final GravityTube tube = GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> gravityTube.isInTube(player)).findFirst().orElse(null);
            if (tube != null) {
                PlayerDataManager.getPlayerData(player, true).ifPresent(playerData -> {
                    playerData.setGravityTube(tube);
                    if (player.isSneaking() && ConfigFile.getInstance().isSneakToFall()) { //TODO slow falling effect or Levitation inverted at 128+.
                        player.removePotionEffect(PotionEffectType.LEVITATION);
                        if (ConfigFile.getInstance().isDisableFallDamage()) player.setFallDistance(0);
                    } else gravitate(player, tube);
                });
            } else {
                PlayerDataManager.getPlayerData(player).ifPresent(playerData -> {
                    if (playerData.getGravityTube() != null) {
                        playerData.setGravityTube(null);
                        player.removePotionEffect(PotionEffectType.LEVITATION);
                        player.setFallDistance(0);
                    }
                });
            }
        });
    }

    private void gravitate(final Player player, final GravityTube tube) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, Integer.MAX_VALUE, tube.getPower(), false, false)); //TODO Slow down at top (setting?).
    }


    /**
     * Recursive method adding all entities. E.g. a player riding a pig, riding a boat would return a list containing those three.
     *
     * @param entity The entity to check the vehicle for.
     * @return A list of the vehicle "hierarchy" as mentioned above.
     */
    /*private List<Entity> getVehicles(final Entity entity) {
        final List<Entity> temp = new ArrayList<>(Collections.singletonList(entity));
        if (entity.getVehicle() != null) temp.addAll(getVehicles(entity.getVehicle()));
        return temp;
    }*/
}