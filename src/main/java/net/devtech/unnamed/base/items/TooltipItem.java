package net.devtech.unnamed.base.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class TooltipItem extends Item {
	private final Text text;
	public TooltipItem(Settings settings, Text text) {
		super(settings);
		this.text = text;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(this.text);
	}
}
