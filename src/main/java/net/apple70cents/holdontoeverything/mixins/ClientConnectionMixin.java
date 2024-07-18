package net.apple70cents.holdontoeverything.mixins;

import net.apple70cents.holdontoeverything.HoldOntoEverything;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    protected void cancelDropItem(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof PlayerActionC2SPacket c2SPacket) {
            if (PlayerActionC2SPacket.Action.DROP_ITEM.equals(c2SPacket.getAction()) ||
                PlayerActionC2SPacket.Action.DROP_ALL_ITEMS.equals(c2SPacket.getAction())) {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null) {
                    int selectedSlot = player.getInventory().selectedSlot;
                    ItemStack stack = player.getInventory().getStack(selectedSlot);
                    boolean shouldCancel = HoldOntoEverything.shouldCancel(stack);
                    if (shouldCancel) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}
