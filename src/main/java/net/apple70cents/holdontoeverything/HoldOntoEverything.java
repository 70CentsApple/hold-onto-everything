package net.apple70cents.holdontoeverything;

import net.apple70cents.holdontoeverything.config.ConfigStorage;
import net.apple70cents.holdontoeverything.utils.LoggerUtils;
import net.apple70cents.holdontoeverything.utils.MessageUtils;
import net.apple70cents.holdontoeverything.utils.TextUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;

/**
 * @author 70CentsApple
 */
public class HoldOntoEverything implements ModInitializer {

    public final static ConfigStorage DEFAULT_CONFIG = new ConfigStorage(true);
    public static ConfigStorage CONFIG;

    @Override
    public void onInitialize() {
        LoggerUtils.init();

        if (!ConfigStorage.configFileExists()) {
            // if the config file doesn't exist, create a new one with the default settings.
            DEFAULT_CONFIG.save();
        }

        CONFIG = new ConfigStorage(false).withDefault(DEFAULT_CONFIG.getHashmap());

        LoggerUtils.info("[HoldOntoEverything] Successfully started with config: " + CONFIG.getHashmap());
    }

    public static boolean shouldCancel(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return true;
        }
        if (!(boolean) HoldOntoEverything.CONFIG.get("config.enabled")) {
            return false;
        }
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if ((!(boolean) HoldOntoEverything.CONFIG.get("config.hotbar"))
                && screen == null) {
            MessageUtils.sendToActionbar(TextUtils.trans("texts.prevent"));
            return true;
        }

        if (screen == null) {
            // Already dealt with hotbars, so we can just return.
            return false;
        }
        // Now, we can assure that screen is not null!
        if ((!(boolean) HoldOntoEverything.CONFIG.get("config.inventory"))
                && screen instanceof AbstractInventoryScreen) {
            MessageUtils.sendToActionbar(TextUtils.trans("texts.prevent"));
            return true;
        }
        if ((!(boolean) HoldOntoEverything.CONFIG.get("config.container"))
                && (screen instanceof GenericContainerScreen || screen instanceof Generic3x3ContainerScreen)) {
            MessageUtils.sendToActionbar(TextUtils.trans("texts.prevent"));
            return true;
        }
        return false;
    }
}
