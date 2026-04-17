package net.apple70cents.holdontoeverything.mixins;

import net.apple70cents.holdontoeverything.HoldOntoEverything;
import net.apple70cents.holdontoeverything.utils.LoggerUtils;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "close", at = @At("HEAD"))
    protected void recoverDropKey(CallbackInfo ci) {
        if (!(boolean) HoldOntoEverything.CONFIG.get("config.enabled")) {
            return;
        }
        LoggerUtils.info("[HoldOntoEverything] Recovered drop key!");
        HoldOntoEverything.enableDrop();
        HoldOntoEverything.CONFIG.set("status.successfullySaved", true);
        HoldOntoEverything.CONFIG.set("status.lastDropKey", HoldOntoEverything.originalDropKey.getName());
        HoldOntoEverything.CONFIG.save();
    }
}
