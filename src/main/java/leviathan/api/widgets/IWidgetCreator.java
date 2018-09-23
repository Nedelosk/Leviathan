package leviathan.api.widgets;

import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;
import leviathan.api.widgets.builders.IImageBuilder;
import leviathan.api.widgets.builders.ILabelBuilder;

public interface IWidgetCreator {

	ILabelBuilder label(String name, String text);

	IImageBuilder image(String name, IDrawable drawable);

	IImageBuilder image(String name, ISprite sprite);
}
