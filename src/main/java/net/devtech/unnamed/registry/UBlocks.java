package net.devtech.unnamed.registry;

import static net.devtech.arrp.json.models.JModel.model;
import static net.devtech.arrp.json.models.JModel.textures;
import static net.devtech.unnamed.Unnamed.MODID;

import java.util.function.BiConsumer;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.lang.JLang;
import net.devtech.unnamed.ResourceGen;
import net.devtech.unnamed.early.blocks.AshesBlock;
import net.devtech.unnamed.util.func.TriConsumer;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface UBlocks {
	Block ASH_BLOCK = register(new AshesBlock(AbstractBlock.Settings.of(Material.SNOW_LAYER)
	                                                                .strength(0.1F)
	                                                                .sounds(BlockSoundGroup.SAND)), "ash_block", (o, r, i) -> {}, (r, i) -> {

	}, (i, l) -> l.block(i, "Ash Block"));

	static <T extends Block> T register(T t, String id, String en_us) {
		return register(t, id, (o, r, i) -> {
			// todo full block model & blockstate
			// todo item model
		}, (r, i) -> {
			// loot table
		}, (i, l) -> l.block(i, en_us));
	}

	static <T extends Block> T register(T t, String id, TriConsumer<T, RuntimeResourcePack, Identifier> client, BiConsumer<RuntimeResourcePack,
			                                                                                                                      Identifier> server,
	                                    BiConsumer<Identifier, JLang> lang) {
		Identifier identifier = new Identifier(MODID, id);
		ResourceGen.registerClient(r -> client.accept(t, r, identifier));
		ResourceGen.registerServer(r -> server.accept(r, identifier));
		ResourceGen.registerLang("en_us", l -> lang.accept(identifier, l));
		Registry.register(Registry.ITEM, identifier, new BlockItem(t, new Item.Settings()));
		return Registry.register(Registry.BLOCK, identifier, t);
	}

	static TriConsumer<?, RuntimeResourcePack, Identifier> fullBlockModel() {
		return (o, r, i) -> r.addModel(model("block/cube_all").textures(textures().var("all", i.toString())), ResourceGen.prefixPath(i, "block"));
	}

	static TriConsumer<Block, RuntimeResourcePack, Identifier> fullBlockState() {
		return (b, r, i) -> {
			JVariant variant = JState.variant();
			JState blockState = JState.state(variant);
			for (BlockState state : b.getStateManager()
			                         .getStates()) {
				String str = state.toString();
				variant.put(str.substring(1, str.length() - 1),
				            JState.model(ResourceGen.prefixPath(i, "block")
				                                    .toString()));
			}
			r.addBlockState(blockState, i);
		};
	}

	static void init() {}
}
