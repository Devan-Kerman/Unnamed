package net.devtech.unnamed.base.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class DryingBlock extends Block {
	private final int slowness;
	private final BlockState state;
	public DryingBlock(Settings settings, BlockState state, int slowness) {
		super(settings.ticksRandomly());
		this.state = state;
		this.slowness = slowness;
	}

	public DryingBlock(Settings settings, BlockState state) {
		this(settings, state, 2);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if(random.nextInt(this.slowness) == 0 && world.getLightLevel(LightType.SKY, pos) > 10) {
			world.setBlockState(pos, this.state);
		}
	}
}
