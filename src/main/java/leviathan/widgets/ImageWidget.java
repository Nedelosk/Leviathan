package leviathan.widgets;

import net.minecraft.client.renderer.GlStateManager;

import leviathan.api.geometry.Point;
import leviathan.api.geometry.RectTransform;
import leviathan.api.render.IDrawable;
import leviathan.api.widgets.IImageWidget;

public class ImageWidget extends Widget implements IImageWidget {
	private IDrawable drawable;

	public ImageWidget(String name, int x, int y, int width, int height, IDrawable drawable) {
		super(name, x, y, width, height);
		this.drawable = drawable;
	}

	@Override
	public void drawWidget() {
		RectTransform transform = getTransform();
		Point size = transform.getSize();
		GlStateManager.enableAlpha();
		drawable.draw(0, 0, size.getX(), size.getY());
		GlStateManager.disableAlpha();
	}

	public IDrawable getDrawable() {
		return drawable;
	}

	public void setDrawable(IDrawable drawable) {
		this.drawable = drawable;
	}
}
