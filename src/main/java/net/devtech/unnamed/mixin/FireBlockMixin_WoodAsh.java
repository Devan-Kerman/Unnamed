package net.devtech.unnamed.mixin;

import net.devtech.unnamed.registry.UBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin (FireBlock.class)
public abstract class FireBlockMixin_WoodAsh extends AbstractFireBlock {
	public FireBlockMixin_WoodAsh(Settings settings, float damage) {
		super(settings, damage);
	}

	// todo replace this with a modify arg
	@Redirect (method = "trySpreadingFire",
			at = @At (value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
	private boolean onBurn(World world, BlockPos pos, boolean move) {
		BlockState state = world.getBlockState(pos);
		if (BlockTags.LOGS_THAT_BURN.contains(state.getBlock()) && world.random.nextInt(3) == 0) {
			double d = world.random.nextDouble();
			return world.setBlockState(pos,
			                           UBlocks.ASH_BLOCK.getDefaultState()
			                                            .with(SnowBlock.LAYERS, Math.min(((int) (d * d * 8)) + 1, 8)));
		} else {
			return world.removeBlock(pos, move);
		}
	}
}