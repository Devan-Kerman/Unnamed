package net.devtech.unnamed.registry;

import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.devtech.unnamed.Unnamed;

import net.minecraft.util.Identifier;

public interface UTextures {
	Identifier FURNACE = new Identifier("textures/gui/container/furnace.png");
	Identifier OBJECT_BAR_EMPTY = Unnamed.id("textures/gui/heat_bar_empty.png");
	Identifier HEAT_BAR_FULL = Unnamed.id("textures/gui/heat_bar_full.png");
	Identifier FLYWHEEL_SLOT = Unnamed.id("textures/gui/slot/flywheel.png");
	float FURNACE_TEXTURE_DIMENSIONS = 256;
	Texture EMPTY_FURNACE_FLAME = of(FURNACE, 57, 37, 13, 13);
	Texture FULL_FURNACE_FLAME = of(FURNACE, 176, 0, 14, 16);
	Texture EMPTY_FURNACE_PROGRESS = of(FURNACE, 80, 35, 22, 16);
	Texture FULL_FURNACE_PROGRESS = of(FURNACE, 177, 14, 22, 16);

	static Texture of(Identifier identifier, int x, int y, int width, int height) {
		return new Texture(identifier,
		                   x / FURNACE_TEXTURE_DIMENSIONS,
		                   y / FURNACE_TEXTURE_DIMENSIONS,
		                   (x + width) / FURNACE_TEXTURE_DIMENSIONS,
		                   (y + height) / FURNACE_TEXTURE_DIMENSIONS);
	}
}
