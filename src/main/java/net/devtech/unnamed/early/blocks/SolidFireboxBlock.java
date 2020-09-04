package net.devtech.unnamed.early.blocks;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.devtech.unnamed.early.blocks.gui.SolidFireboxBlockGui;
import net.devtech.unnamed.registry.UTiles;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.util.NbtType;

public class SolidFireboxBlock extends BlockWithEntity {
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
		// You need a Block.createScreenHandlerFactory implementation that delegates to the block entity,
		// such as the one from BlockWithEntity
		player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		return ActionResult.SUCCESS;
	}

	public static class Entity extends BlockEntity implements InventoryProvider, PropertyDelegateHolder, NamedScreenHandlerFactory, Tickable, BlockEntityClientSerializable {
		private final FuelInventory inventory = new FuelInventory();
		/**
		 * 0 = total burn time of current fuel
		 * 1 = remaining burn time left of current fuel
		 */
		private final ArrayPropertyDelegate burnTime = new ArrayPropertyDelegate(2);

		public Entity() {
			super(UTiles.SOLID_FIREBOX);
		}

		@Override
		public void tick() {
			if(this.world != null && this.world.isClient) return;

			int i = this.burnTime.get(1);
			if(i <= 0) { // check inventory
				ItemStack stack = this.inventory.getStack(0);
				if(!stack.isEmpty()) {
					Integer time = FuelRegistry.INSTANCE.get(stack.getItem());
					if(time != null) {
						stack.decrement(1);
						this.burnTime.set(0, time);
						this.burnTime.set(1, time);
						this.world.setBlockState(this.getPos(),
						                         this.getCachedState().with(Properties.LIT, true));
						this.markDirty();
					}
				}
			} else {
				this.burnTime.set(1, i--);
				if(i == 0) {
					this.world.setBlockState(this.getPos(),
					                         this.getCachedState().with(Properties.LIT, false));
				}
				this.markDirty();
			}
		}

		@Override
		public void fromTag(BlockState state, CompoundTag tag) {
			super.fromTag(state, tag);
			this.inventory.readTags(tag.getList("inventory", NbtType.COMPOUND));
			this.burnTime.set(0, tag.getInt("burnTime"));
			this.burnTime.set(1, tag.getInt("timeLeft"));
		}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			tag.put("inventory", this.inventory.getTags());
			tag.putInt("burnTime", this.burnTime.get(0));
			tag.putInt("timeLeft", this.burnTime.get(1));
			return super.toTag(tag);
		}

		@Override
		public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
			return this.inventory;
		}

		@Override
		public PropertyDelegate getPropertyDelegate() {
			return this.burnTime;
		}

		@Override
		public Text getDisplayName() {
			return new TranslatableText(this.getCachedState().getBlock().getTranslationKey());
		}

		@Override
		public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			return new SolidFireboxBlockGui(syncId, inv, ScreenHandlerContext.create(this.world, this.pos));
		}

		@Override
		public void fromClientTag(CompoundTag tag) {
			this.fromTag(null, tag);
		}

		@Override
		public CompoundTag toClientTag(CompoundTag tag) {
			return this.toTag(tag);
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
