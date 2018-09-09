package leviathan.utils;

import leviathan.api.render.ISprite;
import leviathan.api.render.ISpriteRenderer;

public class RendererRepeat implements ISpriteRenderer {
	private final ISprite sprite;

	public RendererRepeat(ISprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void draw(int xOffset, int yOffset, int width, int height) {
		final int xTileCount = width / sprite.getWidth();
		final int xRemainder = width - (xTileCount * sprite.getWidth());
		final int yTileCount = height / sprite.getHeight();
		final int yRemainder = height - (yTileCount * sprite.getHeight());

		final int yStart = yOffset + height;

		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int w = (xTile == xTileCount) ? xRemainder : sprite.getWidth();
				int h = (yTile == yTileCount) ? yRemainder : sprite.getHeight();
				int x = xOffset + (xTile * sprite.getWidth());
				int y = yStart - ((yTile + 1) * sprite.getHeight());
				if (w > 0 && h > 0) {
					int maskTop = sprite.getHeight() - h;
					int maskRight = sprite.getWidth() - w;

					sprite.draw(x, y, maskTop, 0, 0, maskRight);
				}
			}
		}
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}
}
