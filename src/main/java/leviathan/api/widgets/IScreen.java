package leviathan.api.widgets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.Pair;

import leviathan.api.events.EventKey;
import leviathan.api.events.JEGEvent;
import leviathan.api.geometry.Vector;

public interface IScreen extends Iterable<IWindow> {
	void drawWindows();

	void addWindow(String name, IWindow windowWidget);

	IWindow createWindow(String name, int width, int height);

	Optional<IWindow> getWindow(String name);

	Optional<IWidget> getWidget(String windowName, String widgetName);

	Collection<IWindow> getWindows();

	Optional<IWindow> getWindowUnderMouse();

	Optional<IWindow> getWindowUnderPosition(Vector position);

	Vector getMousePosition();

	Vector getSize();

	Collection<Pair<IWindow, Rectangle>> getWindowAreas();

	boolean isOverWindowArea(Vector position);

	boolean isFocused(IWidget widget);

	boolean isDragged(IWidget widget);

	boolean isMouseOver(IWidget widget);

	Collection<IWidget> getHoveredWidgets();

	@Nullable
	IWidget getFocusedWidget();

	void setFocusedWidget(@Nullable IWidget widget);

	@Nullable
	IWidget getDraggedWidget();

	void setDraggedWidget(@Nullable IWidget widget);

	void setDraggedWidget(@Nullable IWidget widget, int button);

	void resize(int width, int height);

	void updateScreen(int mouseX, int mouseY);

	default <E extends JEGEvent> void sendEventToHover(EventKey<E> eventKey, Function<IWidget, E> eventFactory, boolean single){
		sendEventToHover(widget -> widget.hasListener(eventKey), eventFactory, single);
	}

	<E extends JEGEvent> void sendEventToHover(Predicate<IWidget> widgetPredicate, Function<IWidget, E> eventFactory, boolean single);

	List<String> getTooltip();

	@Override
	@Nonnull
	default Iterator<IWindow> iterator() {
		return getWindows().iterator();
	}
}
