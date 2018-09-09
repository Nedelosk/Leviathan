package leviathan.utils;

import net.minecraft.util.ResourceLocation;

import leviathan.api.render.ISprite;
import leviathan.api.render.ISpriteRenderer;

public class RendererSliced implements ISpriteRenderer {

	private final ISprite origin;
	private final int width;
	private final int height;
	private final ISprite leftTop;
	private final ISprite leftMiddle;
	private final ISprite leftBottom;
	private final ISprite middleTop;
	private final ISprite middleMiddle;
	private final ISprite middleBottom;
	private final ISprite rightTop;
	private final ISprite rightMiddle;
	private final ISprite rightBottom;

	public RendererSliced(ISprite origin, int leftWidth, int rightWidth, int topHeight, int bottomHeight) {
		this.origin = origin;
		this.width = origin.getWidth();
		this.height = origin.getHeight();

		final ResourceLocation resourceLocation = origin.getLocation();
		final int u = origin.getU();
		final int v = origin.getV();
		final int uMiddle = u + leftWidth;
		final int uRight = u + width - rightWidth;
		final int vMiddle = v + topHeight;
		final int vBottom = v + height - bottomHeight;

		final int middleWidth = uRight - uMiddle;
		final int middleHeight = vBottom - vMiddle;

		this.leftTop = new Sprite(resourceLocation, u, v, leftWidth, topHeight);
		this.leftMiddle = new Sprite(resourceLocation, u, vMiddle, leftWidth, middleHeight);
		this.leftBottom = new Sprite(resourceLocation, u, vBottom, leftWidth, bottomHeight);
		this.middleTop = new Sprite(resourceLocation, uMiddle, v, middleWidth, topHeight);
		this.middleMiddle = new Sprite(resourceLocation, uMiddle, vMiddle, middleWidth, middleHeight);
		this.middleBottom = new Sprite(resourceLocation, uMiddle, vBottom, middleWidth, bottomHeight);
		this.rightTop = new Sprite(resourceLocation, uRight, v, rightWidth, topHeight);
		this.rightMiddle = new Sprite(resourceLocation, uRight, vMiddle, rightWidth, middleHeight);
		this.rightBottom = new Sprite(resourceLocation, uRight, vBottom, rightWidth, bottomHeight);
	}

	@Override
	public int getWidth() {
		return leftTop.getWidth() + origin.getWidth() + rightTop.getWidth();
	}

	@Override
	public int getHeight() {
		return leftTop.getHeight() + origin.getHeight() + rightMiddle.getHeight();
	}

	@Override
	public void draw(int xOffset, int yOffset, int width, int height) {
		// corners first
		this.leftTop.draw(xOffset, yOffset);
		this.leftBottom.draw(xOffset, yOffset + height - this.leftBottom.getHeight());
		this.rightTop.draw(xOffset + width - this.rightTop.getWidth(), yOffset);
		this.rightBottom.draw(xOffset + width - this.rightBottom.getWidth(), yOffset + height - this.rightBottom.getHeight());

		// fill in the remaining areas
		final int leftWidth = this.leftTop.getWidth();
		final int rightWidth = this.rightTop.getWidth();
		final int middleWidth = width - leftWidth - rightWidth;
		final int topHeight = this.leftTop.getHeight();
		final int bottomHeight = this.leftBottom.getHeight();
		final int middleHeight = height - topHeight - bottomHeight;
		if (middleWidth > 0) {
			drawTiled(xOffset + leftWidth, yOffset, middleWidth, topHeight, this.middleTop);
			drawTiled(xOffset + leftWidth, yOffset + height - this.leftBottom.getHeight(), middleWidth, bottomHeight, this.middleBottom);
		}
		if (middleHeight > 0) {
			drawTiled(xOffset, yOffset + topHeight, leftWidth, middleHeight, this.leftMiddle);
			drawTiled(xOffset + width - this.rightTop.getWidth(), yOffset + topHeight, rightWidth, middleHeight, this.rightMiddle);
		}
		if (middleHeight > 0 && middleWidth > 0) {
			drawTiled(xOffset + leftWidth, yOffset + topHeight, middleWidth, middleHeight, this.middleMiddle);
		}
	}

	private void drawTiled(final int xOffset, final int yOffset, final int tiledWidth, final int tiledHeight, ISprite texture) {
		final int xTileCount = tiledWidth / texture.getWidth();
		final int xRemainder = tiledWidth - (xTileCount * texture.getWidth());
		final int yTileCount = tiledHeight / texture.getHeight();
		final int yRemainder = tiledHeight - (yTileCount * texture.getHeight());

		final int yStart = yOffset + tiledHeight;

		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int width = (xTile == xTileCount) ? xRemainder : texture.getWidth();
				int height = (yTile == yTileCount) ? yRemainder : texture.getHeight();
				int x = xOffset + (xTile * texture.getWidth());
				int y = yStart - ((yTile + 1) * texture.getHeight());
				if (width > 0 && height > 0) {
					int maskTop = texture.getHeight() - height;
					int maskRight = texture.getWidth() - width;

					texture.draw(x, y, maskTop, 0, 0, maskRight);
				}
			}
		}
	}
}
