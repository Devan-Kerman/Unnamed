package net.devtech.unnamed.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;

@SuppressWarnings ("ConstantConditions")
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin_SlowFurnaceMixin extends LockableContainerBlockEntity {
	protected AbstractFurnaceBlockEntityMixin_SlowFurnaceMixin(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void tick(CallbackInfo ci) {
		// haha furnace nerf go brrr
		if(((Object)this).getClass() == FurnaceBlockEntity.class && this.hasWorld() && this.world.random.nextInt(3) == 0) {
			ci.cancel();
		}
	}
}
