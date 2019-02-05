package com.benzoft.gravitytubes.runtimedata;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class PlayerDataManager {

    private static final Map<UUID, PlayerData> playerData = new HashMap<>();

    public static Optional<PlayerData> getPlayerData(final Player player) {
        return getPlayerData(player, false);
    }

    public static Optional<PlayerData> getPlayerData(final Player player, final boolean createIfNotPresent) {
        return createIfNotPresent ? Optional.of(playerData.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(player.getUniqueId()))) : Optional.ofNullable(playerData.get(player.getUniqueId()));
    }

    public static void removePlayerData(final Player player) {
        playerData.remove(player.getUniqueId());
    }
}
