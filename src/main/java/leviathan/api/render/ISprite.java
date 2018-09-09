package leviathan.api.render;

import net.minecraft.util.ResourceLocation;

public interface ISprite {
	int getU();

	int getV();

	int getWidth();

	int getHeight();

	int getTextureWidth();

	int getTextureHeight();

	ResourceLocation getLocation();

	default void draw() {
		draw(0, 0);
	}

	default void draw(int xOffset, int yOffset) {
		draw(xOffset, yOffset, 0, 0, 0, 0);
	}

	void draw(int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight);
}
