package net.apple70cents.holdontoeverything.mixins;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyMapping.class)
public interface KeyBindingInvoker {
    @Invoker("release")
    public void resetKeybinding();
}
