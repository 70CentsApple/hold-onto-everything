package net.apple70cents.holdontoeverything.mixins;

import net.apple70cents.holdontoeverything.HoldOntoEverything;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Inject(method = "internalOnSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;takeStackRange(IILnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    protected void cancelDropItem(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        ItemStack stack = player.getInventory().getStack(slotIndex);
        boolean shouldCancel = HoldOntoEverything.shouldCancel(stack);
        if (shouldCancel) {
            ci.cancel();
        }
    }
}
