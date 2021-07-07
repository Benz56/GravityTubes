package com.benzoft.gravitytubes.utils;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@RequiredArgsConstructor
final class CustomJSONParser {

    private final BaseComponent mainComponent = new TextComponent("");
    private final String message;
    private int stringProgress = 0;

    //Translate message events. ["Message","/Command"] | ["Message","Hover"] | ["Message","Command","Hover"]
    BaseComponent parseMessage() {
        while (true) {
            final int eventStart = message.indexOf("[\"", stringProgress);
            final int eventEnd = message.indexOf("\"]", stringProgress + 1);

            if (eventStart == -1 || eventEnd == -1) {
                if (!message.substring(stringProgress == 0 ? 0 : stringProgress + 2).isEmpty()) {
                    mainComponent.addExtra(new TextComponent(TextComponent.fromLegacyText(message.substring(stringProgress == 0 ? 0 : stringProgress + 2))));
                }
                break;
            }

            mainComponent.addExtra(new TextComponent(TextComponent.fromLegacyText(message.substring(stringProgress == 0 ? 0 : stringProgress + 2, eventStart))));
            stringProgress = eventEnd;

            final String[] split = message.substring(eventStart + 2, eventEnd).split("\",\"");
            String message = "";
            String command = "";
            String hover = "";
            boolean suggest = false;
            for (int i = 0; i < split.length; i++) {
                if (i != 0) {
                    if (split[i].contains("/") && command.isEmpty()) {
                        command = split[i];
                    } else if (split[i].equalsIgnoreCase("Suggest")) {
                        suggest = true;
                    } else hover = split[i];
                } else message = split[i];
            }
            final TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(message));
            textComponent.setClickEvent(!command.isEmpty() ? new ClickEvent(suggest ? ClickEvent.Action.SUGGEST_COMMAND : ClickEvent.Action.RUN_COMMAND, command) : null);
            textComponent.setHoverEvent(!hover.isEmpty() ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)) : null);
            mainComponent.addExtra(textComponent);
        }

        return mainComponent;
    }
}
