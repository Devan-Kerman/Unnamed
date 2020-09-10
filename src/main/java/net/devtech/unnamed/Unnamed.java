package net.devtech.unnamed;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.unnamed.registry.UBlocks;
import net.devtech.unnamed.registry.UContainers;
import net.devtech.unnamed.registry.UItems;
import net.devtech.unnamed.registry.URecipes;
import net.devtech.unnamed.registry.UTiles;

import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Unnamed implements ModInitializer {
	public static final String ID = "unnamed";
	public static final boolean CLIENT = FabricLoader.getInstance()
	                                                 .getEnvironmentType() == EnvType.CLIENT;

	@Override
	public void onInitialize() {
		UItems.init();
		UBlocks.init();
		UTiles.init();
		UContainers.init();
		URecipes.init();
		ResourceGen.registerLang("en_us", r -> {
			r.translate("text.unnamed.wood_ash.info",
			            "To make wood ash, craft a fire starter and light a log on fire, there is a chance it will turn into wood ash.");
			r.translate("text.unnamed.reinforced_stone.info",
			            "To make reinforced stone, make a cobblestone wood ash mix and let it outside to dry.  needs sunlight!");
			r.translate("text.unnamed.heat", "Heat %d/%dF");
			r.translate("text.unnamed.rpm", "RPM %d/%d");
			r.translate("text.unnamed.in_heat", "Hot Reservoir %d/%dF");
			r.translate("text.unnamed.out_heat", "Cold Reservoir %d/%dF");
			r.translate("tooltip.unnamed.stone_flywheel", "Spins slowly, but it retains it's energy for longer");
			r.translate("tooltip.unnamed.wooden_flywheel", "Spins fast, but doesn't retain it's energy for long");
		});
		RRPCallback.EVENT.register(e -> {
			RuntimeResourcePack resource = RuntimeResourcePack.create(ID + ":rrp");
			ResourceGen.init(resource);
			e.add(resource);
		});
	}

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
}
