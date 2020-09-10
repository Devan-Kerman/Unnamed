package net.devtech.unnamed.api;

import java.util.Random;

import net.minecraft.item.ItemStack;

public interface FlywheelItem {

	int getMaxRPM(ItemStack stack);

	/**
	 * @return true if energy should be lost that tick
	 */
	boolean loss(ItemStack stack);
}
