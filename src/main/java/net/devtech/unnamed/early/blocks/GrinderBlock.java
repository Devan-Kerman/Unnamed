package net.devtech.unnamed.early.blocks;

import io.github.cottonmc.resources.BuiltinResources;
import net.devtech.fixedfluids.api.Participant;
import net.devtech.fixedfluids.api.util.FluidUtil;
import net.devtech.fixedfluids.api.util.Transaction;
import net.devtech.mcrf.recipes.Recipe;
import net.devtech.mcrf.util.MCRFUtil;
import net.devtech.unnamed.api.EnergyType;
import net.devtech.unnamed.base.blocks.BaseWithEntity;
import net.devtech.unnamed.base.inventory.IOInventory;
import net.devtech.unnamed.early.blocks.gui.GrinderGui;
import net.devtech.unnamed.registry.URecipes;
import net.devtech.unnamed.registry.UTiles;
import net.devtech.unnamed.util.UUtil;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class GrinderBlock extends BaseWithEntity.Directional {
	public GrinderBlock(Settings settings) {
		super(settings);

	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new Entity();
	}

	/**
	 * 1 progress 2 max progress
	 */
	public static class Entity extends BaseWithEntity.Entity implements Tickable {
		private Recipe lastRecipe;

		public Entity() {
			super(UTiles.GRINDER, new IOInventory(i -> {
				for (Recipe recipe : URecipes.GRINDING_RECIPE) {
					if (MCRFUtil.suffices(i, recipe.getInput(0))) {
						return true;
					}
				}
				return false;
			}), 2);
		}

		@Override
		public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			return new GrinderGui(syncId, inv, ScreenHandlerContext.create(this.world, this.pos));
		}

		@Override
		public void tick() {
			if (this.world != null) {
				return;
			}

			ItemStack input = this.inventory.getStack(0);
			if (input.isEmpty()) {
				this.reset();
				return;
			}

			Recipe consume = null;
			for (Recipe recipe : URecipes.GRINDING_RECIPE) {
				if (MCRFUtil.suffices(input, recipe.getInput(0))) {
					consume = recipe;
				}
			}

			// no recipe
			if (consume == null) {
				this.reset();
				return;
			}

			if (!UUtil.canCombine(consume.getOutput(0), this.inventory.getStack(0))) {
				this.reset();
				return;
			}

			// new recipe
			if (consume != this.lastRecipe) {
				if (this.properties.get(0) != 0) {
					this.properties.set(0, 0);
					this.markDirty();
				}
			}

			this.lastRecipe = consume;
			int energy = consume.getInput(1);
			if (this.properties.get(1) != energy) {
				this.properties.set(1, energy);
				this.markDirty();
			}

			energy -= this.properties.get(0);
			// if recipe needs more energy
			if (energy > 0) {
				try (Transaction transaction = new Transaction()) {
					for (Direction direction : UUtil.DIRECTIONS) {
						Participant<?> participant = FluidUtil.get(this.world, this.pos.offset(direction), direction.getOpposite());
						energy -= participant.take(transaction, EnergyType.KINETIC, energy);
						// complete energy requirements
						if (energy <= 0) {
							break;
						}
					}
				}

				// incomplete energy requirements
				if (energy > 0) {
					this.properties.set(0, this.properties.get(1) - energy);
					this.markDirty();
					return;
				}
			}

			ItemStack stack = consume.getOutput(0);
			this.inventory.setStack(1, UUtil.stack(stack, this.inventory.getStack(1)));
			input.setCount(input.getCount() - MCRFUtil.getAmount(consume.getInput(0)));
			this.markDirty();
		}

		private void reset() {
			if (this.properties.get(0) != 0) {
				this.properties.set(0, 0);
				this.markDirty();
			}
		}
	}
}
