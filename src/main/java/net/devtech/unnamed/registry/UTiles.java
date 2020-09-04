package net.devtech.unnamed.registry;

import static net.devtech.unnamed.Unnamed.MODID;

import java.util.function.Supplier;

import net.devtech.unnamed.early.blocks.SolidFireboxBlock;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface UTiles {
	BlockEntityType<SolidFireboxBlock.Entity> SOLID_FIREBOX = register("solid_firebox", SolidFireboxBlock.Entity::new, UBlocks.SOLID_FIREBOX);
	static <T extends BlockEntity> BlockEntityType<T> register(String id, Supplier<T> t, Block... blocks) {
		BlockEntityType.Builder<T> builder = BlockEntityType.Builder.create(t, blocks);
		BlockEntityType<T> type = builder.build(null);
		Identifier identifier = new Identifier(MODID, id);
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, type);
	}
	static void init() {}
}
