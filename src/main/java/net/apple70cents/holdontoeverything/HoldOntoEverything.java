package net.apple70cents.holdontoeverything;

import net.apple70cents.holdontoeverything.config.ConfigStorage;
import net.apple70cents.holdontoeverything.mixins.KeyBindingInvoker;
import net.apple70cents.holdontoeverything.utils.LoggerUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
//? if >=1.21 {
import net.minecraft.client.gui.screens.options.OptionsScreen;
//? } else {
/*import net.minecraft.client.gui.screens.OptionsScreen;
*///? }
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;

//? if >=1.21.2 {
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
//? } else {
/*import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
*///? }

/**
 * @author 70CentsApple
 */
public class HoldOntoEverything implements ModInitializer {

    public final static ConfigStorage DEFAULT_CONFIG = new ConfigStorage(true);
    public static ConfigStorage CONFIG;
    public static InputConstants.Key originalDropKey = InputConstants.UNKNOWN;
    //? if >=1.21.9 {
    public static final KeyMapping EMPTY_DROP_KEYBINDING = new KeyMapping("key.drop", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.INVENTORY);
    //? } else {
    /*public static final KeyMapping EMPTY_DROP_KEYBINDING = new KeyMapping("key.drop", InputConstants.UNKNOWN.getValue(), "key.categories.inventory");
    *///? }
    private static boolean initialized = false;

    @Override
    public void onInitialize() {
        LoggerUtils.init();

        if (!ConfigStorage.configFileExists()) {
            // if the config file doesn't exist, create a new one with the default settings.
            DEFAULT_CONFIG.save();
        }

        CONFIG = new ConfigStorage(false).withDefault(DEFAULT_CONFIG.getHashmap());

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!HoldOntoEverything.CONFIG.getAsBool("config.enabled")) {
                return;
            }
            KeyMapping dropKey = Minecraft.getInstance().options.keyDrop;
            if (!dropKey.same(EMPTY_DROP_KEYBINDING)) {
                originalDropKey = InputConstants.getKey(dropKey.saveString());
            }

            if (!initialized) {
                // we put it here because these should be done AFTER the game initialized bound keys
                // also we need to update `originalDropKey`
                if (!HoldOntoEverything.CONFIG.getAsBool("status.successfullySaved")) {
                    // key not recovered on last window close
                    String lastDropKey = (String) HoldOntoEverything.CONFIG.get("status.lastDropKey");
                    if (Minecraft.getInstance().options != null)
                        Minecraft.getInstance().options.keyDrop.setKey(InputConstants.getKey(lastDropKey));
                    originalDropKey = InputConstants.getKey(lastDropKey);
                    enableDrop();
                    LoggerUtils.warn("[HoldOntoEverything] Did NOT recover the drop key on last window close, now we set it to " + lastDropKey);
                }
                HoldOntoEverything.CONFIG.set("status.successfullySaved", false);
                HoldOntoEverything.CONFIG.save();
                initialized = true;
            }

//? if >=26.2 {
            Screen screen = Minecraft.getInstance().gui.screen();
//? } else {
            /*Screen screen = Minecraft.getInstance().screen;
*///? }
            if (screen instanceof OptionsScreen) {
                enableDrop();
                return;
            }
            if ((!HoldOntoEverything.CONFIG.getAsBool("config.hotbar")) && screen == null) {
                disableDrop();
                return;
            }
            if ((!HoldOntoEverything.CONFIG.getAsBool("config.inventory")) && screen instanceof
                    //? if >=1.21.2 {
                    InventoryScreen
                    //? } else {
                    /*EffectRenderingInventoryScreen
                    *///? }
            ) {
                disableDrop();
                return;
            }
            if ((!HoldOntoEverything.CONFIG.getAsBool("config.container")) && (screen instanceof ContainerScreen || screen instanceof DispenserScreen)) {
                disableDrop();
                return;
            }
            enableDrop();
        });
        LoggerUtils.info("[HoldOntoEverything] Successfully started with config: " + CONFIG.getHashmap());
    }

    public static void disableDrop() {
        if (Minecraft.getInstance().options == null) return;
        Minecraft.getInstance().options.keyDrop.setKey(InputConstants.UNKNOWN);
        ((KeyBindingInvoker) Minecraft.getInstance().options.keyDrop).resetKeybinding();
    }

    public static void enableDrop() {
        if (Minecraft.getInstance().options == null) return;
        Minecraft.getInstance().options.keyDrop.setKey(originalDropKey);
    }
}
