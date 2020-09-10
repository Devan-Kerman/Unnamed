package net.devtech.unnamed.base.inventory;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class IOInventory extends SimpleInventory implements SidedInventory {
	private static final int[] TOP = {0};
	private static final int[] BOTTOM = {1};
	private final Predicate<ItemStack> inputFilter;

	public IOInventory(Predicate<ItemStack> filter) {
		super(2);
		this.inputFilter = filter;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return side == Direction.DOWN ? BOTTOM : TOP;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return this.inputFilter.test(stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return slot == 1;
	}
}
