package net.devtech.unnamed.early.items;

import java.util.List;

import net.devtech.unnamed.api.FlywheelItem;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class WoodenFlywheel extends Item implements FlywheelItem {
	public WoodenFlywheel(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("tooltip.unnamed.wooden_flywheel").formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
	}

	@Override
	public int getMaxRPM(ItemStack stack) {
		return 1000;
	}

	@Override
	public boolean loss(ItemStack stack) {
		return RANDOM.nextFloat() < .33f;
	}
}
