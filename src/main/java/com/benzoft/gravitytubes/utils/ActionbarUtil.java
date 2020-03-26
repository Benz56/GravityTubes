package com.benzoft.gravitytubes.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

final class ActionbarUtil {

    private static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);

    private static Class<?> craftPlayerClass;
    private static Constructor<?> ppoc;
    private static Class<?> packet;
    private static Class<?> chat;
    private static Class<?> chatBaseComponent;

    static {
        try {
            if (NMS_VERSION.startsWith("v1_8_R") || NMS_VERSION.startsWith("v1_9_R")) {
                craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + ".entity.CraftPlayer");
                chatBaseComponent = Class.forName("net.minecraft.server." + NMS_VERSION + ".IChatBaseComponent");
                ppoc = Class.forName("net.minecraft.server." + NMS_VERSION + ".PacketPlayOutChat").getConstructor(chatBaseComponent, Byte.TYPE);
                packet = Class.forName("net.minecraft.server." + NMS_VERSION + ".Packet");
                chat = Class.forName("net.minecraft.server." + NMS_VERSION + (NMS_VERSION.equalsIgnoreCase("v1_8_R1") ? ".ChatSerializer" : ".ChatComponentText"));
            }
        } catch (final ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    static void sendMessage(final Player player, final String message) {
        if (NMS_VERSION.startsWith("v1_9_R") || NMS_VERSION.startsWith("v1_8_R")) {
            try {
                final Object craftPlayer = craftPlayerClass.cast(player);

                final Object object = NMS_VERSION.equalsIgnoreCase("v1_8_R1") ? chatBaseComponent.cast(chat.getDeclaredMethod("a", String.class).invoke(chat, "{'text': '" + message + "'}")) : chat.getConstructor(new Class[]{String.class}).newInstance(message);
                final Object packetPlayOutChat = ppoc.newInstance(object, (byte) 2);

                final Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
                final Object iCraftPlayer = handle.invoke(craftPlayer);
                final Field playerConnectionField = iCraftPlayer.getClass().getDeclaredField("playerConnection");
                final Object playerConnection = playerConnectionField.get(iCraftPlayer);
                final Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", packet);
                sendPacket.invoke(playerConnection, packetPlayOutChat);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        } else player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}