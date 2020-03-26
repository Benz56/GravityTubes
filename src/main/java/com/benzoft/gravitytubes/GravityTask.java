package com.benzoft.gravitytubes;

import com.benzoft.gravitytubes.files.ConfigFile;
import com.benzoft.gravitytubes.files.GravityTubesFile;
import com.benzoft.gravitytubes.files.MessagesFile;
import com.benzoft.gravitytubes.runtimedata.PlayerDataManager;
import com.benzoft.gravitytubes.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public class GravityTask implements Runnable {

    private final GravityTubes gravityTubes;

    @Override
    public void run() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (player.getGameMode() == GameMode.SPECTATOR) return;
            final GravityTube tube = GravityTubesFile.getInstance().getTubes().stream().filter(gravityTube -> gravityTube.isInTube(player)).findFirst().orElse(null);
            if (tube != null) {
                PlayerDataManager.getPlayerData(player, true).ifPresent(playerData -> {
                    final boolean hasPermission = GTPerm.USE.checkPermission(player);
                    final boolean playerInCombat = ConfigFile.getInstance().isDisableInCombat() && gravityTubes.getCombatLogXPlugin().isPresent() && gravityTubes.getCombatLogXPlugin().get().getCombatManager().isInCombat(player);
                    if (playerData.getGravityTube() == null) { //Enter gravity tube.
                        if (hasPermission) {
                            if (!playerInCombat) {
                                gravityTubes.getAntiCheatHook().ifPresent(antiCheatHook -> antiCheatHook.setExempt(player, true));
                                playerData.setFlying(player.isFlying());
                                player.setFlying(false);
                            } else MessageUtil.send(player, MessagesFile.getInstance().getInCombat());
                        } else MessageUtil.send(player, MessagesFile.getInstance().getInvalidPermission());
                    }
                    playerData.setGravityTube(tube);
                    playerData.getGravityBar().update();
                    if (hasPermission && !playerInCombat) {
                        if (player.isSneaking() && ConfigFile.getInstance().isSneakToFall()) { //TODO slow falling effect or Levitation inverted at 128+.
                            player.removePotionEffect(PotionEffectType.LEVITATION);
                            if (ConfigFile.getInstance().isDisableFallDamage()) player.setFallDistance(0);
                        } else gravitate(player, tube);
                    }
                });
            } else PlayerDataManager.getPlayerData(player).ifPresent(playerData -> {
                if (playerData.getGravityTube() != null) {
                    playerData.setGravityTube(null);
                    playerData.getGravityBar().remove();
                    player.removePotionEffect(PotionEffectType.LEVITATION);
                    player.setFallDistance(0);
                    if (playerData.isFlying()) player.setFlying(playerData.isFlying());
                }
            });
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