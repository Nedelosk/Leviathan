package leviathan.widgets.builders;

import leviathan.api.render.DrawMode;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IImageWidget;
import leviathan.api.widgets.builders.IImageBuilder;
import leviathan.utils.Drawable;
import leviathan.widgets.ImageWidget;

public class ImageBuilder extends WidgetBuilder<IImageWidget, IImageBuilder> implements IImageBuilder {
	private IDrawable drawable;

	public ImageBuilder(IContainer container, String name, ISprite sprite) {
		super(container, name);
		this.drawable = new Drawable(DrawMode.SIMPLE, sprite);
	}

	public ImageBuilder(IContainer container, String name, IDrawable drawable) {
		super(container, name);
		this.drawable = drawable;
	}

	@Override
	public IImageBuilder setDrawable(IDrawable drawable) {
		this.drawable = drawable;
		return this;
	}

	@Override
	public IImageBuilder setMode(DrawMode mode) {
		this.drawable = new Drawable(mode, drawable.getSprite());
		return this;
	}

	@Override
	public IImageBuilder setSprite(ISprite texture) {
		this.drawable = new Drawable(drawable.getMode(), texture);
		return this;
	}

	@Override
	public IImageWidget create() {
		return new ImageWidget(name, x, y, width, height, drawable);
	}
}
