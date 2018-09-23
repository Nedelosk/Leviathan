package leviathan.api.widgets.builders;

import leviathan.api.geometry.Point;
import leviathan.api.widgets.IWidget;

public interface IWidgetBuilder<W extends IWidget, B extends IWidgetBuilder> {

	B setName(String name);

	B setPosition(Point position);

	B setSize(Point size);

	B setX(int x);

	B setY(int y);

	B setWidth(int width);

	B setHeight(int height);

	W create();
}
