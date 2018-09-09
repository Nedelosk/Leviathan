package leviathan.api.gui;

import leviathan.api.render.IDrawable;

public interface ITextureWidget extends IWidget {
	void setDrawable(IDrawable drawable);

	IDrawable getDrawable();
}
