package net.devtech.unnamed.registry;

import static net.devtech.arrp.json.recipe.JIngredient.ingredient;
import static net.devtech.arrp.json.recipe.JKeys.keys;
import static net.devtech.arrp.json.recipe.JPattern.pattern;
import static net.devtech.arrp.json.recipe.JRecipe.shaped;
import static net.devtech.arrp.json.recipe.JResult.item;
import static net.devtech.unnamed.Unnamed.ID;
import static net.devtech.unnamed.Unnamed.id;
import static net.devtech.unnamed.util.Static.static_;

import net.devtech.unnamed.ResourceGen;
import net.devtech.unnamed.base.blocks.DryingBlock;
import net.devtech.unnamed.base.blocks.HorizontalFacingBlock;
import net.devtech.unnamed.early.blocks.AshesBlock;
import net.devtech.unnamed.early.blocks.GrinderBlock;
import net.devtech.unnamed.early.blocks.SolidFireboxBlock;
import net.devtech.unnamed.early.blocks.StirlingEngineBlock;
import net.devtech.unnamed.util.resource.ResourceGenerateable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface UBlocks {
	ResourceGenerateable.Block DEFAULT = new ResourceGenerateable.Block() {};
	Block ASH_BLOCK = register(AshesBlock.PROVIDED,
	                           new AshesBlock(AbstractBlock.Settings.of(Material.SNOW_LAYER)
	                                                                .strength(0.1F)
	                                                                .sounds(BlockSoundGroup.SAND)),
	                           "ash_block",
	                           "Ash Block");
	Block REINFORCED_STONE = register(DEFAULT,
	                                  new Block(AbstractBlock.Settings.of(Material.STONE)
	                                                                  .sounds(BlockSoundGroup.BASALT)),
	                                  "reinforced_stone",
	                                  "Reinforced Stone");
	Block CLAY_COBBLE_MIX = register(DEFAULT,
	                                 new DryingBlock(AbstractBlock.Settings.of(Material.STONE)
	                                                                       .sounds(BlockSoundGroup.BASALT), REINFORCED_STONE.getDefaultState()),
	                                 "clay_cobble_mix",
	                                 "Cobblestone Ash Clay Mix");
	Block STONE_CHASSIS = register(new ResourceGenerateable.Facing("reinforced_stone", "stone_port", "reinforced_stone", "reinforced_stone"),
	                               new HorizontalFacingBlock(AbstractBlock.Settings.of(Material.STONE)
	                                                                               .sounds(BlockSoundGroup.STONE)),
	                               "stone_chassis",
	                               "Stone Chassis");
	SolidFireboxBlock SOLID_FIREBOX = register(new ResourceGenerateable.FurnaceLike("heat_port",
	                                                                                "solid_firebox",
	                                                                                "reinforced_stone",
	                                                                                "reinforced_stone",
	                                                                                "heat_port",
	                                                                                "solid_firebox_on",
	                                                                                "reinforced_stone",
	                                                                                "reinforced_stone"),
	                                           new SolidFireboxBlock(AbstractBlock.Settings.copy(Blocks.FURNACE)),
	                                           "solid_firebox",
	                                           "Solid Firebox");
	StirlingEngineBlock STIRLING_ENGINE = register(new ResourceGenerateable.Facing("cold_port", "stirling_engine", "stone_port", "heat_port"),
	                                               new StirlingEngineBlock(AbstractBlock.Settings.copy(Blocks.STONE)),
	                                               "stirling_engine",
	                                               "Stirling Engine");
	GrinderBlock GRINDER_BLOCK = register(new ResourceGenerateable.Facing("grinder_top", "grinder", "stone_port", "stone_port"), new GrinderBlock(AbstractBlock.Settings.copy(Blocks.STONE)), "grinder", "Grinder");

	Object _STATIC_ = static_(() -> {
		//
		ResourceGen.registerServer(r -> {
			// furnace recipe but not
			r.addRecipe(id("stone_chassis"),
			            shaped(pattern("###", "#X#", "###"),
			                   keys().key("#", ingredient().item(REINFORCED_STONE.asItem()))
			                         .key("X", ingredient().item(Items.IRON_INGOT)),
			                   item(STONE_CHASSIS.asItem())));
		});
	});

	static <T extends Block> T register(ResourceGenerateable.Block resource, T t, String id, String en_us) {
		Identifier identifier = new Identifier(ID, id);
		resource.init(t);
		ResourceGen.registerClient(r -> resource.client(r, identifier));
		ResourceGen.registerServer(r -> resource.server(r, identifier));
		ResourceGen.registerLang("en_us", l -> l.block(identifier, en_us));
		Registry.register(Registry.ITEM, identifier, new BlockItem(t, new Item.Settings()));
		return Registry.register(Registry.BLOCK, identifier, t);
	}

	static void init() {}
}
