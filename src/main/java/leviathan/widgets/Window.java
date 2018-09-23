package leviathan.widgets;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IScreen;
import leviathan.api.widgets.IWidget;
import leviathan.api.widgets.IWindow;

public class Window extends Container implements IWindow {

	private final Screen screen;

	public Window(Screen screen, String name, int width, int height) {
		super(name, 0, 0, width, height);
		this.screen = screen;
	}

	@Override
	protected void drawWidget() {
		validate();
		super.drawWidget();
	}

	@Override
	public boolean isMouseOver(IWidget widget) {
		return screen.isMouseOver(widget);
	}

	@Override
	public IScreen getScreen() {
		return screen;
	}

	@Override
	public Collection<IWidget> getWidgets(String name) {
		return getChildren().stream()
			.filter(widget -> widget instanceof IContainer)
			.map(widget -> ((IContainer) widget).getWidget(name))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}
}
