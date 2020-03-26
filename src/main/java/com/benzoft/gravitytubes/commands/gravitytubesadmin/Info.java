package com.benzoft.gravitytubes.commands.gravitytubesadmin;

import com.benzoft.gravitytubes.GTPerm;
import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.commands.AbstractSubCommand;
import com.benzoft.gravitytubes.utils.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Info extends AbstractSubCommand {

    private final GravityTubes gravityTubes;

    Info(final GravityTubes gravityTubes, final String name, final GTPerm permission, final boolean playerOnly, final String... aliases) {
        super(name, permission, playerOnly, aliases);
        this.gravityTubes = gravityTubes;
    }

    @Override
    public void onCommand(final Player player, final String[] args) {
        MessageUtil.send(player, "&9&m&l----------------------------------");
        MessageUtil.sendJSON(player, " &7&l● &eAuthor: &aBenz56", "&eClick to see all plugins by Benz56!", "https://www.spigotmc.org/resources/authors/benz56.171209/", ClickEvent.Action.OPEN_URL);
        MessageUtil.send(player, " &7&l● &eServer version: &a" + Bukkit.getVersion());
        MessageUtil.send(player, " &7&l● &ePlugin version: &a" + gravityTubes.getUpdateChecker().getLocalPluginVersion());
        MessageUtil.send(player, " &7&l● &eLatest version: &a" + (gravityTubes.getUpdateChecker().getSpigotPluginVersion() == null ? "&cunknown" : gravityTubes.getUpdateChecker().getSpigotPluginVersion()));
        if (player != null) {
            if (gravityTubes.getUpdateChecker().getSpigotPluginVersion() != null && !gravityTubes.getUpdateChecker().getLocalPluginVersion().equals(gravityTubes.getUpdateChecker().getSpigotPluginVersion())) {
                MessageUtil.sendJSON(player, " &7&l● &eClick here to Update", "&eOpens the plugin page on Spigot!", "https://www.spigotmc.org/resources/64624/updates", ClickEvent.Action.OPEN_URL);
            }
            MessageUtil.sendJSON(player, " &7&l● &eClick for Support", "&eClick to join the Benzoft Discord server!", "https://discordapp.com/invite/8YVeFHe", ClickEvent.Action.OPEN_URL);
        }
        MessageUtil.sendJSON(player, "&9&m&l----------------------------------", null, null, null);
    }
}
