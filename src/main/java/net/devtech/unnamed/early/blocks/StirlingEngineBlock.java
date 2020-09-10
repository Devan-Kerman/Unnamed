package net.devtech.unnamed.early.blocks;

import static net.devtech.unnamed.api.EnergyType.KINETIC_;

import java.util.Set;

import net.devtech.fixedfluids.api.BlockParticipant;
import net.devtech.fixedfluids.api.Participant;
import net.devtech.fixedfluids.api.util.FluidUtil;
import net.devtech.fixedfluids.api.util.Transaction;
import net.devtech.unnamed.api.EnergyType;
import net.devtech.unnamed.api.FlywheelItem;
import net.devtech.unnamed.base.blocks.BaseWithEntity;
import net.devtech.unnamed.base.blocks.HorizontalFacingBlock;
import net.devtech.unnamed.early.blocks.gui.StirlingEngineGui;
import net.devtech.unnamed.registry.UTiles;
import net.devtech.unnamed.util.UUtil;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

// todo loose energy instantly
public class StirlingEngineBlock extends BaseWithEntity implements BlockParticipant<Integer> {
	public StirlingEngineBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
		                                      .with(HorizontalFacingBlock.FACING, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new Entity();
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		Entity entity = (Entity) world.getBlockEntity(pos);
		if (entity == null) {
			return 0;
		}
		PropertyDelegate delegate = entity.getPropertyDelegate();
		float ratio = delegate.get(4) / (float) delegate.get(5);
		return (int) (ratio * 15);
	}

	@Override
	public Participant<Integer> get(BlockState state, World world, BlockPos pos, @Nullable Direction direction) {
		if (direction == null || direction.getOffsetY() != 0) {
			return (Participant<Integer>) world.getBlockEntity(pos);
		}
		return null;
	}

	/**
	 * 0 temperature     1 max temperature 2 kinetic energy  3 max kinetic energy
	 */
	public static class Entity extends BaseWithEntity.Entity implements Tickable, Participant.State<Integer> {
		private static final int MAX_TEMP = 2000;

		public Entity() {
			super(UTiles.STIRLING_ENGINE, new FlywheelInventory(), 4);
			this.properties.set(1, MAX_TEMP);
		}

		@Override
		public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			return new StirlingEngineGui(syncId, inv, ScreenHandlerContext.create(this.world, this.pos));
		}

		@Override
		public void tick() {
			boolean dirty = false;
			int heatEnergy = this.properties.get(0);
			int kineticEnergy = this.properties.get(2);
			ItemStack stack = this.inventory.getStack(0);
			if (!stack.isEmpty() && stack.getItem() instanceof FlywheelItem) {
				FlywheelItem flywheel = (FlywheelItem) stack.getItem();
				int max = flywheel.getMaxRPM(stack);
				if (this.properties.get(3) != max) {
					this.properties.set(3, max);
					dirty = true;
				}

				// kinetic energy loss
				if (kineticEnergy > 0 && flywheel.loss(stack)) {
					kineticEnergy--;
				}

				int toTake = Math.min(MAX_TEMP - heatEnergy, 10);
				if (toTake > 0 && this.world != null) {
					Transaction transaction = new Transaction();
					Participant<?> participant = FluidUtil.get(this.world, this.pos.down(), Direction.UP);
					if (participant != null) {
						long taken = participant.take(transaction, EnergyType.HEAT, toTake);
						transaction.commit();
						if (taken > 0) {
							heatEnergy += taken;
						}
					}
				}

				// min
				if (this.getTicks() % 5 == 0) {
					float consumption = UUtil.sigmoid(heatEnergy / 500f) * 4;
					if (consumption >= 1) {
						heatEnergy -= consumption;
						kineticEnergy += consumption * .66f;
					}
				}
			} else if (this.properties.get(2) != 0) {
				kineticEnergy = 0;
			}

			if (heatEnergy > 0 && this.getTicks() % 5 == 0) {
				heatEnergy--;
			}

			if (heatEnergy != this.properties.get(0) || kineticEnergy != this.properties.get(2)) {
				dirty = true;
				this.properties.set(0, heatEnergy);
				this.properties.set(2, kineticEnergy);
			}

			if(dirty) {
				this.markDirty();
			}
		}

		@Override
		public long take(Transaction transaction, Object o, long l) {
			if (o == EnergyType.KINETIC) {
				l = Math.min(l, Integer.MAX_VALUE);
				int amount = transaction.computeIfAbsent(this, () -> this.properties.get(2));
				l = Math.min(l, amount);
				long finalL = l;
				transaction.mutate(this, i -> Math.toIntExact(i - finalL));
				return l;
			}
			return 0;
		}

		@Override
		public long add(Transaction transaction, Object o, long l) {
			return l;
		}

		@Override
		public Set<Class<?>> getSupportedTypes() {
			return KINETIC_;
		}

		@Override
		public void onCommit(Integer integer) {
			this.properties.set(2, integer);
		}
	}

	static class FlywheelInventory extends SimpleInventory implements SidedInventory {
		private static final int[] SLOTS = {0};

		public FlywheelInventory() {
			super(1);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return stack.getItem() instanceof FlywheelItem;
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
}
