package net.apple70cents.holdontoeverything.mixins;

import net.apple70cents.holdontoeverything.HoldOntoEverything;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    protected void cancelDropItem(boolean entireStack, CallbackInfoReturnable<ItemStack> cir) {
        int selectedSlot = ((PlayerInventory) (Object) this).selectedSlot;
        ItemStack stack = ((PlayerInventory) (Object) this).getStack(selectedSlot);
        boolean shouldCancel = HoldOntoEverything.shouldCancel(stack);
        if (shouldCancel) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
