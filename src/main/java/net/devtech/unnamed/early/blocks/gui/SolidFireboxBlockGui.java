package net.devtech.unnamed.early.blocks.gui;

import static net.devtech.unnamed.registry.UTextures.EMPTY_FURNACE_FLAME;
import static net.devtech.unnamed.registry.UTextures.FULL_FURNACE_FLAME;
import static net.devtech.unnamed.registry.UTextures.OBJECT_BAR_EMPTY;
import static net.devtech.unnamed.registry.UTextures.HEAT_BAR_FULL;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.devtech.unnamed.registry.UContainers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;

public class SolidFireboxBlockGui extends SyncedGuiDescription {
	public SolidFireboxBlockGui(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(UContainers.SOLID_FIREBOX, syncId, playerInventory, getBlockInventory(context, 1), getBlockPropertyDelegate(context, 4));
		WGridPanel panel = new WGridPanel();
		this.setRootPanel(panel);

		WItemSlot slot = WItemSlot.of(this.blockInventory, 0);
		panel.add(slot, 4, 1);

		WBar bar = new WBar(EMPTY_FURNACE_FLAME, FULL_FURNACE_FLAME, 1, 0);
		panel.add(bar, 4, 0);

		WBar heatBar = new WBar(OBJECT_BAR_EMPTY, HEAT_BAR_FULL, 2, 3, WBar.Direction.RIGHT);
		heatBar.withTooltip("text.unnamed.heat");
		panel.add(heatBar, 2, 2, 5, 1);

		panel.add(this.createPlayerInventoryPanel(), 0, 3);

		panel.validate(this);
	}

	public static class Screen extends CottonInventoryScreen<SolidFireboxBlockGui> {
		public Screen(SolidFireboxBlockGui gui, PlayerEntity player, Text title) {
			super(gui, player, title);
		}
	}
}
