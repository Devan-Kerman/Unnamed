package net.devtech.unnamed.compat.rei;

import me.shedaniel.rei.api.BuiltinPlugin;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.devtech.unnamed.registry.UBlocks;
import net.devtech.unnamed.registry.UItems;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UREI implements REIPluginV0 {
	@Override
	public Identifier getPluginIdentifier() {
		return new Identifier("unnamed:rei_plugin");
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		BuiltinPlugin.getInstance().registerInformation(EntryStack.create(UItems.WOOD_ASH), new TranslatableText("item.unnamed.wood_ash"), texts -> {
			texts.add(new TranslatableText("text.unnamed.wood_ash.info"));
			return texts;
		});
		BuiltinPlugin.getInstance().registerInformation(EntryStack.create(UBlocks.REINFORCED_STONE), new TranslatableText("block.unnamed.reinforced_stone"), texts -> {
			texts.add(new TranslatableText("text.unnamed.reinforced_stone.info"));
			return texts;
		});
	}
}
