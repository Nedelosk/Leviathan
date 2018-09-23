package leviathan.api.widgets.builders;

import leviathan.api.render.DrawMode;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;
import leviathan.api.widgets.IImageWidget;

public interface IImageBuilder extends IWidgetBuilder<IImageWidget, IImageBuilder> {
	IImageBuilder setDrawable(IDrawable drawable);

	IImageBuilder setMode(DrawMode mode);

	IImageBuilder setSprite(ISprite texture);
}
