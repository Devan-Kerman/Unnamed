package net.devtech.unnamed.registry;

import static net.devtech.arrp.json.recipe.JIngredient.ingredient;
import static net.devtech.arrp.json.recipe.JIngredients.ingredients;
import static net.devtech.arrp.json.recipe.JRecipe.shapeless;
import static net.devtech.arrp.json.recipe.JResult.item;
import static net.devtech.unnamed.Unnamed.ID;
import static net.devtech.unnamed.Unnamed.id;
import static net.devtech.unnamed.util.Static.static_;
import static net.minecraft.util.Formatting.GRAY;
import static net.minecraft.util.Formatting.ITALIC;

import net.devtech.unnamed.ResourceGen;
import net.devtech.unnamed.base.items.TooltipItem;
import net.devtech.unnamed.early.items.StoneFlywheel;
import net.devtech.unnamed.early.items.WoodenFlywheel;
import net.devtech.unnamed.util.resource.ResourceGenerateable;

import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface UItems {
	ResourceGenerateable.Item DEFAULT = new ResourceGenerateable.Item() {};
	// @formatter:off
	Item WOOD_ASH = register(DEFAULT, new TooltipItem(new Item.Settings(), new TranslatableText(ID + ".tooltip.smile").formatted(GRAY, ITALIC)), "wood_ash", "Wood Ash");
	Item ASH_CLAY = register(DEFAULT, "ash_clay", "Ash Clay");
	Item FIRE_STARTER = register(DEFAULT, new FlintAndSteelItem(new Item.Settings().maxDamage(4).group(ItemGroup.TOOLS)), "fire_starter", "Fire Starter");
	Item WOODEN_FLYWHEEL = register(DEFAULT, new WoodenFlywheel(new Item.Settings()), "wooden_flywheel", "Wooden Flywheel");
	Item STONE_FLYWHEEL = register(DEFAULT, new StoneFlywheel(new Item.Settings()), "stone_flywheel", "Stone Flywheel");
	Item BROKEN_IRON_ORE = register(DEFAULT, new Item(new Item.Settings()), "broken_iron_ore", "Broken Iron Ore");
	Item IRON_ORE_DUST = register(DEFAULT, new Item(new Item.Settings()), "iron_ore_dust", "Iron Ore Dust");
	// @formatter:on

	// java, can we have static blocks in interfaces?
	// java: no, we have static blocks at home
	// static block at home:
	Object _STATIC_ = static_(() -> {
		ResourceGen.registerLang("en_us", l -> l.translate(ID + ".tooltip.smile", "The dust smiles at you"));
		ResourceGen.registerServer(r -> {
			r.addRecipe(id("ash_clay"),
			            shapeless(ingredients().add(ingredient().item(WOOD_ASH)).add(ingredient().item(Items.CLAY_BALL)), item(ASH_CLAY)));
			r.addRecipe(id("fire_starter"),
			            shapeless(ingredients().add(ingredient().item(Items.STICK)).add(ingredient().item(Items.STICK)).add(ingredient().item(Items.STICK)), item(FIRE_STARTER)));
			r.addRecipe(id("clay_stone_mix"), shapeless(ingredients().add(ingredient().item(ASH_CLAY)).add(ingredient().item(Items.COBBLESTONE)), item(UBlocks.CLAY_COBBLE_MIX.asItem())));
		});
	});

	static Item register(ResourceGenerateable.Item generateable, String id, String en_us) {
		return register(generateable, new Item(new Item.Settings()), id, en_us);
	}

	static <T extends Item> T register(ResourceGenerateable.Item generateable, T t, String id, String en_us) {
		Identifier identifier = new Identifier(ID, id);
		generateable.init(t);
		ResourceGen.registerClient(r -> generateable.client(r, identifier));
		ResourceGen.registerServer(r -> generateable.server(r, identifier));
		ResourceGen.registerLang("en_us", l -> l.item(identifier, en_us));
		return Registry.register(Registry.ITEM, identifier, t);
	}

	static void init() {}
}
