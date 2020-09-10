package net.devtech.unnamed.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class EmptyInventory implements SidedInventory {
	public static final SidedInventory INSTANCE = new EmptyInventory();
	private static final int[] AVAILABLE = {};

	private EmptyInventory() {}
	@Override
	public int[] getAvailableSlots(Direction side) {
		return AVAILABLE;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return false;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {}

	@Override
	public void markDirty() {}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {return false;}

	@Override
	public void clear() {}
}
