package net.apple70cents.holdontoeverything.mixins;

import net.apple70cents.holdontoeverything.HoldOntoEverything;
import net.apple70cents.holdontoeverything.utils.LoggerUtils;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftMixin {
    @Inject(method = "stop", at = @At("HEAD"))
    protected void recoverDropKey(CallbackInfo ci) {
        if (!(boolean) HoldOntoEverything.CONFIG.get("config.enabled")) {
            return;
        }
        LoggerUtils.info("[HoldOntoEverything] Recovered drop key!");
        HoldOntoEverything.enableDrop();
    }
}
