package com.benzoft.gravitytubes.utils;

import com.benzoft.gravitytubes.files.MessagesFile;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class MessageUtil {

    private static String translate(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void send(final Player player, String message) {
        message = message.replaceAll("%prefix%", MessagesFile.getInstance().getPrefix());
        if (!message.isEmpty()) {
            message = translate(message);
            if (!message.contains("[\"") && !message.contains("\"]")) {
                if (player != null) {
                    player.sendMessage(message);
                } else Bukkit.getServer().getConsoleSender().sendMessage(message);
            } else {
                final BaseComponent mainComponent = new CustomJSONParser(message).parseMessage();
                if (player != null) {
                    player.spigot().sendMessage(mainComponent);
                } else Bukkit.getServer().getConsoleSender().sendMessage(mainComponent.toLegacyText());
            }
        }
    }

    public static void sendJSON(final Player player, final String message, final String hoverMessage) {
        sendJSON(player, message, hoverMessage, null);
    }

    public static void sendJSON(final Player player, final String message, final String hoverMessage, final String clickEvent) {
        sendJSON(player, message, hoverMessage, clickEvent, ClickEvent.Action.RUN_COMMAND);
    }

    public static void sendJSON(final Player player, final String message, final String hoverMessage, final String clickEvent, final ClickEvent.Action action) {
        if (player != null) {
            final TextComponent tc = new TextComponent(MessageUtil.translate(message));
            if (hoverMessage != null)
                tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.translate(hoverMessage)).create()));
            if (clickEvent != null) tc.setClickEvent(new ClickEvent(action, clickEvent));
            player.spigot().sendMessage(tc);
        } else Bukkit.getServer().getConsoleSender().sendMessage(MessageUtil.translate(message.replaceAll("●", "-")));
    }
}
