package net.devtech.unnamed.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class UUtil {
	public static final List<Direction> DIRECTIONS = Collections.unmodifiableList(Arrays.asList(Direction.values()));
	public static float sigmoid(float x) {
		return x / (1 + Math.abs(x));
	}

	public static boolean canCombine(ItemStack src, ItemStack dest) {
		return dest.isEmpty() || src.getItem() == dest.getItem() && ItemStack.areTagsEqual(src, dest) && src.getCount() + dest.getCount() <= dest.getMaxCount();
	}

	/**
	 * assumes the items are stackable
	 */
	public static ItemStack stack(ItemStack src, ItemStack dest) {
		if(dest.isEmpty())
			return src.copy();
		dest.setCount(src.getCount() + dest.getCount());
		return dest;
	}
}
