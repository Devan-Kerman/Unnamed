package net.devtech.unnamed.registry;

import static net.devtech.arrp.json.models.JModel.model;
import static net.devtech.arrp.json.models.JModel.textures;
import static net.devtech.unnamed.ResourceGen.prefixPath;
import static net.devtech.unnamed.Unnamed.MODID;
import static net.minecraft.util.Formatting.*;

import java.util.function.BiConsumer;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.lang.JLang;
import net.devtech.unnamed.ResourceGen;
import net.devtech.unnamed.items.TooltipItem;

import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface UItems {
	Item WOOD_ASH = register(new TooltipItem(new Item.Settings(), new TranslatableText(MODID + ".tooltip.smile").formatted(GRAY, ITALIC)), "wood_ash", (i, l) -> {
		l.item(i, "Wood Ash");
		l.translate(MODID+".tooltip.smile", "The dust smiles at you");
	});

	static <T extends Item> T register(T t, String id, BiConsumer<Identifier, JLang> lang) {
		return register(t, id, (r, i) -> r.addModel(model("item/generated").textures(textures().layer0(MODID + ":item/" + id)), prefixPath(i, "item")), (r, i) -> {}, lang);
	}

	static <T extends Item> T register(T t, String id, String en_us) {
		return register(t, id, (r, i) -> r.addModel(model("item/generated").textures(textures().layer0(MODID + ":item/" + id)), prefixPath(i, "item")), (r, i) -> {}, (i, l) -> l.item(i, en_us));
	}

	static <T extends Item> T register(T t, String id, BiConsumer<RuntimeResourcePack, Identifier> client,
	                                   BiConsumer<RuntimeResourcePack, Identifier> server, BiConsumer<Identifier, JLang> lang) {
		Identifier identifier = new Identifier(MODID, id);
		ResourceGen.registerClient(r -> client.accept(r, identifier));
		ResourceGen.registerServer(r -> server.accept(r, identifier));
		ResourceGen.registerLang("en_us", l -> lang.accept(identifier, l));
		return Registry.register(Registry.ITEM, identifier, t);
	}

	static void init() {}
}
