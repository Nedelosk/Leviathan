package leviathan.api.widgets;

public interface IWindow extends IContainer {
	boolean isMouseOver(IWidget widget);

	IScreen getScreen();
}
