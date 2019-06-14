package com.benzoft.gravitytubes.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

final class ActionbarUtil {
    /**
     * Utility message for sending version independent actionbar messages as to be able to
     * support versions from 1.8 and up without having to disable a simple feature such as this.
     *
     * @param player  the recipient of the actionbar message.
     * @param message the message to send. If it is empty ("") the actionbar is cleared.
     */
    static void sendMessage(final Player player, final String message) {
        if (player == null || message == null) return;
        String nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
        nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);

        //1.10 and up
        if (!nmsVersion.startsWith("v1_9_R") && !nmsVersion.startsWith("v1_8_R")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            return;
        }

        //1.8.x and 1.9.x
        try {
            final Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
            final Object craftPlayer = craftPlayerClass.cast(player);

            final Class<?> ppoc = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");
            final Class<?> packet = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
            final Object packetPlayOutChat;
            final Class<?> chat = Class.forName("net.minecraft.server." + nmsVersion + (nmsVersion.equalsIgnoreCase("v1_8_R1") ? ".ChatSerializer" : ".ChatComponentText"));
            final Class<?> chatBaseComponent = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");

            final Object object = nmsVersion.equalsIgnoreCase("v1_8_R1") ? chatBaseComponent.cast(chat.getDeclaredMethod("a", String.class).invoke(chat, "{'text': '" + message + "'}")) : chat.getConstructor(new Class[]{String.class}).newInstance(message);
            packetPlayOutChat = ppoc.getConstructor(new Class[]{chatBaseComponent, Byte.TYPE}).newInstance(object, (byte) 2);

            final Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
            final Object iCraftPlayer = handle.invoke(craftPlayer);
            final Field playerConnectionField = iCraftPlayer.getClass().getDeclaredField("playerConnection");
            final Object playerConnection = playerConnectionField.get(iCraftPlayer);
            final Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", packet);
            sendPacket.invoke(playerConnection, packetPlayOutChat);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
}
