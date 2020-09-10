package net.devtech.unnamed.base.blocks;

import static net.minecraft.block.HorizontalFacingBlock.FACING;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.devtech.unnamed.mixin.access.ArrayPropertyDelegateAccess;
import net.devtech.unnamed.util.EmptyInventory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;

public abstract class BaseWithEntity extends BlockWithEntity implements InventoryProvider {
	protected BaseWithEntity(Settings settings) {
		super(settings);
	}

	public static abstract class Directional extends BaseWithEntity {
		protected Directional(Settings settings) {
			super(settings);
			this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
		}

		@Override
		protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
			builder.add(FACING);
		}

		@Override
		public BlockState rotate(BlockState state, BlockRotation rotation) {
			return state.with(FACING, rotation.rotate(state.get(FACING)));
		}

		@Override
		public BlockState mirror(BlockState state, BlockMirror mirror) {
			return state.rotate(mirror.getRotation(state.get(FACING)));
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		return ActionResult.SUCCESS;
	}

	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof Entity) {
			return ((Entity) entity).inventory;
		}
		return EmptyInventory.INSTANCE;
	}

	public static abstract class EntityNoInv extends BlockEntity implements BlockEntityClientSerializable, NamedScreenHandlerFactory,
			                                                                        PropertyDelegateHolder {
		protected final ArrayPropertyDelegate properties;

		public EntityNoInv(BlockEntityType<?> type, int properties) {
			super(type);
			this.properties = new ArrayPropertyDelegate(properties);
		}

		@Override
		public void fromTag(BlockState state, CompoundTag tag) {
			super.fromTag(state, tag);
			int[] arr = tag.getIntArray("properties");
			for (int i = 0; i < arr.length; i++) {
				this.properties.set(i, arr[i]);
			}
		}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			tag.putIntArray("properties",
			                ((ArrayPropertyDelegateAccess) this.properties).getData()
			                                                               .clone());
			return super.toTag(tag);
		}

		@Override
		public Text getDisplayName() {
			return new TranslatableText(this.getCachedState()
			                                .getBlock()
			                                .getTranslationKey());
		}

		@Override
		public PropertyDelegate getPropertyDelegate() {
			return this.properties;
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

	public static abstract class Entity extends EntityNoInv {
		protected final SidedInventory inventory;

		public Entity(BlockEntityType<?> type, SidedInventory inventory, int properties) {
			super(type, properties);
			this.inventory = inventory;
		}

		@Override
		public void fromTag(BlockState state, CompoundTag tag) {
			super.fromTag(state, tag);
			ListTag tags = tag.getList("inventory", NbtType.COMPOUND);
			for (int i = 0; i < tags.size(); ++i) {
				ItemStack itemStack = ItemStack.fromTag(tags.getCompound(i));
				if (!itemStack.isEmpty()) {
					this.inventory.setStack(i, itemStack);
				}
			}
		}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			ListTag listTag = new ListTag();
			for (int i = 0; i < this.inventory.size(); ++i) {
				ItemStack itemStack = this.inventory.getStack(i);
				if (!itemStack.isEmpty()) {
					listTag.add(itemStack.toTag(new CompoundTag()));
				}
			}
			tag.put("inventory", listTag);
			return super.toTag(tag);
		}

		/**
		 * please use modulo number == 0
		 */
		public final int getTicks() {
			if (this.world != null) {
				MinecraftServer server = this.world.getServer();
				if (server != null) {
					return server.getTicks();
				}
			}
			return 1;
		}

	}

}
