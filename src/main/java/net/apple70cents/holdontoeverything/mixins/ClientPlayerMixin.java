package net.apple70cents.holdontoeverything.mixins;

import net.apple70cents.holdontoeverything.HoldOntoEverything;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin {
    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    protected void cancelDropItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        int selectedSlot = ((ClientPlayerEntity) (Object) this).getInventory().selectedSlot;
        ItemStack stack = ((ClientPlayerEntity) (Object) this).getInventory().getStack(selectedSlot);
        boolean shouldCancel = HoldOntoEverything.shouldCancel(stack);
        if (shouldCancel) {
            cir.setReturnValue(false);
        }
    }
}
