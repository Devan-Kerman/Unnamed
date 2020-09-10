package net.devtech.unnamed.early.blocks;

import java.util.Set;

import net.devtech.fixedfluids.api.BlockParticipant;
import net.devtech.fixedfluids.api.Participant;
import net.devtech.fixedfluids.api.util.Transaction;
import net.devtech.unnamed.api.EnergyType;
import net.devtech.unnamed.base.blocks.BaseWithEntity;
import net.devtech.unnamed.early.blocks.gui.SolidFireboxBlockGui;
import net.devtech.unnamed.registry.UTiles;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.registry.FuelRegistry;

// todo comparator output
// todo cooling down
public class SolidFireboxBlock extends BlockWithEntity implements BlockParticipant<Integer> {
	public SolidFireboxBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH).with(Properties.LIT, false));
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new Entity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		return ActionResult.SUCCESS;
	}

	@Override
	public Participant<Integer> get(BlockState state, World world, BlockPos pos, @Nullable Direction direction) {
		return (Participant<Integer>)world.getBlockEntity(pos);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		Entity entity = (Entity) world.getBlockEntity(pos);
		if(entity == null) return 0;
		PropertyDelegate delegate = entity.getPropertyDelegate();
		float ratio = delegate.get(2) / (float)delegate.get(3);
		return (int) (ratio * 15);
	}

	/**
	 * 0 = total burn time of current fuel
	 * 1 = remaining burn time left of current fuel
	 * 2 = heat energy
	 * 3 = max heat energy
	 */
	public static class Entity extends BaseWithEntity.Entity implements Tickable, Participant.State<Integer> {
		public Entity() {
			super(UTiles.SOLID_FIREBOX, new FuelInventory(), 4);
			this.properties.set(3, 2000);
		}

		@Override
		public void tick() {
			if(this.world != null && this.world.isClient) return;
			int i = this.properties.get(1);
			if(i <= 0) { // check inventory
				ItemStack stack = this.inventory.getStack(0);
				if(!stack.isEmpty()) {
					Integer time = FuelRegistry.INSTANCE.get(stack.getItem());
					if(time != null) {
						stack.decrement(1);
						this.properties.set(0, time);
						this.properties.set(1, time);
						this.world.setBlockState(this.getPos(),
						                         this.getCachedState().with(Properties.LIT, true));
						this.markDirty();
					}
				}

				// slowly cool down
				this.properties.set(2, Math.max(0, this.properties.get(2) - 1));
			} else {
				this.properties.set(1, --i);
				this.properties.set(2, Math.min(2000, this.properties.get(2) + 1));
				if(i == 0) {
					this.world.setBlockState(this.getPos(),
					                         this.getCachedState().with(Properties.LIT, false));
				}
				this.markDirty();
			}
		}


		@Override
		public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			return new SolidFireboxBlockGui(syncId, inv, ScreenHandlerContext.create(this.world, this.pos));
		}

		@Override
		public long take(Transaction transaction, Object o, long l) {
			if(o == EnergyType.HEAT) {
				int heat = transaction.computeIfAbsent(this, () -> this.properties.get(2));
				int toTake = (int) Math.min(heat, l);
				transaction.mutate(this, i -> i - toTake);
				return toTake;
			}
			return 0;
		}

		@Override
		public long add(Transaction transaction, Object o, long l) {
			return l;
		}

		@Override
		public Set<Class<?>> getSupportedTypes() {
			return EnergyType.HEAT_;
		}

		@Override
		public void onCommit(Integer integer) {
			this.properties.set(2, integer);
			this.markDirty();
		}
	}

	static class FuelInventory extends SimpleInventory implements SidedInventory {
		private static final int[] SLOTS = {0};
		public FuelInventory() {
			super(1);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
		}

		@Override
		public int[] getAvailableSlots(Direction side) {
			return SLOTS;
		}

		@Override
		public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
			return this.canInsert(stack);
		}

		@Override
		public boolean canExtract(int slot, ItemStack stack, Direction dir) {
			return false;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING, Properties.LIT);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
