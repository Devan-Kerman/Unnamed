package net.devtech.unnamed.registry;

import net.devtech.mcrf.elements.ElementParser;
import net.devtech.mcrf.recipes.RecipeSchema;
import net.devtech.mcrf.util.Id;
import net.devtech.mcrf.util.RefreshingRecipe;
import net.devtech.unnamed.Unnamed;

public interface URecipes {
	RefreshingRecipe GRINDING_RECIPE = new RefreshingRecipe(Unnamed.id("recipes/grinding"), new RecipeSchema.Builder(id("grinding")).addInput(ElementParser.MCRF_INGREDIENT).addInput(ElementParser.INTEGER).addOutput(ElementParser.ITEM_STACK));

	static void init() {}

	/**
	 * ah yes, the id is made of id
	 */
	static Id id(String id) {
		return new Id(Unnamed.ID, id);
	}
}
