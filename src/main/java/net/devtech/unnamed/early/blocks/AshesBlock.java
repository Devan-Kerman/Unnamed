package net.devtech.unnamed.early.blocks;

import java.util.Random;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.unnamed.util.resource.ResourceGenerateable;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AshesBlock extends SnowBlock implements ResourceGenerateable.Block {
	public static final Provided PROVIDED = new Provided();
	public static class Provided implements ResourceGenerateable.Block {
		@Override
		public void generateBlockState(RuntimeResourcePack pack, Identifier id) {}
		@Override
		public void generateBlockModel(RuntimeResourcePack pack, Identifier id) {}
		@Override
		public void generateLootTable(RuntimeResourcePack pack, Identifier id) {}
	}

	public AshesBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {}
}
