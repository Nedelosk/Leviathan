package leviathan.api.widgets;

import leviathan.api.render.IDrawable;

public interface IImageWidget extends IWidget {
	void setDrawable(IDrawable drawable);

	IDrawable getDrawable();
}
