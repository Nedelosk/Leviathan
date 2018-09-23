package leviathan.api.widgets;

import java.util.Collection;

public interface IWindow extends IContainer {
	boolean isMouseOver(IWidget widget);

	IScreen getScreen();

	Collection<IWidget> getWidgets(String name);
}
