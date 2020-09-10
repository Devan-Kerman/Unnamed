package net.devtech.unnamed.registry;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.SLOT;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;

public interface UBackgrounds {
	BackgroundPainter FLYWHEEL = (x, y, w) -> {
		SLOT.paintBackground(x, y, w);
		ScreenDrawing.texturedRect(x, y, 18, 18, UTextures.FLYWHEEL_SLOT, -1);
	};
}
