package net.apple70cents.holdontoeverything.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 70CentsApple
 */
public class MessageUtils {
    public static void sendToActionbar(Component text) {
        if (Minecraft.getInstance().player != null) {
            //? if >=26.1 {
            Minecraft.getInstance().player.sendOverlayMessage(text);
            //? } else {
            /*Minecraft.getInstance().player.displayClientMessage(text, true);
            *///? }
        }
    }

    public static void sendToNonPublicChat(Component text) {
        //? if >=26.1 {
        Minecraft.getInstance().gui.getChat().addClientSystemMessage(text);
        //? } else {
        /*Minecraft.getInstance().gui.getChat().addMessage(text);
        *///? }
    }

    public static void sendToPublicChat(String text) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        //? if >=1.19 {
        String text2 = StringUtils.normalizeSpace(text.trim());
        if (!text2.isEmpty()) {
            Minecraft.getInstance().gui.getChat().addRecentChat(text);
            if (text2.startsWith("/")) {
                player.connection.sendCommand(text2.substring(1));
            } else {
                player.connection.sendChat(text2);
            }
        }
        //? } else {
        /*player.chat(text);
        *///? }
    }
}
