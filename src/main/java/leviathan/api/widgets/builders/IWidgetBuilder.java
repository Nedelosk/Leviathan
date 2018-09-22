package leviathan.api.widgets.builders;

import leviathan.api.geometry.Vector;
import leviathan.api.widgets.IWidget;

public interface IWidgetBuilder<W extends IWidget, B extends IWidgetBuilder> {

	B setName(String name);

	B setPosition(Vector position);

	B setSize(Vector size);

	B setX(int x);

	B setY(int y);

	B setWidth(int width);

	B setHeight(int height);

	W create();
}
