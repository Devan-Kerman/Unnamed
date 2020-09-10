package net.devtech.unnamed.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.screen.ArrayPropertyDelegate;

@Mixin (ArrayPropertyDelegate.class)
public interface ArrayPropertyDelegateAccess {
	@Accessor
	int[] getData();
}
