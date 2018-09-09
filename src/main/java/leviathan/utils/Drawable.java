package leviathan.utils;

import com.google.common.base.MoreObjects;

import net.minecraft.util.ResourceLocation;

import leviathan.api.render.DrawMode;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;
import leviathan.api.render.ISpriteRenderer;

public class Drawable implements IDrawable {
	public static final Drawable MISSING = new Drawable();

	private DrawMode mode;
	private ISprite sprite;
	private ISpriteRenderer renderer;

	public Drawable() {
		this.mode = DrawMode.SIMPLE;
		this.sprite = new Sprite(new ResourceLocation("missingno"), 0, 0, 16, 16);
		this.renderer = mode.createRenderer(sprite);
	}

	public Drawable(DrawMode mode, ISprite sprite) {
		this.mode = mode;
		this.sprite = sprite;
		this.renderer = mode.createRenderer(sprite);
	}

	@Override
	public void setMode(DrawMode mode) {
		this.mode = mode;
		this.renderer = mode.createRenderer(sprite);
	}

	@Override
	public DrawMode getMode() {
		return mode;
	}

	@Override
	public void setSprite(ResourceLocation textureLocation, int u, int v, int width, int height) {
		setSprite(new Sprite(textureLocation, u, v, width, height));
	}

	@Override
	public void setSprite(ResourceLocation textureLocation, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		setSprite(new Sprite(textureLocation, u, v, width, height, textureWidth, textureHeight));
	}

	@Override
	public void setSprite(ISprite sprite) {
		this.sprite = sprite;
		this.renderer = mode.createRenderer(sprite);
	}

	@Override
	public ISprite getSprite() {
		return sprite;
	}

	@Override
	public ISpriteRenderer getRenderer() {
		return renderer;
	}

	@Override
	public void draw(int xOffset, int yOffset, int width, int height) {
		renderer.draw(xOffset, yOffset, width, height);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("sprite", sprite)
			.add("mode", mode)
			.toString();
	}
}
