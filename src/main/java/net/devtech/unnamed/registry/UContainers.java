package net.devtech.unnamed.registry;

import net.devtech.unnamed.Unnamed;
import net.devtech.unnamed.early.blocks.gui.SolidFireboxBlockGui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

public interface UContainers {
	ScreenHandlerType<SolidFireboxBlockGui> SOLID_FIREBOX = register("solid_firebox", SolidFireboxBlockGui::new, SolidFireboxBlockGui.Screen::new);

	// @formatter:off
	@SuppressWarnings({"MethodCallSideOnly", "RedundantTypeArguments"}) static <T extends ScreenHandler, V extends Screen & ScreenHandlerProvider<T>> ScreenHandlerType<T> register(String id, BlockStateVariantMap.TriFunction<Integer, PlayerInventory, ScreenHandlerContext, T> create, BlockStateVariantMap.TriFunction<T, PlayerEntity, Text, V> client) {
		ScreenHandlerType<T> type = ScreenHandlerRegistry.registerSimple(Unnamed.id(id), (syncId, inventory) -> create.apply(syncId, inventory, ScreenHandlerContext.EMPTY));
		if(Unnamed.CLIENT) {
			ScreenRegistry.<T, V>register(type, (gui, player, title) -> client.apply(gui, player.player, title));
		}

		return type;
	}
	// @formatter:on

	static void init() {}
}
