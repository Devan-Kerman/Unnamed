package net.devtech.unnamed.early.blocks.gui;

import static net.devtech.unnamed.registry.UTextures.HEAT_BAR_FULL;
import static net.devtech.unnamed.registry.UTextures.OBJECT_BAR_EMPTY;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.devtech.unnamed.base.gui.WLabel2;
import net.devtech.unnamed.registry.UBackgrounds;
import net.devtech.unnamed.registry.UContainers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;

public class StirlingEngineGui extends SyncedGuiDescription {
	@SuppressWarnings ("MethodCallSideOnly")
	public StirlingEngineGui(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(UContainers.STIRLING_ENGINE, syncId, playerInventory, getBlockInventory(context, 1), getBlockPropertyDelegate(context, 4));
		WGridPanel panel = new WGridPanel();
		this.setRootPanel(panel);

		WItemSlot flywheel = WItemSlot.of(this.blockInventory, 0);
		flywheel.setBackgroundPainter(UBackgrounds.FLYWHEEL);
		panel.add(flywheel, 0, 3);

		WBar inputHeatBar = new WBar(OBJECT_BAR_EMPTY, HEAT_BAR_FULL, 0, 1, WBar.Direction.RIGHT);
		inputHeatBar.withTooltip("text.unnamed.in_heat");
		panel.add(inputHeatBar, 1, 3, 5, 1);

		WLabel label = new WLabel2("text.unnamed.rpm", 2, 3);
		panel.add(label, 0, 1);

		panel.add(this.createPlayerInventoryPanel(), 0, 4);

		panel.validate(this);
	}

	public static class Screen extends CottonInventoryScreen<StirlingEngineGui> {
		public Screen(StirlingEngineGui gui, PlayerEntity player, Text title) {
			super(gui, player, title);
		}
	}
}
