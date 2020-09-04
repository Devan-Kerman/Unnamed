package net.devtech.unnamed.early.blocks.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.devtech.unnamed.registry.UContainers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SolidFireboxBlockGui extends SyncedGuiDescription {
	private static final Identifier FURNACE = new Identifier("textures/gui/container/furnace.png");
	private static final float FURNACE_TEXTURE_DIMENSIONS = 256;
	private static final Texture EMPTY_BAR = new Texture(FURNACE,
	                                                     57 / FURNACE_TEXTURE_DIMENSIONS,
	                                                     37 / FURNACE_TEXTURE_DIMENSIONS,
	                                                     70 / FURNACE_TEXTURE_DIMENSIONS,
	                                                     50 / FURNACE_TEXTURE_DIMENSIONS);
	private static final Texture FULL_HEAT = new Texture(FURNACE,
	                                                     176 / FURNACE_TEXTURE_DIMENSIONS,
	                                                     0 / FURNACE_TEXTURE_DIMENSIONS,
	                                                     190 / FURNACE_TEXTURE_DIMENSIONS,
	                                                     16 / FURNACE_TEXTURE_DIMENSIONS);

	public SolidFireboxBlockGui(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(UContainers.SOLID_FIREBOX, syncId, playerInventory, getBlockInventory(context, 1), getBlockPropertyDelegate(context, 2));
		WGridPanel panel = new WGridPanel();
		this.setRootPanel(panel);

		WItemSlot slot = WItemSlot.of(this.blockInventory, 0);
		panel.add(slot, 4, 1);

		WBar bar = new WBar(EMPTY_BAR, FULL_HEAT, 1, 0);
		panel.add(bar, 4, 0);

		panel.add(this.createPlayerInventoryPanel(), 0, 3);

		panel.validate(this);
	}

	public static class Screen extends CottonInventoryScreen<SolidFireboxBlockGui> {
		public Screen(SolidFireboxBlockGui gui, PlayerEntity player, Text title) {
			super(gui, player, title);
		}
	}
}
