package net.devtech.unnamed.early.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class AshesBlock extends SnowBlock {
	public AshesBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
	}
}
