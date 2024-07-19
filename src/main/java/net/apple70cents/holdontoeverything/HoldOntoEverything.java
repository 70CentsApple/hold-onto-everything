package net.apple70cents.holdontoeverything;

import net.apple70cents.holdontoeverything.config.ConfigStorage;
import net.apple70cents.holdontoeverything.mixins.KeyBindingInvoker;
import net.apple70cents.holdontoeverything.utils.LoggerUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
 * @author 70CentsApple
 */
public class HoldOntoEverything implements ModInitializer {

    public final static ConfigStorage DEFAULT_CONFIG = new ConfigStorage(true);
    public static ConfigStorage CONFIG;
    public static InputUtil.Key originalDropKey = InputUtil.UNKNOWN_KEY;
    public static final KeyBinding EMPTY_DROP_KEYBINDING = new KeyBinding("key.drop", InputUtil.UNKNOWN_KEY.getCode(), "key.categories.inventory");

    @Override
    public void onInitialize() {
        LoggerUtils.init();

        if (!ConfigStorage.configFileExists()) {
            // if the config file doesn't exist, create a new one with the default settings.
            DEFAULT_CONFIG.save();
        }

        CONFIG = new ConfigStorage(false).withDefault(DEFAULT_CONFIG.getHashmap());

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!(boolean) HoldOntoEverything.CONFIG.get("config.enabled")) {
                return;
            }
            KeyBinding dropKey = MinecraftClient.getInstance().options.dropKey;
            if (!dropKey.equals(EMPTY_DROP_KEYBINDING)) {
                originalDropKey = InputUtil.fromTranslationKey(dropKey.getBoundKeyTranslationKey());
            }
            Screen screen = MinecraftClient.getInstance().currentScreen;
            if (screen instanceof GameOptionsScreen) {
                enableDrop();
                return;
            }
            if ((!(boolean) HoldOntoEverything.CONFIG.get("config.hotbar")) && screen == null) {
                disableDrop();
                return;
            }
            if ((!(boolean) HoldOntoEverything.CONFIG.get("config.inventory")) && screen instanceof AbstractInventoryScreen) {
                disableDrop();
                return;
            }
            if ((!(boolean) HoldOntoEverything.CONFIG.get("config.container")) && (screen instanceof GenericContainerScreen || screen instanceof Generic3x3ContainerScreen)) {
                disableDrop();
                return;
            }
            enableDrop();
        });
        LoggerUtils.info("[HoldOntoEverything] Successfully started with config: " + CONFIG.getHashmap());
    }

    public static void disableDrop() {
        MinecraftClient.getInstance().options.dropKey.setBoundKey(InputUtil.UNKNOWN_KEY);
        ((KeyBindingInvoker) MinecraftClient.getInstance().options.dropKey).resetKeybinding();
    }

    public static void enableDrop() {
        MinecraftClient.getInstance().options.dropKey.setBoundKey(originalDropKey);
    }
}
