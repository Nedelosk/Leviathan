package leviathan.utils;

import leviathan.api.render.ISprite;
import leviathan.api.render.ISpriteRenderer;

public class RendererSimple implements ISpriteRenderer {
	private final ISprite sprite;

	public RendererSimple(ISprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void draw(int xOffset, int yOffset, int width, int height) {
		sprite.draw(xOffset, yOffset);
	}

	@Override
	public int getWidth() {
		return sprite.getWidth();
	}

	@Override
	public int getHeight() {
		return sprite.getHeight();
	}
}
