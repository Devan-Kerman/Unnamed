package net.devtech.unnamed.early.blocks.gui;

import static net.devtech.unnamed.registry.UTextures.EMPTY_FURNACE_FLAME;
import static net.devtech.unnamed.registry.UTextures.FULL_FURNACE_FLAME;
import static net.devtech.unnamed.registry.UTextures.HEAT_BAR_FULL;
import static net.devtech.unnamed.registry.UTextures.OBJECT_BAR_EMPTY;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.devtech.unnamed.registry.UContainers;
import net.devtech.unnamed.registry.UTextures;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;

public class GrinderGui extends SyncedGuiDescription {
	public GrinderGui(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(UContainers.GRINDER_GUI, syncId, playerInventory, getBlockInventory(context, 2), getBlockPropertyDelegate(context, 2));
		WGridPanel panel = new WGridPanel();
		this.setRootPanel(panel);

		WItemSlot input = WItemSlot.of(this.blockInventory, 0);
		panel.add(input, 2, 1);

		WItemSlot output = WItemSlot.of(this.blockInventory, 1);
		panel.add(output, 5, 1);

		WBar bar = new WBar(UTextures.EMPTY_FURNACE_PROGRESS, UTextures.FULL_FURNACE_PROGRESS, 0, 1, WBar.Direction.LEFT);
		panel.add(bar, 3, 1, 2, 1);

		panel.add(this.createPlayerInventoryPanel(), 0, 3);
		panel.validate(this);
	}

	public static class Screen extends CottonInventoryScreen<GrinderGui> {
		public Screen(GrinderGui gui, PlayerEntity player, Text title) {
			super(gui, player, title);
		}
	}
}
